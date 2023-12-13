package kr.co.library.api.model.properties.message;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "message")
public class MessageProperty {
    private String baseFilePath;
    private List<String> filePathList;
}
