package org.sheep1500.toyadvertisementbackend.ads.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sheep1500.toyadvertisementbackend.ads.application.CreateAdsService;
import org.sheep1500.toyadvertisementbackend.ads.application.dto.AdsDto;
import org.sheep1500.toyadvertisementbackend.ads.domain.Ads;
import org.sheep1500.toyadvertisementbackend.ads.domain.QueryAdsService;
import org.sheep1500.toyadvertisementbackend.ads.presentation.dto.AdsRequest;
import org.sheep1500.toyadvertisementbackend.common.api.response.ApiResponseDto;
import org.sheep1500.toyadvertisementbackend.facade.CacheAdsFacade;
import org.sheep1500.toyadvertisementbackend.facade.LockAdsFacade;
import org.sheep1500.toyadvertisementbackend.lock.LockData;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ads")
@RequiredArgsConstructor
public class AdsController {

    private final CreateAdsService createAdsService;
    private final QueryAdsService queryAdsService;

    private final LockAdsFacade lockAdsFacade;
    private final CacheAdsFacade cacheAdsFacade;

    @PostMapping
    public ApiResponseDto<?> create(@RequestBody @Valid AdsRequest.Create request) {
        Ads ads = lockAdsFacade.locking(
                new LockData(request.name()),
                () -> createAdsService.create(AdsDto.Create.builder()
                        .name(request.name())
                        .reward(request.reward())
                        .joinCount(request.joinCount())
                        .text(request.text())
                        .imageUrl(request.imageUrl())
                        .startDate(request.startDate())
                        .endDate(request.endDate())
                        .build()));

        return ApiResponseDto.createOK(ads);
    }

    @GetMapping("/currentDisplayAdsList")
    public ApiResponseDto<?> currentDisplayAdsList() {
        List<Ads> list = cacheAdsFacade.cacheCurrentDisplayAds(
                QueryAdsService.currentDateString(),
                queryAdsService::currentDisplayAdsList
        );
        return ApiResponseDto.createOK(list);
    }
}