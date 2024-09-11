package org.sheep1500.toyadvertisementbackend.ads_join.mq.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sheep1500.toyadvertisementbackend.ads.application.ReduceAdsJoinService;
import org.sheep1500.toyadvertisementbackend.ads.domain.AdsId;
import org.sheep1500.toyadvertisementbackend.ads.event.JoinAdsEvent;
import org.sheep1500.toyadvertisementbackend.ads_join.application.RequestAdsJoinService;
import org.sheep1500.toyadvertisementbackend.ads_join.domain.AdsJoinHistory;
import org.sheep1500.toyadvertisementbackend.ads_join.domain.AdsJoinHistoryRepository;
import org.sheep1500.toyadvertisementbackend.ads_join.external.AdsJoinRequestProxy;
import org.sheep1500.toyadvertisementbackend.ads_join.external.dto.RewardPointDto;
import org.sheep1500.toyadvertisementbackend.ads_join.mq.event.AdsJoinEvent;
import org.sheep1500.toyadvertisementbackend.facade.LockAdsFacade;
import org.sheep1500.toyadvertisementbackend.lock.LockData;
import org.sheep1500.toyadvertisementbackend.mq.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdsJoinConsumer {

    private final AdsJoinHistoryRepository adsJoinHistoryRepository;
    private final LockAdsFacade lockAdsFacade;
    private final ReduceAdsJoinService reduceAdsJoinService;
    private final RequestAdsJoinService requestAdsJoinService;
    private final TransactionTemplate transactionTemplate;
    private final ApplicationEventPublisher eventPublisher;
    private final AdsJoinRequestProxy joinRequestProxy;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleJoinAds(AdsJoinEvent event) {
        log.info("consuming event: queueName[{}], event[{}]", RabbitMQConfig.QUEUE_NAME, event);
        try {
            lockAdsFacade.executeWithLock(new LockData(event.getAdId(), 3L, 3L),
                    () -> {
                        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                            @Override
                            protected void doInTransactionWithoutResult(TransactionStatus status) {
                                try {
                                    // 트랜잭션 내에서 실행할 메소드
                                    requestAdsJoinService.validJoin(event.getUserId(), event.getAdId());

                                    reduceAdsJoinService.reduceJoinCount(new AdsId(event.getAdId()));

                                    AdsJoinHistory adsJoinHistory = AdsJoinHistory.builder()
                                            .userId(event.getUserId())
                                            .adId(event.getAdId())
                                            .adName(event.getAdName())
                                            .rewardAmount(event.getRewardAmount())
                                            .joinDate(event.getJoinTime())
                                            .build();

                                    adsJoinHistoryRepository.save(adsJoinHistory);

                                    joinRequestProxy.rewardPoint(new RewardPointDto(event.getUserId(), event.getRewardAmount()));

                                    eventPublisher.publishEvent(new JoinAdsEvent(new AdsId(event.getAdId())));
                                } catch (Exception e) {
                                    // 트랜잭션 롤백
                                    status.setRollbackOnly();
                                    throw e;
                                }
                            }
                        });

                    });
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
