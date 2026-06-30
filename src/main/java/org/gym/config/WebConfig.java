package org.gym.config;

import org.gym.logging.RestCallLoggingInterceptor;
import org.gym.security.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final RestCallLoggingInterceptor restCallLoggingInterceptor;

    public WebConfig(AuthInterceptor authInterceptor, RestCallLoggingInterceptor restCallLoggingInterceptor) {
        this.authInterceptor = authInterceptor;
        this.restCallLoggingInterceptor = restCallLoggingInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(restCallLoggingInterceptor)
                .addPathPatterns("/api/**");

        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/trainees/register",
                        "/api/trainers/register",
                        "/api/login",
                        "/api/training-types");
    }
}
