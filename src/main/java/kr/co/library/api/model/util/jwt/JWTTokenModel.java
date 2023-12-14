package kr.co.library.api.model.util.jwt;

import lombok.Data;

@Data
public class JWTTokenModel {
    private String accessToken;
    private String refreshToken;
}
