package kr.co.library.configuration;

import kr.co.library.api.common.util.DateUtils;
import kr.co.library.api.common.util.JWTUtils;
import kr.co.library.api.common.util.RedisUtils;
import kr.co.library.api.common.util.rest.NaverRest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class UtilConfiguration {

    @Bean
    public JWTUtils jwtUtils() {
        return new JWTUtils();
    }
    @Bean
    public DateUtils dateUtils() {
        return new DateUtils();
    }
    @Bean
    public RedisUtils redisUtils() {
        return new RedisUtils();
    }
    @Bean
    public NaverRest naverRest() {
        return new NaverRest();
    }

}
