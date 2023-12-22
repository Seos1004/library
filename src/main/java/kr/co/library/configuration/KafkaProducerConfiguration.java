package kr.co.library.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfiguration {

    @Value("${spring.kafka.host}")
    private String kafkaHost;

    @Value("${spring.kafka.port}")
    private int kafkaPort;

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        //BOOTSTRAP_SERVERS_CONFIG: Producer가 처음으로 연결할 Kafka 브로커의 위치를 설정
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, String.format("%s:%s" , kafkaHost , kafkaPort));
        //KEY_SERIALIZER_CLASS_CONFIG & VALUE_SERIALIZER_CLASS_CONFIG: Producer가 Key와 Value
        //값의 데이터를 Kafka 브로커로 전송하기 전에 데이터를 byte array로 변환하는 데 사용하는 직렬화 메커니즘을 설정
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
