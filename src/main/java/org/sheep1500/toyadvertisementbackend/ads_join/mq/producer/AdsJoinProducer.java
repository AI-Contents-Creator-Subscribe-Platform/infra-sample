package org.sheep1500.toyadvertisementbackend.ads_join.mq.producer;

import org.sheep1500.toyadvertisementbackend.ads_join.mq.event.AdsJoinEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class AdsJoinProducer {
    private final RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "ads.join.exchange";

    public AdsJoinProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendJoinAdsEvent(AdsJoinEvent event) {
        String routingKey = "ads.join." + event.getAdId();
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, routingKey, event);
    }
}
