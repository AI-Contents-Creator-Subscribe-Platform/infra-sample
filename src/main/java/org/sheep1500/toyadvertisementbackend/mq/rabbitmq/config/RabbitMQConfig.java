package org.sheep1500.toyadvertisementbackend.mq.rabbitmq.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue adjoinQueue() {
        return new Queue("ad.join.queue", true); // Durable queue
    }
}
