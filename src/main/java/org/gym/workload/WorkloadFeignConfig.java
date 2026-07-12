package org.gym.workload;

import feign.RequestInterceptor;
import org.gym.security.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

public class WorkloadFeignConfig {

    @Bean
    public RequestInterceptor workloadRequestInterceptor(JwtService jwtService) {
        return template -> template.header(HttpHeaders.AUTHORIZATION,
                "Bearer " + jwtService.generateToken("gym-main-service"));
    }
}
