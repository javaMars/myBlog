package ru.practicum.sprint4.spring.boot.blog.config;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app.upload")
@Component
@Getter
@Setter
public class UploadProperties {
    private String dir;
    private String url;
}
