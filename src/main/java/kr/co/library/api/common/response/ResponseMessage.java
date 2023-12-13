package kr.co.library.api.common.response;

import jakarta.annotation.PostConstruct;
import kr.co.library.api.constant.ResponseMessageConstant;
import kr.co.library.api.model.properties.message.MessageProperty;
import kr.co.library.api.model.response.ResponseMessageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ResponseMessage {

    @Autowired
    private MessageProperty messageProperty;
    private Map<String , Object> initMessageInfo = null;

    @PostConstruct
    private void init(){
        if(messageProperty.getFilePathList().size() > 0){
            YamlMapFactoryBean yaml = new YamlMapFactoryBean();
            yaml.setResources(new ClassPathResource(messageProperty.getBaseFilePath()));
            initMessageInfo = yaml.getObject();
            Map<String,Object> successSumMap = new HashMap<>();
            Map<String,Object> exceptionSumMap = new HashMap<>();
            for(String filePath : messageProperty.getFilePathList()){
                yaml = new YamlMapFactoryBean();
                yaml.setResources(new ClassPathResource(filePath));
                successSumMap.putAll((Map<String, Object>) yaml.getObject().get(ResponseMessageConstant.MESSAGE_TYPE_SUCCESS));
                exceptionSumMap.putAll((Map<String, Object>) yaml.getObject().get(ResponseMessageConstant.MESSAGE_TYPE_EXCEPTION));
            }
            initMessageInfo.put(ResponseMessageConstant.MESSAGE_TYPE_SUCCESS , successSumMap);
            initMessageInfo.put(ResponseMessageConstant.MESSAGE_TYPE_EXCEPTION , exceptionSumMap);
        }
    }

    public ResponseMessageModel getExceptionInfo(String ymlKey) {
        ResponseMessageModel result = new ResponseMessageModel();
        Map<String , Object> exceptionInfos = (Map<String, Object>) initMessageInfo.get(ResponseMessageConstant.MESSAGE_TYPE_EXCEPTION);
        Map<String , Object> exceptionInfo = null;
        exceptionInfo = (Map<String, Object>) exceptionInfos.get(ymlKey);
        if(null != exceptionInfo){
            result.setCode(exceptionInfo.get(ResponseMessageConstant.MESSAGE_KEY_CODE).toString());
            result.setDesc(exceptionInfo.get(ResponseMessageConstant.MESSAGE_KEY_DESC).toString());
            result.setStatus(exceptionInfo.get(ResponseMessageConstant.MESSAGE_KEY_STATUS).toString());
        }else if(null == exceptionInfo){
            exceptionInfo = (Map<String, Object>) exceptionInfos.get(ResponseMessageConstant.MESSAGE_EXCEPTION_DEFAULT_KEY);
            result.setCode(exceptionInfo.get(ResponseMessageConstant.MESSAGE_KEY_CODE).toString());
            result.setDesc(exceptionInfo.get(ResponseMessageConstant.MESSAGE_KEY_DESC).toString());
            result.setStatus(exceptionInfo.get(ResponseMessageConstant.MESSAGE_KEY_STATUS).toString());
        }
        return result;
    }

   public ResponseMessageModel getSuccessInfo(String ymlKey) {
       ResponseMessageModel result = new ResponseMessageModel();
       Map<String , Object> successInfos = (Map<String, Object>) initMessageInfo.get(ResponseMessageConstant.MESSAGE_TYPE_SUCCESS);
       Map<String , Object> successInfo = null;
       successInfo = (Map<String, Object>) successInfos.get(ymlKey);
       if(null != successInfo){
           result.setCode(successInfo.get(ResponseMessageConstant.MESSAGE_KEY_CODE).toString());
           result.setDesc(successInfo.get(ResponseMessageConstant.MESSAGE_KEY_DESC).toString());
           result.setStatus(successInfo.get(ResponseMessageConstant.MESSAGE_KEY_STATUS).toString());
       }else if(null == successInfo){
           successInfo = (Map<String, Object>) successInfos.get(ResponseMessageConstant.MESSAGE_SUCCESS_DEFAULT_KEY);
           result.setCode(successInfo.get(ResponseMessageConstant.MESSAGE_KEY_CODE).toString());
           result.setDesc(successInfo.get(ResponseMessageConstant.MESSAGE_KEY_DESC).toString());
           result.setStatus(successInfo.get(ResponseMessageConstant.MESSAGE_KEY_STATUS).toString());
       }
       return result;
    }
}
