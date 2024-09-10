package org.sheep1500.toyadvertisementbackend.mq.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue adjoinQueue() {
        return new Queue("ad.join.queue", true); // Durable queue
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("ads.join-queue.exchange");
    }
}
