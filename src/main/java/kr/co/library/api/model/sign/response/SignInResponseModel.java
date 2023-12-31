package kr.co.library.api.model.sign.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class SignInResponseModel {
    private String accessToken;
    private String refreshToken;
    @JsonIgnore
    private String ymlKey;
}
