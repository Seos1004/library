package kr.co.library.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
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
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            boolean result = true;
            boolean isPathCheck = pathCheck(request);
            if(isPathCheck){
                log.info("[InterceptorConfiguration.InterceptorHandler.preHandle] pass url {}" , request.getRequestURL());
                return result;
            }else{
                log.info("redis jwt토큰 체크 예정");
                //토큰확인후 유효할경우 세션정보를 새롭게 갱신해주는 소스 추가
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
