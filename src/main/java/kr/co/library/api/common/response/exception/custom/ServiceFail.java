package kr.co.library.api.common.response.exception.custom;

import lombok.Data;

@Data
public class ServiceFail extends RuntimeException{
    private String ymlKey;
    private Exception exception;

    public ServiceFail(String ymlKey , Exception exception){
        this.ymlKey = ymlKey;
        this.exception = exception;
    }

    public ServiceFail(String ymlKey){
        this.ymlKey = ymlKey;
    }
}
