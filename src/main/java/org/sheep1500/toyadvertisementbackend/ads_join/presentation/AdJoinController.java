package org.sheep1500.toyadvertisementbackend.ads_join.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sheep1500.toyadvertisementbackend.ads_join.application.CreateAdsJoinService;
import org.sheep1500.toyadvertisementbackend.ads_join.application.dto.AdsJoinDto;
import org.sheep1500.toyadvertisementbackend.ads_join.domain.QueryAdsJoinService;
import org.sheep1500.toyadvertisementbackend.ads_join.presentation.dto.AdsJoinRequest;
import org.sheep1500.toyadvertisementbackend.common.api.response.ApiResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/joinAd")
@RequiredArgsConstructor
public class AdJoinController {

    private final CreateAdsJoinService createAdsJoinService;
    private final QueryAdsJoinService queryAdsJoinService;

    @PostMapping
    public ApiResponseDto<?> requestJoinAd(@RequestBody @Valid AdsJoinRequest.Request request) {
        createAdsJoinService.requestJoinAd(new AdsJoinDto.Create(request.userId(), request.adId()));

        return ApiResponseDto.DEFAULT_OK;
    }

    @GetMapping("/{userId}/list")
    public ApiResponseDto<?> listUserAdsJoin(
            @PathVariable String userId,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @PageableDefault(size = 50, sort = "joinDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ApiResponseDto.createOK(queryAdsJoinService.listByUserId(userId, pageable));
    }
}
