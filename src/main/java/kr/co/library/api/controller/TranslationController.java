package kr.co.library.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.library.api.common.response.api.ApiResponseHandler;
import kr.co.library.api.component.kafka.producer.ProducerComponent;
import kr.co.library.api.model.response.ApiResponseModel;
import kr.co.library.api.model.sign.request.SignUpRequestModel;
import kr.co.library.api.model.translation.TranslationMessageKafKaRequestModel;
import kr.co.library.api.service.translation.TranslationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/translation")
@Tag(name = "2. 번역" , description = "카프카 활용 네이버 파파고 동시번역")
public class TranslationController {

    @Autowired
    private ApiResponseHandler apiResponseHandler;

    @Autowired
    private TranslationService translationService;


    @PostMapping("/kafka/request")
    public ApiResponseModel<Void> kafkaRequest(@Valid @RequestBody TranslationMessageKafKaRequestModel translationMessageKafKaRequestModel){
        translationService.kafkaRequest(translationMessageKafKaRequestModel);
        return apiResponseHandler.apiResponse("");
    }


}


