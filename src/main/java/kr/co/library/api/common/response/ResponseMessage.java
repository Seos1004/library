package kr.co.library.api.common.response;

import jakarta.annotation.PostConstruct;
import kr.co.library.api.model.response.ResponseMessageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class ResponseMessage {
    private Map<String , Object> initExceptionInfo = null;

    @PostConstruct
    private void init(){
        YamlMapFactoryBean yaml = new YamlMapFactoryBean();
        yaml.setResources(new ClassPathResource("/message/api-response-message.yml"));
        initExceptionInfo = yaml.getObject();
    }

    public ResponseMessageModel getExceptionInfo(String ymlKey) {
        ResponseMessageModel result = new ResponseMessageModel();
        Map<String , Object> exceptionInfos = (Map<String, Object>) initExceptionInfo.get("exception");
        Map<String , Object> exceptionInfo = null;
        exceptionInfo = (Map<String, Object>) exceptionInfos.get(ymlKey);
        if(null != exceptionInfo){
            result.setCode(exceptionInfo.get("code").toString());
            result.setDesc(exceptionInfo.get("desc").toString());
            result.setStatus(exceptionInfo.get("status").toString());
        }else if(null == exceptionInfo){
            exceptionInfo = (Map<String, Object>) exceptionInfos.get("not_specified_message");
            result.setCode(exceptionInfo.get("code").toString());
            result.setDesc(exceptionInfo.get("desc").toString());
            result.setStatus(exceptionInfo.get("status").toString());
        }
        return result;
    }

   public ResponseMessageModel getSuccessInfo(String ymlKey) {
       ResponseMessageModel result = new ResponseMessageModel();
       Map<String , Object> successInfos = (Map<String, Object>) initExceptionInfo.get("success");
       Map<String , Object> successInfo = null;
       successInfo = (Map<String, Object>) successInfos.get(ymlKey);
       if(null != successInfo){
           result.setCode(successInfo.get("code").toString());
           result.setDesc(successInfo.get("desc").toString());
           result.setStatus(successInfo.get("status").toString());
       }else if(null == successInfo){
           successInfo = (Map<String, Object>) successInfos.get("ok");
           result.setCode(successInfo.get("code").toString());
           result.setDesc(successInfo.get("desc").toString());
           result.setStatus(successInfo.get("status").toString());
       }
       return result;
    }
}
