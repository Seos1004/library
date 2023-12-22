package kr.co.library.api.service.translation;

import kr.co.library.api.common.response.exception.custom.ServiceFail;
import kr.co.library.api.component.kafka.producer.ProducerComponent;
import kr.co.library.api.constant.response.YmlKey001SignConstant;
import kr.co.library.api.model.sign.request.SignOutRequestModel;
import kr.co.library.api.model.translation.TranslationMessageKafKaRequestModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TranslationService {

    @Autowired
    private ProducerComponent producerComponent;
    public void kafkaRequest(TranslationMessageKafKaRequestModel translationMessageKafKaRequestModel) {
        log.info("[TranslationService.kafkaRequest] START {}" , translationMessageKafKaRequestModel.getMessage());
        producerComponent.create("translation" ,translationMessageKafKaRequestModel.getMessage());
        log.info("[TranslationService.kafkaRequest] END");
    }


}
