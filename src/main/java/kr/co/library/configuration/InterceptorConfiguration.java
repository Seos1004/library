package kr.co.library.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.library.api.common.response.exception.custom.ServiceFail;
import kr.co.library.api.common.util.JWTUtils;
import kr.co.library.api.common.util.RedisUtils;
import kr.co.library.api.constant.JWTConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private JWTUtils jwtUtils;

    @Bean
    public InterceptorHandler interceptorhandler(){
        return new InterceptorHandler();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptorhandler()).addPathPatterns("/**");
    }

    class InterceptorHandler implements HandlerInterceptor {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            boolean result = true;
            boolean isPathCheck = pathCheck(request);
            String tokenParsingResult = "";
            String token = "";
            if(isPathCheck){
                log.info("[InterceptorConfiguration.InterceptorHandler.preHandle] pass url {}" , request.getRequestURL());
                return result;
            }else{
                try {
                    token = jwtUtils.tokenPrefixParser(request.getHeader(JWTConstant.REQUEST_TOKEN_HEADER_KEY));
                }catch (Exception e){
                    log.error("[InterceptorConfiguration.InterceptorHandler.preHandle] HEADER 파싱 실패");
                    throw new ServiceFail("not_specified_message" , e);
                }
                if(null == token || token.isBlank()){
                    log.info("[InterceptorConfiguration.InterceptorHandler.preHandle] 토큰이 헤더에 존재하지 않음");
                    throw new ServiceFail("not_specified_message");
                }
                tokenParsingResult = jwtUtils.tokenValidation(token);
                log.info("[InterceptorConfiguration.InterceptorHandler.preHandle] tokenParsingResult ? : {}" , tokenParsingResult);
                switch (tokenParsingResult){
                    case JWTConstant.TOKEN_VALIDATION_SUCCESS:
                        //정상 토큰에서 사용자 ID를 추출해서 DB에 존재하는 사용자인지 확인
                        //REDIS에 토큰이 존재하는지 홗인
                        break;
                    case JWTConstant.TOKEN_VALIDATION_EXPIRATION:
                        log.error("[InterceptorConfiguration.InterceptorHandler.preHandle] 만료된 토큰입니다.");
                        throw new ServiceFail("not_specified_message");
                    case JWTConstant.TOKEN_VALIDATION_FAKE:
                        log.error("[InterceptorConfiguration.InterceptorHandler.preHandle] 위/변조한 토큰입니다.");
                        throw new ServiceFail("not_specified_message");
                    case JWTConstant.TOKEN_VALIDATION_FAIL:
                        log.error("[InterceptorConfiguration.InterceptorHandler.preHandle] 토큰 파싱에 실패하였습니다.");
                        throw new ServiceFail("not_specified_message");
                    default:
                        log.error("[InterceptorConfiguration.InterceptorHandler.preHandle] 규격 외 결과");
                        throw new ServiceFail("not_specified_message");
                }
            }
            return result;
        }
        private boolean pathCheck(HttpServletRequest request){
            boolean result = false;
            String uri = request.getRequestURI();

            List<String> containsWhitelist = new ArrayList<>(
                    Arrays.asList(
                              "swagger-ui"
                            , "api-docs"
                            , "h2-console"
                    )
            );
            List<String> startsWithWhitelist = new ArrayList<>(
                    Arrays.asList(
                              "/api/sign-in"
                            , "/api/sign-up"
                            , "/api/sample"
                    )
            );

            for(String path : containsWhitelist){
                if(uri.contains(path)){
                    return true;
                }
            }
            for(String path : startsWithWhitelist){
                if(uri.startsWith(path)){
                    return true;
                }
            }
            return result;
        }
    }
}
