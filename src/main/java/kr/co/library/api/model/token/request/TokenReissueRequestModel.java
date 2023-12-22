package kr.co.library.api.model.token.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TokenReissueRequestModel {

    @NotBlank(message = "userId는 비어있을 수 없습니다.")
    private String userId;

    @NotBlank(message = "refreshToken은 비어있을 수 없습니다.")
    private String refreshToken;
}
