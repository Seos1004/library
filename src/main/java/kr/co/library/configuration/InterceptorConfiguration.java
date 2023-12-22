package kr.co.library.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.library.api.common.response.exception.custom.ServiceFail;
import kr.co.library.api.common.util.JWTUtils;
import kr.co.library.api.common.util.RedisUtils;
import kr.co.library.api.component.user.UserComponent;
import kr.co.library.api.constant.JWTConstant;
import kr.co.library.api.constant.response.YmlKey000CommonConstant;
import kr.co.library.api.model.properties.interceptor.InterceptorWhiteListProperty;
import kr.co.library.api.model.properties.message.MessageProperty;
import kr.co.library.api.model.util.jwt.JWTTokenModel;
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

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private InterceptorWhiteListProperty interceptorwhitelistproperty;

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
            log.info("[InterceptorConfiguration.InterceptorHandler.preHandle] START");
            boolean result = true;
            boolean isPathCheck = pathCheck(request);
            String tokenParsingResult = "";
            String token = "";
            String userId = "";
            if(isPathCheck){
                log.info("[InterceptorConfiguration.InterceptorHandler.preHandle] pass url {}" , request.getRequestURL());
                return result;
            }else{
                if(null == token || token.isBlank()){
                    log.info("[InterceptorConfiguration.InterceptorHandler.preHandle] 토큰이 헤더에 존재하지 않음");
                    throw new ServiceFail(YmlKey000CommonConstant.INTERCEPTOR_HEADER_TOKEN_NOT_EXIST);
                }
                try {
                    token = jwtUtils.tokenPrefixParser(request.getHeader(JWTConstant.REQUEST_TOKEN_HEADER_KEY));
                }catch (Exception e){
                    log.error("[InterceptorConfiguration.InterceptorHandler.preHandle] HEADER 파싱 실패");
                    throw new ServiceFail(YmlKey000CommonConstant.INTERCEPTOR_HEADER_TOKEN_PARSING_FAIL , e);
                }
                tokenParsingResult = jwtUtils.tokenValidation(token);
                log.info("[InterceptorConfiguration.InterceptorHandler.preHandle] tokenParsingResult ? : {}" , tokenParsingResult);
                switch (tokenParsingResult){
                    case JWTConstant.TOKEN_VALIDATION_SUCCESS:
                        userId = jwtUtils.getClaimsInKey(token, JWTConstant.GET_CLAIMS_IN_ID);
                        if(userComponent.userExist(userId)){
                            JWTTokenModel redisToken = redisUtils.getHashType(userId);
                            if(null == redisToken){
                                log.info("[InterceptorConfiguration.InterceptorHandler.preHandle] redis에 key가 존재하지 않습니다.");
                                throw new ServiceFail(YmlKey000CommonConstant.INTERCEPTOR_REDIS_KEY_NOT_EXIST);
                            }
                            if(null == redisToken.getAccessToken()){
                                log.info("[InterceptorConfiguration.InterceptorHandler.preHandle] key에 access 토큰이 존재하지 않습니다.");
                                throw new ServiceFail(YmlKey000CommonConstant.INTERCEPTOR_REDIS_ACCESS_TOKEN_NOT_EXIST);
                            }
                            if(null == redisToken.getRefreshToken()){
                                log.info("[InterceptorConfiguration.InterceptorHandler.preHandle] key에 refresh 토큰이 존재하지 않습니다.");
                                throw new ServiceFail(YmlKey000CommonConstant.INTERCEPTOR_REDIS_REFRESH_TOKEN_NOT_EXIST);
                            }
                            if(!token.equals(redisToken.getAccessToken())){
                                log.info("[InterceptorConfiguration.InterceptorHandler.preHandle] 토큰이 일치하지 않습니다.");
                                throw new ServiceFail(YmlKey000CommonConstant.INTERCEPTOR_REDIS_ACCESS_TOKEN_NOT_EQUAL);
                            }
                        }else{
                            log.info("[InterceptorConfiguration.InterceptorHandler.preHandle] 존재하지 않는 사용자입니다. => {}" , userId);
                            throw new ServiceFail(YmlKey000CommonConstant.INTERCEPTOR_USER_NOT_EXIST);
                        }
                        break;
                    case JWTConstant.TOKEN_VALIDATION_EXPIRATION:
                        log.error("[InterceptorConfiguration.InterceptorHandler.preHandle] 만료된 토큰입니다.");
                        throw new ServiceFail(YmlKey000CommonConstant.INTERCEPTOR_TOKEN_VALIDATION_EXPIRATION);
                    case JWTConstant.TOKEN_VALIDATION_FAKE:
                        log.error("[InterceptorConfiguration.InterceptorHandler.preHandle] 위/변조된 토큰입니다.");
                        throw new ServiceFail(YmlKey000CommonConstant.INTERCEPTOR_TOKEN_VALIDATION_FAKE);
                    case JWTConstant.TOKEN_VALIDATION_FAIL:
                        log.error("[InterceptorConfiguration.InterceptorHandler.preHandle] 토큰 파싱에 실패하였습니다.");
                        throw new ServiceFail(YmlKey000CommonConstant.INTERCEPTOR_TOKEN_VALIDATION_PARSING_FAIL);
                    default:
                        log.error("[InterceptorConfiguration.InterceptorHandler.preHandle] 규격 외 결과");
                        throw new ServiceFail(YmlKey000CommonConstant.INTERCEPTOR_TOKEN_VALIDATION_OUT_OF_SPECIFICATION);
                }
            }
            log.info("[InterceptorConfiguration.InterceptorHandler.preHandle] END {}" , userId);
            return result;
        }
        private boolean pathCheck(HttpServletRequest request){
            boolean result = false;
            String uri = request.getRequestURI();

           /* List<String> containsWhitelist = new ArrayList<>(
                    Arrays.asList(
                              "swagger-ui"
                            , "api-docs"
                            , "h2-console"
                    )
            );*/
            /*List<String> startsWithWhitelist = new ArrayList<>(
                    Arrays.asList(
                              "/api/sign-in"
                            , "/api/sign-up"
                            , "/api/sample"
                    )
            );*/

            for(String path : interceptorwhitelistproperty.getContainsWhiteList()){
                if(uri.contains(path)){
                    return true;
                }
            }
            for(String path : interceptorwhitelistproperty.getStartsWithWhiteList()){
                if(uri.startsWith(path)){
                    return true;
                }
            }
            return result;
        }
    }
}
