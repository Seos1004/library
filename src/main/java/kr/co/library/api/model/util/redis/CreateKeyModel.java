package kr.co.library.api.model.util.redis;

import lombok.Data;

@Data
public class CreateKeyModel {
    private String key;
    private String value;
}
