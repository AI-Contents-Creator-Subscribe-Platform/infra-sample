package org.sheep1500.toyadvertisementbackend.ads.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sheep1500.toyadvertisementbackend.ads.domain.*;
import org.sheep1500.toyadvertisementbackend.ads.presentation.dto.AdsRequest;
import org.sheep1500.toyadvertisementbackend.common.api.response.RtCode;
import org.sheep1500.toyadvertisementbackend.common.domain.IdGenerator;
import org.sheep1500.toyadvertisementbackend.facade.CacheAdsFacade;
import org.sheep1500.toyadvertisementbackend.facade.LockAdsFacade;
import org.sheep1500.toyadvertisementbackend.fixture.*;
import org.sheep1500.toyadvertisementbackend.mock.BaseRestDocTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
class AdsControllerTest extends BaseRestDocTest {


    @MockBean
    private LockAdsFacade lockAdsFacade;

    @MockBean
    private CacheAdsFacade cacheAdsFacade;

    @DisplayName("/api/v1/ads POST 요청과 광고 등록 정보를 보내면 HTTP 200 코드와 함께 광고가 등록되어야 한다.")
    @Test
    void create() throws Exception {
        // given
        AdsRequest.Create request = new AdsRequest.Create("ad name", BigDecimal.ONE, 10, "ad text", "ad image url", LocalDateTime.now(), LocalDateTime.now().plusWeeks(1));
        AdsId adsId = AdsIdFixture.createAdsId(IdGenerator.simpleTimestampUuid());
        Ads ads = AdsFixture.createAds(
                adsId,
                RewardAmountsFixture.createRewardAmounts(request.reward()),
                JoinLimitFixture.createJoinLimit(request.joinCount()),
                AdsDisplayDateFixture.createAdsDisplayDate(request.startDate(), request.endDate()),
                new AdsContent(adsId, AdsInfoFixture.createAdsInfo(request.name(), request.text()), new AdsImage(request.imageUrl()))
        );

        given(lockAdsFacade.executeWithLock(any(), any(Supplier.class))).willReturn(ads);


        // expect
        this.mockMvc.perform(post("/api/v1/ads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(RtCode.RT_SUCCESS.name()))
                .andExpect(jsonPath("$.message").value((RtCode.RT_SUCCESS).getRtMessage()))
                .andExpect(jsonPath("$.data.id.id").value(ads.getId().getId()))
                .andExpect(jsonPath("$.data.reward.amounts").value(ads.getReward().getAmounts()))
                .andExpect(jsonPath("$.data.limit.limit").value(ads.getLimit().getLimit()))
                .andExpect(jsonPath("$.data.limit.currentLimit").value(ads.getLimit().getCurrentLimit()))
                .andExpect(jsonPath("$.data.content.info.name").value(ads.getContent().getInfo().getName()))
                .andExpect(jsonPath("$.data.content.info.text").value(ads.getContent().getInfo().getText()))
                .andExpect(jsonPath("$.data.content.image.url").value(ads.getContent().getImage().getUrl()))
                .andDo(document("createAds",
                        requestFields(
                                fieldWithPath("name").description("광고명"),
                                fieldWithPath("reward").description("광고 참여시 적립액수"),
                                fieldWithPath("joinCount").description("광고 참여 가능 횟수"),
                                fieldWithPath("text").description("광고 문구"),
                                fieldWithPath("imageUrl").description("광고 이미지 URL"),
                                fieldWithPath("startDate").description("광고 노출 시간: 시작일시"),
                                fieldWithPath("endDate").description("광고 노출 시간: 종료일시")
                        )
                        , responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("결과 메시지"),
                                fieldWithPath("data.id.id").description("생성된 광고 ID"),
                                fieldWithPath("data.reward.amounts").description("생성된 광고 참여시 적립액수"),
                                fieldWithPath("data.limit.limit").description("생성된 광고 참여 가능 횟수"),
                                fieldWithPath("data.limit.currentLimit").description("생성된 현재 광고 참여 가능 횟수"),
                                fieldWithPath("data.content.adsId.id").description("생성된 광고 ID"),
                                fieldWithPath("data.content.info.name").description("생성된 광고명"),
                                fieldWithPath("data.content.info.text").description("생성된 광고 문구"),
                                fieldWithPath("data.content.image.url").description("생성된 광고 이미지 URL"),
                                fieldWithPath("data.displayDate.startDate").description("생성된 광고 노출 시작일시"),
                                fieldWithPath("data.displayDate.endDate").description("생성된 광고 노출 종료일시")
                        )
                ));
    }

    @DisplayName("/api/v1/ads/currentDisplayAdsList GET 요청을 보내면 HTTP 200 코드와 함께 전시기간 광고가 조회된다.")
    @Test
    void currentDisplayAdsList() throws Exception {
        // given
        AdsId adsId = AdsIdFixture.createAdsId(IdGenerator.simpleTimestampUuid());
        Ads ads = AdsFixture.createAds(
                adsId,
                RewardAmountsFixture.createRewardAmounts(BigDecimal.ONE),
                JoinLimitFixture.createJoinLimit(10),
                AdsDisplayDateFixture.createAdsDisplayDate(LocalDateTime.now(), LocalDateTime.now().plusWeeks(1)),
                new AdsContent(adsId, AdsInfoFixture.createAdsInfo("ad name", "ad text"), new AdsImage("ad image url"))
        );
        int count = 10;
        List<AdsSummary> list = IntStream.rangeClosed(1, count)
                .mapToObj(i -> AdsSummary.builder()
                        .adsId(ads.getId().getId())
                        .adsName(ads.getContent().getInfo().getName() + i)
                        .adsDescription(ads.getContent().getInfo().getText() + i)
                        .imageUrl(ads.getContent().getImage().getUrl() + i)
                        .reward(ads.getReward().getAmounts().add(BigDecimal.valueOf(i)))
                        .build())
                .collect(Collectors.toList());

        given(cacheAdsFacade.cacheCurrentDisplayAds(any(), any(Supplier.class))).willReturn(list);


        // expect
        this.mockMvc.perform(get("/api/v1/ads/currentDisplayAdsList")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(RtCode.RT_SUCCESS.name()))
                .andExpect(jsonPath("$.message").value((RtCode.RT_SUCCESS).getRtMessage()))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(count)))
                .andDo(document("currentDisplayAdsList",
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("결과 메시지"),
                                fieldWithPath("data").description("전시조회 광고 List"),
                                fieldWithPath("data[].adsId").description("광고 ID"),
                                fieldWithPath("data[].adsName").description("광고명"),
                                fieldWithPath("data[].adsDescription").description("광고문구"),
                                fieldWithPath("data[].imageUrl").description("광고 이미지 URL"),
                                fieldWithPath("data[].reward").description("광고 적립 포인트")
                        )
                ));
    }
}