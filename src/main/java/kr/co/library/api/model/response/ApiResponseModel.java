package kr.co.library.api.model.response;

import lombok.Data;

import java.util.Date;

@Data
public class ApiResponseModel<T> {
    private Date timestamp;
    private String code;
    private String status;
    private String desc;
    private T body;


    public static <T> ApiResponseModel<T> createApiResponse(
            Date timestamp , String code , String status , String desc , T body){
        return new ApiResponseModel(timestamp , code , status , desc , body);
    }

    public ApiResponseModel(Date timestamp , String code , String status , String desc , T body){
        this.timestamp = timestamp;
        this.code = code;
        this.status = status;
        this.desc = desc;
        this.body = body;
    }
}
