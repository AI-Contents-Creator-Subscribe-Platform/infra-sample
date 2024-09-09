package org.sheep1500.toyadvertisementbackend.user_ads.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sheep1500.toyadvertisementbackend.common.api.response.ApiResponseDto;
import org.sheep1500.toyadvertisementbackend.user_ads.application.CreateAdsJoinService;
import org.sheep1500.toyadvertisementbackend.user_ads.application.dto.AdsJoinDto;
import org.sheep1500.toyadvertisementbackend.user_ads.domain.AdsJoinHistory;
import org.sheep1500.toyadvertisementbackend.user_ads.domain.QueryAdsJoinService;
import org.sheep1500.toyadvertisementbackend.user_ads.presentation.dto.AdsJoinRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AdJoinController {

    private final CreateAdsJoinService createAdsJoinService;
    private final QueryAdsJoinService queryAdsJoinService;

    @PostMapping("/joinAd")
    public ApiResponseDto<?> joinAd(@RequestBody @Valid AdsJoinRequest.Create request) {
        createAdsJoinService.create(AdsJoinDto.Create.builder()
                .amounts(request.getAmounts())
                .vat(request.getVat())
                .build());
        return ApiResponseDto.DEFAULT_OK;
    }

    @GetMapping("/listUserJoinAds/{userId}")
    public ApiResponseDto<?> listUserJoinAds(@PathVariable String userId) {
        List<AdsJoinHistory> list = queryAdsJoinService.list(userId);
        return ApiResponseDto.createOK(list);
    }
}
