package org.sheep1500.toyadvertisementbackend.ads_join.mq.producer;

import org.sheep1500.toyadvertisementbackend.ads_join.mq.event.AdsJoinEvent;
import org.sheep1500.toyadvertisementbackend.mq.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class AdsJoinProducer {
    private final RabbitTemplate rabbitTemplate;

    public AdsJoinProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendJoinAdsEvent(AdsJoinEvent event) {
        String routingKey = "ads." + event.getAdId();
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, routingKey, event);
    }
}
