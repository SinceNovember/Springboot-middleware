package com.simple.binlog;

import com.alibaba.fastjson.JSON;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.simple.dto.BinlogDto;
import com.simple.kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 监听binlog日志动作,并发送信息
 */
public class BinlogListener  implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${binlog.host}")
    private String host;

    @Value("${binlog.port}")
    private int port;

    @Value("${binlog.user}")
    private String user;

    @Value("${binlog.password}")
    private String password;

    // binlog server_id
    @Value("${server.id}")
    private long serverId;

    // kafka话题
    @Value("${kafka.topic}")
    private String topic;

    // kafka分区
    @Value("${kafka.partNum}")
    private int partNum;

    // Kafka备份数
    @Value("${kafka.repeatNum}")
    private short repeatNum;

    // kafka地址
    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaHost;

    // 指定监听的数据表
    @Value("${binlog.database.table}")
    private String database_table;

    @Resource
    private KafkaProducer kafkaProducer;

    @Async
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            // 创建topic
            kafkaProducer.createTopic(kafkaHost, topic, partNum, repeatNum);
            // 获取监听数据表数组
            List<String> databaseList = Arrays.asList(database_table.split(","));
            HashMap<Long, String> tableMap = new HashMap<Long, String>();
            // 创建binlog监听客户端
            BinaryLogClient client = new BinaryLogClient(host, port, user, password);
            client.setServerId(serverId);
            client.registerEventListener(logEvent -> {
                EventData data = logEvent.getData();
                if (null != data) {
                    // binlog事件
                    if (data instanceof TableMapEventData) {
                        TableMapEventData tableMapEventData = (TableMapEventData) data;
                        tableMap.put(tableMapEventData.getTableId(), tableMapEventData.getDatabase() + "." + tableMapEventData.getTable());
                    }
                    //update数据
                    if (data instanceof UpdateRowsEventData) {
                        UpdateRowsEventData updateRowsEventData = (UpdateRowsEventData) data;
                        String tableName = tableMap.get(updateRowsEventData.getTableId());
                        if (tableName != null && databaseList.contains(tableName)) {
                            String eventKey = tableName + ".update";
                            updateRowsEventData.getRows().forEach(row -> {
                                String msg = JSON.toJSONString(new BinlogDto(eventKey, row.getValue()));
                                kafkaProducer.send(topic, msg);
                            });
                        }
                    }    // insert数据
                    else if (data instanceof WriteRowsEventData) {
                        WriteRowsEventData writeRowsEventData = (WriteRowsEventData) data;
                        String tableName = tableMap.get(writeRowsEventData.getTableId());
                        if (tableName != null && databaseList.contains(tableName)) {
                            String eventKey = tableName + ".insert";
                            for (Serializable[] row : writeRowsEventData.getRows()) {
                                String msg = JSON.toJSONString(new BinlogDto(eventKey, row));
                                kafkaProducer.send(topic, msg);
                            }
                        }
                    }
                    // delete数据
                    else if (data instanceof DeleteRowsEventData) {
                        DeleteRowsEventData deleteRowsEventData = (DeleteRowsEventData) data;
                        String tableName = tableMap.get(deleteRowsEventData.getTableId());
                        if (tableName != null && databaseList.contains(tableName)) {
                            String eventKey = tableName + ".delete";
                            for (Serializable[] row : deleteRowsEventData.getRows()) {
                                String msg = JSON.toJSONString(new BinlogDto(eventKey, row));
                                kafkaProducer.send(topic, msg);
                            }
                        }
                    }
                }
            });
            client.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
