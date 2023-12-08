package kr.co.library.api.model.response;

import lombok.Data;

@Data
public class ResponseMessageModel {
   private String code;
   private String desc;
   private String status;
}
