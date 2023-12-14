package kr.co.library.api.model.properties.interceptor;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "interceptor")
public class InterceptorWhiteListProperty {
    private List<String> containsWhiteList;
    private List<String> startsWithWhiteList;
}


