package org.sheep1500.toyadvertisementbackend.user_ads_worker.mq.consumer;

import org.sheep1500.toyadvertisementbackend.user_ads.domain.AdsJoinHistory;
import org.sheep1500.toyadvertisementbackend.user_ads.domain.AdsJoinHistoryRepository;
import org.sheep1500.toyadvertisementbackend.user_ads.mq.event.AdsJoinEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AdsJoinConsumer {
    @Autowired
    private AdsJoinHistoryRepository adsJoinHistoryRepository;

    @RabbitListener(queues = "ad.join.queue")
    public void handleAdParticipation(AdsJoinEvent event) {

        // Check if the user has already participated in this ad
        Optional<AdsJoinHistory> existingParticipation =
                adsJoinHistoryRepository.findByUserIdAndAdId(event.getUserId(), event.getAdId());

        if (existingParticipation.isEmpty()) {
            // No participation found, proceed with saving new participation
            AdsJoinHistory adsJoinHistory = new AdsJoinHistory();
            adsJoinHistory.setUserId(event.getUserId());
            adsJoinHistory.setAdId(event.getAdId());
            adsJoinHistory.setAdName(event.getAdName());
            adsJoinHistory.setRewardAmount(event.getRewardAmount());
            adsJoinHistory.setJoinTime(LocalDateTime.now());

            adsJoinHistoryRepository.save(adsJoinHistory);

            // Logic to award the user reward points, etc.
            System.out.println("User " + event.getUserId() + " has successfully joined the ad.");
        } else {
            System.out.println("User " + event.getUserId() + " has already participated in this ad.");
        }
    }
}
