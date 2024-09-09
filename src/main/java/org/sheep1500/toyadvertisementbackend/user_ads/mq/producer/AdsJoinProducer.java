package org.sheep1500.toyadvertisementbackend.user_ads.mq.producer;

import org.sheep1500.toyadvertisementbackend.user_ads.mq.event.AdsJoinEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdsJoinProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendAdsJoinEvent(AdsJoinEvent event) {
        rabbitTemplate.convertAndSend("ad.join.queue", event);
    }
}
