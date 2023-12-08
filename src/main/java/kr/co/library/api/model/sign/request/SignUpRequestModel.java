package kr.co.library.api.model.sign.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignUpRequestModel {

    @Max(value = 255 , message = "userId는 255자를 초과할 수 없습니다.")
    @NotBlank(message = "userId는 비어있을 수 없습니다.")
    private String userId;

    @Max(value = 255 , message = "userName는 255자를 초과할 수 없습니다.")
    @NotBlank(message = "userName는 비어있을 수 없습니다.")
    private String userName;

    @Max(value = 255 , message = "password는 255자를 초과할 수 없습니다.")
    @NotBlank(message = "password는 비어있을 수 없습니다.")
    private String password;

}
