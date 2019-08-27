package com.emall.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MessageProducer {
    @Resource
    RabbitTemplate rabbitTemplate;

    public void sendSeckillMessage(Object message) {
        rabbitTemplate.convertAndSend(RabbitConfig.SECKILL_EXCHANGE, "", message);
    }

//    public void sendFanout(Object message) {
//        rabbitTemplate.convertAndSend(RabbitConfig.FANOUT_EXCHANGE, "", message);
//    }
//
//    public void sendDirect(Object message) {
//        rabbitTemplate.convertAndSend(RabbitConfig.DIRECT_EXCHANGE, RabbitConfig.DIRECT_KEY1, message);
//        rabbitTemplate.convertAndSend(RabbitConfig.DIRECT_EXCHANGE, RabbitConfig.DIRECT_KEY2, message);
//    }
//
//    public void sendTopic(Object message) {
//        rabbitTemplate.convertAndSend(RabbitConfig.TOPIC_EXCHANGE, RabbitConfig.TOPIC_KEY1, message);
//        rabbitTemplate.convertAndSend(RabbitConfig.TOPIC_EXCHANGE, RabbitConfig.TOPIC_KEY2, message);
//    }
//
//    public void sendHeaders(Object message) {
//        Map<String, Object> map1 = new HashMap<>();
//        map1.put("header1", "value1");
//        map1.put("header2", "value2");
//
//        Map<String, Object> map2 = new HashMap<>();
//        map2.put("header3", "value3");
//
//        MessagePostProcessor messagePostProcessor1 = getMessagePostProcessor(MessageDeliveryMode.PERSISTENT, "application/json", map1);
//        MessagePostProcessor messagePostProcessor2 = getMessagePostProcessor(MessageDeliveryMode.PERSISTENT, "application/json", map2);
//
//        rabbitTemplate.convertAndSend(RabbitConfig.HEADERS_EXCHANGE, "", message, messagePostProcessor1);
//        rabbitTemplate.convertAndSend(RabbitConfig.HEADERS_EXCHANGE, "", message, messagePostProcessor2);
//    }
//
//    private MessagePostProcessor getMessagePostProcessor(MessageDeliveryMode deliveryMode, String contentType, Map<? extends String, ?> map) {
//        return msg -> {
//            MessageProperties messageProperties = msg.getMessageProperties();
//            messageProperties.setDeliveryMode(deliveryMode);
//            messageProperties.setContentType(contentType);
//            messageProperties.getHeaders().putAll(map);
//            return msg;
//        };
//    }
}
