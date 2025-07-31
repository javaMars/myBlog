package ru.practicum.sprint4.spring.boot.blog.config;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletRegistration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    private static final String LOCATION = "";
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final long MAX_REQUEST_SIZE = 10 * 1024 * 1024; // 10MB
    private static final int FILE_SIZE_THRESHOLD = 0;

    @Override
    protected @NonNull Class<?>[] getRootConfigClasses() {
        return new Class[] { AppConfig.class };
    }

    @Override
    protected @NonNull Class<?>[] getServletConfigClasses() {
        return new Class[] { WebConfig.class };
    }

    @Override
    protected @NonNull String[] getServletMappings() {
        return new String[] { "/" };
    }

    @Override
    protected void customizeRegistration(@NonNull ServletRegistration.Dynamic registration) {
        MultipartConfigElement multipartConfigElement =
                new MultipartConfigElement(LOCATION, MAX_FILE_SIZE, MAX_REQUEST_SIZE, FILE_SIZE_THRESHOLD);
        registration.setMultipartConfig(multipartConfigElement);
    }
}
