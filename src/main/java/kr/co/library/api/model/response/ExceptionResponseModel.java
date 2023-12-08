package kr.co.library.api.model.response;

import lombok.Data;

import java.util.Date;

@Data
public class ExceptionResponseModel<T> {
    private Date timestamp;
    private String code;
    private String status;
    private String desc;
    private String path;

    public static <T> ExceptionResponseModel<T> createExceptionResponse(
            Date timestamp ,
            String code,
            String status,
            String desc,
            String path
    ){
        return new ExceptionResponseModel<>(timestamp , code , status , desc , path);
    }

    public ExceptionResponseModel(
            Date timestamp ,
            String code,
            String status,
            String desc,
            String path
    ){
        this.timestamp = timestamp;
        this.code = code;
        this.status = status;
        this.desc = desc;
        this.path = path;
    }
}
