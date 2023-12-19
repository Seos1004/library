package kr.co.library.api.model.kafka;

import lombok.Data;

@Data
public class KafkaConsumerModel {

    private String topic;

    private String partition;

    private String leaderEpoch;

    private String offset;

}
