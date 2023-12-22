package kr.co.library.api.common.util.rest;

import kr.co.library.api.model.rest.naver.papago.translation.TextTranslationResponseModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@SpringBootTest(properties = "spring.profiles.active=local")
class NaverRestTest {

    @Autowired
    @Qualifier("naverRest")
    private RestUtils naverRest;

    @Test
    void post() {
        //Papago 번역으로 한 번에 번역할 수 있는 분량은 최대 5,000자이며, 하루 번역 처리 한도는 10,000자입니다.
        HttpHeaders httpheaders = new HttpHeaders();
        httpheaders.set("Content-Type" , "application/x-www-form-urlencoded; charset=UTF-8");
        URI url = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/papago/n2mt")
                .encode()
                .queryParam("source", "ko" )
                .queryParam("target","ja")
                .queryParam("text","오늘은 좋은날이에요")
                .build()
                //.expand(100, "steve") // {userId}, {userName}에 들어갈 값을 순차적으로 입력
                .toUri();
        TextTranslationResponseModel response = naverRest.post(url , httpheaders , null , TextTranslationResponseModel.class);
        System.out.println("response ? : " + response.toString());
    }
}
