package org.gym.workload.config;

import org.gym.workload.logging.RestCallLoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RestCallLoggingInterceptor restCallLoggingInterceptor;

    public WebConfig(RestCallLoggingInterceptor restCallLoggingInterceptor) {
        this.restCallLoggingInterceptor = restCallLoggingInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(restCallLoggingInterceptor)
                .addPathPatterns("/api/**");
    }
}
