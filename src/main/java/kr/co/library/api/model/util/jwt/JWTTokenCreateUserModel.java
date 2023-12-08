package kr.co.library.api.model.util.jwt;

import lombok.Data;

@Data
public class JWTTokenCreateUserModel {
    private Long userSeq;
    private String userName;
    private String userId;
}
