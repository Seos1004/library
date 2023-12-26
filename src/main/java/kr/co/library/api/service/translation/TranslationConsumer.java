package kr.co.library.api.service.translation;

import kr.co.library.api.common.util.rest.RestUtils;
import kr.co.library.api.model.rest.naver.papago.translation.TextTranslationResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Service
public class TranslationConsumer {

    @Autowired
    @Qualifier("naverRest")
    private RestUtils naverRest;

    @KafkaListener(topics = "translation", groupId = "japanese")
    public void translationToJapanese(ConsumerRecord data , Acknowledgment acknowledgment) {
        log.info("[TranslationService.translationToJapanese] START");
        try {
            TextTranslationResponseModel response = sendPapago("ja" , data.value().toString());
            log.info("[TranslationService.translationToJapanese] result ? : {}" , response.getMessage().getResult().getTranslatedText());
            acknowledgment.acknowledge();
        }catch (Exception e){
            log.error("[TranslationService.translationToJapanese] FAIL {}" , e);

        }
        log.info("[TranslationService.translationToJapanese] END");
    }

    @KafkaListener(topics = "translation", groupId = "english")
    public void translationToEnglish(ConsumerRecord data , Acknowledgment acknowledgment) {
        log.info("[TranslationService.translationToEnglish] START");
        try{
            TextTranslationResponseModel response = sendPapago("en" , data.value().toString());
            log.info("[TranslationService.translationToEnglish] result ? : {}" , response.getMessage().getResult().getTranslatedText());
            acknowledgment.acknowledge();
        }catch (Exception e){
            log.error("[TranslationService.translationToEnglish] FAIL {}" , e);
        }

        log.info("[TranslationService.translationToEnglish] END");
    }


    private TextTranslationResponseModel sendPapago(String translationTargetCountry , String translationTargetMessage){
        HttpHeaders httpheaders = new HttpHeaders();
        httpheaders.set("Content-Type" , "application/x-www-form-urlencoded; charset=UTF-8");
        URI url = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/papago/n2mt")
                .encode()
                .queryParam("source", "ko" )
                .queryParam("target",translationTargetCountry)
                .queryParam("text",translationTargetMessage)
                .build()
                //.expand(100, "steve") // {userId}, {userName}에 들어갈 값을 순차적으로 입력
                .toUri();
        TextTranslationResponseModel response = naverRest.post(url , httpheaders , null , TextTranslationResponseModel.class);
        return response;
    }

}
