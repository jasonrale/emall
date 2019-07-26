package com.emall.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MessageProducer {
    private static Logger logger = LoggerFactory.getLogger(MessageProducer.class);

    @Resource
    RabbitTemplate rabbitTemplate;

    public void send(Object message) {
        logger.info("Send message:" + message);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_A, message);
    }
}
