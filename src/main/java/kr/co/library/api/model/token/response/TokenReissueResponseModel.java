package kr.co.library.api.model.token.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class TokenReissueResponseModel {
    private String userId;
    private String accessToken;
    private String refreshToken;

    @JsonIgnore
    private String ymlKey;
}
