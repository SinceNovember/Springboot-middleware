package com.simple.kafka;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.simple.es.ESService;
import io.searchbox.action.BulkableAction;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Component
public class KafkaConsumer {

    private static Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @Value("${es.format}")
    String format;

    @Value("${es.index}")
    private String esIndex;

    @Resource
    private ESService esService;

    @KafkaListener(topics ="#{kafkaTopicName}", id = "#{topicGroupId}", containerFactory = "batchFactory", errorHandler = "consumerAwareListenerErrorHandler")
    public void receiveBinlogMsg(List<ConsumerRecord<?, ?>> list) {
        List<String> messages = new ArrayList<>();
        list.forEach(item -> {
            Optional<?> kafkaMsg = Optional.ofNullable(item.value());
            kafkaMsg.ifPresent(o -> messages.add(o.toString()));
        });
        if (messages.size() > 0) {
            updateES(messages);
        }
    }


    private void updateES(List<String> messages) {
        List<BulkableAction> list = new ArrayList<>();

        for (String message : messages) {
            JSONObject result = null;
            try {
                result = JSON.parseObject(message);
            } catch (Exception e) {
                continue;
            }
            // 获取事件类型 event:"wtv3.videos.insert"
            String event = (String) result.get("event");
            String[] eventArray = event.split("\\.");
            String tableName = eventArray[1];
            String eventType = eventArray[2];
            // 获取具体数据
            JSONArray valueStr = (JSONArray) result.get("value");
            // 转化为对应格式的json字符串
            JSONObject object = getESObject(valueStr);
            switch (eventType) {
                case "insert":;
                case "update":
                    list.add(esService.getUpdateIndex(object.getString("id"), esIndex, object));
                    break;
                case "delete":
                    list.add(esService.getDeleteIndex(object.getString("id"), esIndex));
                    break;
            }
        }
        //批量执行操作
        esService.executeESClientRequest(list, esIndex);
    }

    /**
     * 获取解析后的ES对象
     * @param message
     * @param
     * @return
     */
    private JSONObject getESObject(JSONArray message) {
        JSONObject resultObject = new JSONObject();
        if (!format.isEmpty()) {
            LinkedHashMap<String, Object> jsonFormatObject = JSON.parseObject(format, new TypeReference<LinkedHashMap<String, Object>>() {
            });
            int i = 0;
            for (String key : jsonFormatObject.keySet()) {
                if (key.toLowerCase().contains("time") && StringUtils.isNotBlank(message.getString(i))) {
                    resultObject.put(key, DateFormatUtils.format(message.getDate(i++),"yyyy-MM-dd HH:mm:ss"));
                } else {
                    resultObject.put(key, message.get(i++));
                }

            }
        }

        return resultObject;
    }

}
