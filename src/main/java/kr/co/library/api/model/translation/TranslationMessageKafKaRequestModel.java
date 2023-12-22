package kr.co.library.api.model.translation;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TranslationMessageKafKaRequestModel {
    @NotBlank(message = "message는 비어있을 수 없습니다.")
    private String message;
}
