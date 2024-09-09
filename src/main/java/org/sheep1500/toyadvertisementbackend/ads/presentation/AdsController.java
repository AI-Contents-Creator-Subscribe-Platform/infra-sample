package org.sheep1500.toyadvertisementbackend.ads.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sheep1500.toyadvertisementbackend.ads.application.CreateAdsService;
import org.sheep1500.toyadvertisementbackend.ads.application.dto.AdsDto;
import org.sheep1500.toyadvertisementbackend.ads.domain.Ads;
import org.sheep1500.toyadvertisementbackend.ads.domain.QueryAdsService;
import org.sheep1500.toyadvertisementbackend.ads.presentation.dto.AdsRequest;
import org.sheep1500.toyadvertisementbackend.common.api.response.ApiResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ads")
@RequiredArgsConstructor
public class AdsController {

    private final CreateAdsService createAdsService;
    private final QueryAdsService queryAdsService;

    @PostMapping
    public ApiResponseDto<?> create(@RequestBody @Valid AdsRequest.Create request) {
        Ads ads = createAdsService.create(AdsDto.Create.builder()
                .amounts(request.getAmounts())
                .vat(request.getVat())
                .build());

        return ApiResponseDto.createOK(ads);
    }

    @GetMapping("/search")
    public ApiResponseDto<?> query() {
        List<Ads> list = queryAdsService.search(null);

        return ApiResponseDto.createOK(list);
    }
}
