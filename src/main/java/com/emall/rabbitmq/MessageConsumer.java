package com.emall.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    @RabbitListener(queues = RabbitConfig.QUEUE_A)
    public void receive(@Payload Object message) {
        logger.info("receive message:" + message);
    }
}
