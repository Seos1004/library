package kr.co.library.api.model.sign.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignInRequestModel {
    @NotBlank(message = "userId는 비어있을 수 없습니다.")
    private String userId;
    @NotBlank(message = "password는 비어있을 수 없습니다.")
    private String password;
}
