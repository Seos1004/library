package kr.co.library.api.component.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProducerComponent {

    private final KafkaTemplate<String , Object> kafkaTemplate;

    public ProducerComponent(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void create() {
        kafkaTemplate.send("topic_34952", "34952 TEST");
    }

}
