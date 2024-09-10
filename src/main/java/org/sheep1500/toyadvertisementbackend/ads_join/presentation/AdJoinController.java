package org.sheep1500.toyadvertisementbackend.ads_join.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sheep1500.toyadvertisementbackend.ads_join.application.CreateAdsJoinService;
import org.sheep1500.toyadvertisementbackend.ads_join.application.dto.AdsJoinDto;
import org.sheep1500.toyadvertisementbackend.ads_join.domain.AdsJoinHistory;
import org.sheep1500.toyadvertisementbackend.ads_join.domain.QueryAdsJoinService;
import org.sheep1500.toyadvertisementbackend.ads_join.presentation.dto.AdsJoinRequest;
import org.sheep1500.toyadvertisementbackend.common.api.response.ApiResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/joinAd")
@RequiredArgsConstructor
public class AdJoinController {

    private final CreateAdsJoinService createAdsJoinService;
    private final QueryAdsJoinService queryAdsJoinService;

    @PostMapping
    public ApiResponseDto<?> joinAd(@RequestBody @Valid AdsJoinRequest.Create request) {
        createAdsJoinService.create(new AdsJoinDto.Create(request.userId(), request.adId()));

        // 2. join 됐는지 확인, 실제로는 polling 등 재확인 요청으로 로직 분리
//        Optional<AdsJoinHistory> adsJoinHistory = queryAdsJoinService.getByUserIdAndAdId(request.getUserId(), request.getAdId());
//        if (adsJoinHistory.isEmpty()) {
//            ApiResponseDto.createException(null, null);
//        }
        return ApiResponseDto.DEFAULT_OK;
    }

    @GetMapping("/{userId}/list")
    public ApiResponseDto<?> listUserAdsJoin(
            @PathVariable String userId,
            @PageableDefault(size = 50, sort = "joinDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ApiResponseDto.createOK(queryAdsJoinService.listByUserId(userId, pageable));
    }
}
