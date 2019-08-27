package com.emall.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String SECKILL_QUEUE = "seckill.queue";
    public static final String SECKILL_EXCHANGE = "seckillExchange";
//    //Fanout广播
//    public static final String FANOUT_QUEUE1 = "fanout.queue1";
//    public static final String FANOUT_QUEUE2 = "fanout.queue2";
//
//    public static final String FANOUT_EXCHANGE = "fanoutExchange";
//
//    //Direct直连
//    public static final String DIRECT_QUEUE1 = "direct.queue1";
//    public static final String DIRECT_QUEUE2 = "direct.queue2";
//
//    public static final String DIRECT_EXCHANGE = "directExchange";
//
//    public static final String DIRECT_KEY1 = "direct.key1";
//    public static final String DIRECT_KEY2 = "direct.key2";
//
//    //Topic主题
//    public static final String TOPIC_QUEUE1 = "topic.queue1";
//    public static final String TOPIC_QUEUE2 = "topic.queue2";
//
//    public static final String TOPIC_EXCHANGE = "topicExchange";
//
//    public static final String TOPIC_KEY1 = "topic.key1";
//    public static final String TOPIC_KEY2 = "topic.key2";
//    public static final String TOPIC_KEY3 = "topic.#";
//
//    //Headers首部
//    public static final String HEADERS_QUEUE1 = "headers.queue1";
//    public static final String HEADERS_QUEUE2 = "headers.queue2";
//
//    public static final String HEADERS_EXCHANGE = "headersExchange";

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 创建秒杀消息队列
     * @return
     */
    @Bean
    public Queue seckillQueue() {
        return new Queue(SECKILL_QUEUE, true);
    }

    /**
     * 秒杀扇形交换机
     * @return
     */
    @Bean
    public FanoutExchange seckillExchange() {
        return new FanoutExchange(SECKILL_EXCHANGE);
    }

    /**
     * 秒杀队列绑定表啥交换机
     * @return
     */
    @Bean
    public Binding fanoutBinding1() {
        return BindingBuilder.bind(seckillQueue()).to(seckillExchange());
    }

//    /**
//     * Fanout 扇形交换机模式
//     *
//     * @return
//     */
//    @Bean
//    public Queue fanoutQueue1() {
//        return new Queue(FANOUT_QUEUE1, true);
//    }
//
//    @Bean
//    public Queue fanoutQueue2() {
//        return new Queue(FANOUT_QUEUE2, true);
//    }
//
//    @Bean
//    public FanoutExchange fanoutExchange() {
//        return new FanoutExchange(FANOUT_EXCHANGE);
//    }
//
//    @Bean
//    public Binding fanoutBinding1() {
//        return BindingBuilder.bind(fanoutQueue1()).to(fanoutExchange());
//    }
//
//    @Bean
//    public Binding fanoutBinding2() {
//        return BindingBuilder.bind(fanoutQueue2()).to(fanoutExchange());
//    }
//
//    /**
//     * Direct直连交换机模式
//     *
//     * @return
//     */
//    @Bean
//    public Queue directQueue1() {
//        return new Queue(DIRECT_QUEUE1, true);
//    }
//
//    @Bean
//    public Queue directQueue2() {
//        return new Queue(DIRECT_QUEUE2, true);
//    }
//
//    @Bean
//    public DirectExchange directExchange() {
//        return new DirectExchange(DIRECT_EXCHANGE);
//    }
//
//    @Bean
//    public Binding directBinding1() {
//        return BindingBuilder.bind(directQueue1()).to(directExchange()).with(DIRECT_KEY1);
//    }
//
//    @Bean
//    public Binding directBinding2() {
//        return BindingBuilder.bind(directQueue2()).to(directExchange()).with(DIRECT_KEY2);
//    }
//
//    /**
//     * Topic主题交换机模式
//     *
//     * @return
//     */
//    @Bean
//    public Queue topicQueue1() {
//        return new Queue(TOPIC_QUEUE1, true);
//    }
//
//    @Bean
//    public Queue topicQueue2() {
//        return new Queue(TOPIC_QUEUE2, true);
//    }
//
//    @Bean
//    public TopicExchange topicExchange() {
//        return new TopicExchange(TOPIC_EXCHANGE);
//    }
//
//    @Bean
//    public Binding topicBinding1() {
//        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with(TOPIC_KEY1);
//    }
//
//    @Bean
//    public Binding topicBinding2() {
//        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with(TOPIC_KEY3);
//    }
//
//    /**
//     * Header首部交换机模式
//     *
//     * @return
//     */
//    @Bean
//    public Queue headersQueue1() {
//        return new Queue(HEADERS_QUEUE1, true);
//    }
//
//    @Bean
//    public Queue headersQueue2() {
//        return new Queue(HEADERS_QUEUE2, true);
//    }
//
//    @Bean
//    public HeadersExchange headersExchange() {
//        return new HeadersExchange(HEADERS_EXCHANGE);
//    }
//
//    @Bean
//    public Binding headersBinding1() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("header1", "value1");
//        map.put("header2", "value2");
//        return BindingBuilder.bind(headersQueue1()).to(headersExchange()).whereAll(map).match();
//    }
//
//    @Bean
//    public Binding headersBinding2() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("header3", "value3");
//        map.put("header4", "value4");
//        return BindingBuilder.bind(headersQueue2()).to(headersExchange()).whereAll(map).match();
//    }
}
