package ug.daes.onboarding.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ug.daes.onboarding.dto.LogModelDTO;

@Service
public class KafkaSender {
    private static Logger logger = LoggerFactory.getLogger(KafkaSender.class);


    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topicName;
    private final String centralTopicName;

    public KafkaSender(KafkaTemplate<String, Object> kafkaTemplate,
                       @org.springframework.beans.factory.annotation.Value("${com.dt.kafka.topic}") String topicName,
                       @org.springframework.beans.factory.annotation.Value("${com.dt.kafka.topic.central}") String centralTopicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
        this.centralTopicName = centralTopicName;
    }

    public void send(LogModelDTO logmodel) {
        logger.info("Kafka -> Sending LogModelDTO to topic: {}", topicName);
        logger.info("Central Topic =>{} ", centralTopicName);
        kafkaTemplate.send(topicName, logmodel);

        kafkaTemplate.send(centralTopicName, logmodel);
    }


    public void sendString(String logmodel) {
        logger.info("Kafka -> Sending String message to topic: {}", topicName);
        kafkaTemplate.send(topicName, logmodel);

        kafkaTemplate.send(centralTopicName, logmodel);
    }
}
