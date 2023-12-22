package kr.co.library.api.model.rest.naver.papago.translation;

import lombok.Data;

@Data
public class TextTranslationMessageResultResponseModel {
    private String srcLangType;
    private String tarLangType;
    private String translatedText;
    private String engineType;
}
