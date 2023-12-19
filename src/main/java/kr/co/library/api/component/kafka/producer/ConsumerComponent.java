package kr.co.library.api.component.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class ConsumerComponent {

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "topic_34952", groupId = "34952_group")
    public void groupListener(ConsumerRecord data) {
        log.info("[ConsumerComponent.listener] ? : {}" , data.key());
        log.info("data ? : {}" , data.key());
        data.value();
        data.topic();
        data.timestamp()

    }

}
