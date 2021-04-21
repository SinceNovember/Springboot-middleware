package com.simple.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * kafka消息生产
 */
@Component
public class KafkaProducer {

    private static Logger log = LoggerFactory.getLogger(KafkaProducer.class);

    @Resource
    private KafkaTemplate kafkaTemplate;

    /**
     * 创建topic
     * @param host
     * @param topic
     * @param partNum
     * @param repeatNum
     */
    public void createTopic(String host, String topic, int partNum, short repeatNum) {
        Properties props = new Properties();
        props.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,host);
        NewTopic newTopic = new NewTopic(topic, partNum, repeatNum);
        AdminClient adminClient = AdminClient.create(props);
        List<NewTopic> topicList = Arrays.asList(newTopic);
        adminClient.createTopics(topicList);
        adminClient.close(10, TimeUnit.SECONDS);
    }

    /**
     * 发送消息到kafka
     * @param topic
     * @param msg
     */
    public void send(String topic,String msg){
        Message message=new Message();
        message.setId(System.currentTimeMillis());
        message.setMsg(msg);
        message.setSendTime(new Date());
        kafkaTemplate.send(topic, msg);
        log.info("send info success: topic->{},msg->{}", topic, msg);
    }
}
