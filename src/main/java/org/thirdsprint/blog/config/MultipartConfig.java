package org.thirdsprint.blog.config;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MultipartConfig {
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        // Параметры: location, maxFileSize, maxRequestSize, fileSizeThreshold
        // Здесь пример с пустым location и лимитами по 10MB
        return new MultipartConfigElement("", 10_485_760, 10_485_760, 0);
    }
}
