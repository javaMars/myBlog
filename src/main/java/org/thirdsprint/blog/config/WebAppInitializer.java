package org.thirdsprint.blog.config;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected @Nullable Class<?>[] getRootConfigClasses() { return null; }

    @Override
    protected @NonNull Class<?>[] getServletConfigClasses() { return new Class[] { WebConfig.class }; }

    @Override
    protected @NonNull String[] getServletMappings() { return new String[] { "/" }; }
}
