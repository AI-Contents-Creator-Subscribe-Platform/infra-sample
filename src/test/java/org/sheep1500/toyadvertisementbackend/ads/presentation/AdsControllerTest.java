package org.sheep1500.toyadvertisementbackend.ads.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// TODO: 테스트 코드 작성 필요
@WebMvcTest(AdsController.class)
//@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
class AdsControllerTest {
    //    @Test
//    @DisplayName("/contracts POST 요청과 계약 정보를 보내면 HTTP 201 코드와 함께 상품이 등록되어야 한다.")
//    void create() throws Exception {
//        // given
//        ContractsRequest request = new ContractsRequest("글렌피딕", 230_000, false);
//
//        given(productService.createProduct(anyString(), anyInt(), anyString()))
//                .willReturn(new ProductDto(1L, "글렌피딕", 230_000, "https://image.com/image.png"));
//
//        // expect
//        mockMvc.perform(post("/contracts")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andExpect(header().string("location", "/contracts/1"))
//                .andExpect(jsonPath("$.code").value("200"))
//                .andExpect(jsonPath("$.message").value("상품이 생성되었습니다."))
//                .andExpect(jsonPath("$.result.productId").value(1))
//                .andExpect(jsonPath("$.result.name").value("글렌피딕"))
//                .andExpect(jsonPath("$.result.price").value(230000))
//                .andExpect(jsonPath("$.result.imageUrl").value("https://image.com/image.png"))
//                .andDo(document("create-contract",
//                        requestFields(
//                                fieldWithPath("name").description("상품의 이름"),
//                                fieldWithPath("age").description("상품의 가격"),
//                                fieldWithPath("del").description("상품의 이미지 URL")
//                        )
//                        , responseFields(
//                                fieldWithPath("code").description("상태 코드"),
//                                fieldWithPath("message").description("결과 메시지"),
//                                fieldWithPath("result.productId").description("생성된 상품의 ID"),
//                                fieldWithPath("result.name").description("생성된 상품의 이름"),
//                                fieldWithPath("result.price").description("생성된 상품의 가격"),
//                                fieldWithPath("result.imageUrl").description("생성된 상품의 이미지 URL")
//                        )
//                ));
//    }
//
//    @Test
//    @DisplayName("/contracts POST 요청과 계약 정보를 보내면 HTTP 201 코드와 함께 상품이 등록되어야 한다.")
//    void currentDisplayAdsList() throws Exception {
//        // given
//        ContractsRequest request = new ContractsRequest("글렌피딕", 230_000, false);
//
//        given(productService.createProduct(anyString(), anyInt(), anyString()))
//                .willReturn(new ProductDto(1L, "글렌피딕", 230_000, "https://image.com/image.png"));
//
//        // expect
//        mockMvc.perform(post("/contracts")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andExpect(header().string("location", "/contracts/1"))
//                .andExpect(jsonPath("$.code").value("200"))
//                .andExpect(jsonPath("$.message").value("상품이 생성되었습니다."))
//                .andExpect(jsonPath("$.result.productId").value(1))
//                .andExpect(jsonPath("$.result.name").value("글렌피딕"))
//                .andExpect(jsonPath("$.result.price").value(230000))
//                .andExpect(jsonPath("$.result.imageUrl").value("https://image.com/image.png"))
//                .andDo(document("create-contract",
//                        requestFields(
//                                fieldWithPath("name").description("상품의 이름"),
//                                fieldWithPath("age").description("상품의 가격"),
//                                fieldWithPath("del").description("상품의 이미지 URL")
//                        )
//                        , responseFields(
//                                fieldWithPath("code").description("상태 코드"),
//                                fieldWithPath("message").description("결과 메시지"),
//                                fieldWithPath("result.productId").description("생성된 상품의 ID"),
//                                fieldWithPath("result.name").description("생성된 상품의 이름"),
//                                fieldWithPath("result.price").description("생성된 상품의 가격"),
//                                fieldWithPath("result.imageUrl").description("생성된 상품의 이미지 URL")
//                        )
//                ));
//    }
}