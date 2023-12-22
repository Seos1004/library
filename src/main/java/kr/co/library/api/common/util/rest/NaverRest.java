package kr.co.library.api.common.util.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Slf4j
public class NaverRest implements RestUtils {

    @Override
    @Deprecated
    public void get() {

    }


    @Override
    public <T, R> R post(URI url , HttpHeaders httpHeaders , T body, Class<R> responseType) {
        log.info("[NaverRest.post] START");
        RestTemplate restTemplate = new RestTemplate();
        if(null == url){
            log.info("[NaverRest.post] no url");
            return null;
        }
        if(null == responseType){
            log.info("[NaverRest.post] no res type");
            return null;
        }
        if(null != httpHeaders){
            httpHeaders.set("X-Naver-Client-Id", "wBnXOlDxPUAXVXImARmx");
            httpHeaders.set("X-Naver-Client-Secret", "H8hkDwzPus");
        }else{
            HttpHeaders initHttpHeaders = new HttpHeaders();
            httpHeaders.set("X-Naver-Client-Id", "wBnXOlDxPUAXVXImARmx");
            httpHeaders.set("X-Naver-Client-Secret", "H8hkDwzPus");
            httpHeaders = new HttpHeaders(initHttpHeaders);
        }
        // 요청 본문 설정
        HttpEntity<T> request = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<R> response = restTemplate.exchange(url, HttpMethod.POST, request, responseType);
        log.info("[NaverRest.post] END");
        return response.getBody();
    }

    @Override
    @Deprecated
    public void patch() {

    }

    @Override
    @Deprecated
    public void delete() {

    }
}
