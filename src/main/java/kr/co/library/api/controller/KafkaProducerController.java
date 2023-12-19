package kr.co.library.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.library.api.common.response.api.ApiResponseHandler;
import kr.co.library.api.component.kafka.producer.ProducerComponent;
import kr.co.library.api.model.response.ApiResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/kafka/producer")
@Tag(name = "2. Kafka Producer" , description = "카푸카 프로듀서 컨트롤러")
public class KafkaProducerController {

    @Autowired
    private ApiResponseHandler apiResponseHandler;

    @Autowired
    private ProducerComponent producerComponent;


    @PostMapping("/topic/send")
    public ApiResponseModel<Void> topicSend(){
        producerComponent.create();
        return apiResponseHandler.apiResponse("");
    }


}


