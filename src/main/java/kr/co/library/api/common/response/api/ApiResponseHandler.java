package kr.co.library.api.common.response.api;

import kr.co.library.api.common.response.ResponseMessage;
import kr.co.library.api.common.util.DateUtils;
import kr.co.library.api.model.response.ApiResponseModel;
import kr.co.library.api.model.response.ResponseMessageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ApiResponseHandler {

    @Autowired
    private ResponseMessage responseMessage;

    @Autowired
    private DateUtils dateUtils;

    public ApiResponseModel apiResponse(String ymlKey){
        boolean isInvalid = false;
        if(null == ymlKey){
            isInvalid = true;
        }
        ResponseMessageModel successInfo = null;
        if (isInvalid) {
            successInfo = responseMessage.getSuccessInfo("ok");
        }else{
            successInfo = responseMessage.getSuccessInfo(ymlKey);
        }
        if(null == successInfo){
            successInfo = responseMessage.getSuccessInfo("ok");
        }
        return ApiResponseModel.createApiResponse(
                dateUtils.getInstantNowToDate(),
                successInfo.getCode(),
                successInfo.getStatus(),
                successInfo.getDesc(),
                null
        );
    }
    public <T> ApiResponseModel apiResponse(String ymlKey , T body){
        boolean isInvalid = false;
        if(null == ymlKey){
            isInvalid = true;
        }
        ResponseMessageModel successInfo = null;
        if (isInvalid) {
            successInfo = responseMessage.getSuccessInfo("ok");
        }else{
            successInfo = responseMessage.getSuccessInfo(ymlKey);
        }
        if(null == successInfo){
            successInfo = responseMessage.getSuccessInfo("ok");
        }
        return ApiResponseModel.createApiResponse(
                dateUtils.getInstantNowToDate(),
                successInfo.getCode(),
                successInfo.getStatus(),
                successInfo.getDesc(),
                body
        );
    }

}
