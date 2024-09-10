package org.sheep1500.toyadvertisementbackend.ads_join.mq.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sheep1500.toyadvertisementbackend.ads.application.ReduceAdsJoinService;
import org.sheep1500.toyadvertisementbackend.ads.domain.AdsId;
import org.sheep1500.toyadvertisementbackend.ads_join.domain.AdsJoinHistory;
import org.sheep1500.toyadvertisementbackend.ads_join.domain.AdsJoinHistoryRepository;
import org.sheep1500.toyadvertisementbackend.ads_join.mq.event.AdsJoinEvent;
import org.sheep1500.toyadvertisementbackend.facade.LockAdsFacade;
import org.sheep1500.toyadvertisementbackend.lock.LockData;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdsJoinConsumer {

    private final AdsJoinHistoryRepository adsJoinHistoryRepository;
    private final LockAdsFacade lockAdsFacade;
    private final ReduceAdsJoinService reduceAdsJoinService;

    @RabbitListener(queues = "#{queueName}")
    public void handleAdParticipation(AdsJoinEvent event) {
        boolean joined = lockAdsFacade.locking(new LockData(event.getAdId(), 100L, 100L),
                () -> {
                    Optional<AdsJoinHistory> existingParticipation =
                            adsJoinHistoryRepository.findByUserIdAndAdId(event.getUserId(), event.getAdId());
                    if (existingParticipation.isPresent()) {
                        return false;
                    }
                    try {
                        reduceAdsJoinService.reduceJoinCount(new AdsId(event.getAdId()));
                    } catch (RuntimeException e) {
                        return false;
                    }

                    AdsJoinHistory adsJoinHistory = AdsJoinHistory.builder()
                            .userId(event.getUserId())
                            .adId(event.getAdId())
                            .adName(event.getAdName())
                            .rewardAmount(event.getRewardAmount())
                            .build();
                    adsJoinHistory.setUserId(event.getUserId());
                    adsJoinHistory.setAdId(event.getAdId());
                    adsJoinHistory.setAdName(event.getAdName());
                    adsJoinHistory.setRewardAmount(event.getRewardAmount());
                    adsJoinHistory.setJoinDate(LocalDateTime.now());

                    adsJoinHistoryRepository.save(adsJoinHistory);

                    return true;
                });

        if (joined) {
            log.info("#####");
        } else {
            log.info("@@@@@@@@@");
        }
    }
}
