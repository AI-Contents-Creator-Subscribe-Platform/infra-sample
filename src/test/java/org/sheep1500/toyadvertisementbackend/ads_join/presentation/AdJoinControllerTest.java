package org.sheep1500.toyadvertisementbackend.ads_join.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sheep1500.toyadvertisementbackend.ads.domain.*;
import org.sheep1500.toyadvertisementbackend.ads_join.application.RequestAdsJoinService;
import org.sheep1500.toyadvertisementbackend.ads_join.application.dto.AdsJoinDto;
import org.sheep1500.toyadvertisementbackend.ads_join.domain.AdsJoinHistory;
import org.sheep1500.toyadvertisementbackend.ads_join.domain.QueryAdsJoinService;
import org.sheep1500.toyadvertisementbackend.ads_join.presentation.dto.AdsJoinRequest;
import org.sheep1500.toyadvertisementbackend.common.api.response.RtCode;
import org.sheep1500.toyadvertisementbackend.common.domain.IdGenerator;
import org.sheep1500.toyadvertisementbackend.common.dto.PageResponse;
import org.sheep1500.toyadvertisementbackend.fixture.*;
import org.sheep1500.toyadvertisementbackend.mock.BaseRestDocTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


//@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
class AdJoinControllerTest extends BaseRestDocTest {

    @MockBean
    private RequestAdsJoinService requestAdsJoinService;

    @MockBean
    private QueryAdsJoinService queryAdsJoinService;

    @DisplayName("/api/v1/joinAd/request POST 요청과 광고 참여 요청 정보를 보내면 HTTP 200 코드와 함께 광고참여 요청을 한다.")
    @Test
    void requestAdsJoin() throws Exception {
        // given
        AdsJoinRequest.Request request = new AdsJoinRequest.Request("userId", "adId");

//        given(requestAdsJoinService.requestJoinAd(new AdsJoinDto.Create(request.userId(), request.adId()))).willReturn(null);

        // expect
        this.mockMvc.perform(post("/api/v1/joinAd/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(RtCode.RT_SUCCESS.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value((RtCode.RT_SUCCESS).getRtMessage()))
                .andDo(document("requestJoinAd",
                        requestFields(
                                fieldWithPath("userId").description("사용자 ID"),
                                fieldWithPath("adId").description("광고 ID")
                        )
                        , responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("결과 메시지"),
                                fieldWithPath("data").description("결과 값(null)")
                        )
                ));
    }

    @DisplayName("/api/v1/joinAd/{userId}/list GET 요청과 조회기간을 보내면 HTTP 200 코드와 함께 사용자 광고 참여 이력이 조회된다.")
    @Test
    void currentDisplayAdsList() throws Exception {
        // given
        LocalDateTime now = LocalDateTime.now();
        AdsJoinDto.JoinHistoryList request = new AdsJoinDto.JoinHistoryList("userId", now, now.plusWeeks(1));
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        int count = 50;
        List<AdsJoinHistory> list = IntStream.rangeClosed(1, count)
                .mapToObj(i -> AdsJoinHistory.builder()
                        .userId(request.userId())
                        .adId(String.valueOf(i))
                        .adName(String.valueOf(i))
                        .rewardAmount(BigDecimal.valueOf(i))
                        .joinDate(now.plusMinutes(i))
                        .build())
                .collect(Collectors.toList());

        given(queryAdsJoinService.listByUserId(any(AdsJoinDto.JoinHistoryList.class), any(Pageable.class))).willReturn(new PageResponse<>(new PageImpl<>(list, Pageable.ofSize(count), count)));

        // expect
        this.mockMvc.perform(get("/api/v1/joinAd/{userId}/list", request.userId())
                        .param("startDate", request.startDate().format(formatter))
                        .param("endDate", request.endDate().format(formatter))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(RtCode.RT_SUCCESS.name()))
                .andExpect(jsonPath("$.message").value((RtCode.RT_SUCCESS).getRtMessage()))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content", hasSize(count)))
                .andDo(document("listUserAdsJoin",
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("결과 메시지"),
                                fieldWithPath("data").description("사용자 광고 참여 이력 Page"),
                                fieldWithPath("data.pageNumber").description("Page 숫자"),
                                fieldWithPath("data.pageSize").description("Page 사이즈"),
                                fieldWithPath("data.numberOfElements").description("조회 객체 개수"),
                                fieldWithPath("data.totalPages").description("총 Page 수"),
                                fieldWithPath("data.totalElements").description("총 개수"),
                                fieldWithPath("data.last").description("마지막 Page 여부"),
                                fieldWithPath("data.first").description("첫 Page 여부"),
                                fieldWithPath("data.content[].userId").description("사용자 ID"),
                                fieldWithPath("data.content[].adId").description("참여 광고 ID"),
                                fieldWithPath("data.content[].adName").description("참여 광고명"),
                                fieldWithPath("data.content[].rewardAmount").description("광고 참여 적립 포인트"),
                                fieldWithPath("data.content[].joinDate").description("광고 참여 일시")
                        )
                ));
    }
}