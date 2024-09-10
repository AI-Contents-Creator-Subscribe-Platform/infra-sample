package org.sheep1500.toyadvertisementbackend.ads_join.mq.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sheep1500.toyadvertisementbackend.ads.application.ReduceAdsJoinService;
import org.sheep1500.toyadvertisementbackend.ads.domain.AdsId;
import org.sheep1500.toyadvertisementbackend.ads_join.application.CreateAdsJoinService;
import org.sheep1500.toyadvertisementbackend.ads_join.domain.AdsJoinHistory;
import org.sheep1500.toyadvertisementbackend.ads_join.domain.AdsJoinHistoryRepository;
import org.sheep1500.toyadvertisementbackend.ads_join.mq.event.AdsJoinEvent;
import org.sheep1500.toyadvertisementbackend.facade.LockAdsFacade;
import org.sheep1500.toyadvertisementbackend.lock.LockData;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdsJoinConsumer {

    private final AdsJoinHistoryRepository adsJoinHistoryRepository;
    private final LockAdsFacade lockAdsFacade;
    private final ReduceAdsJoinService reduceAdsJoinService;
    private final CreateAdsJoinService createAdsJoinService;

    @RabbitListener(queues = "#{queueName}")
    public void handleAdParticipation(AdsJoinEvent event) {
        lockAdsFacade.executeWithLock(new LockData(event.getAdId(), 100L, 100L),
                () -> {
                    createAdsJoinService.validJoin(event.getUserId(), event.getAdId());

                    reduceAdsJoinService.reduceJoinCount(new AdsId(event.getAdId()));

                    AdsJoinHistory adsJoinHistory = AdsJoinHistory.builder()
                            .userId(event.getUserId())
                            .adId(event.getAdId())
                            .adName(event.getAdName())
                            .rewardAmount(event.getRewardAmount())
                            .joinDate(event.getJoinTime())
                            .build();

                    adsJoinHistoryRepository.save(adsJoinHistory);
                });
    }
}
