package com.yunxiao.service.demooauth.cors;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * @author nekoimi 2023/9/19 9:20
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {
    private String pathPattern = "/**";
    private List<String> allowedOriginPatterns = List.of("*");
    private List<String> allowedMethods = List.of("GET", "POST", "PUT", "DELETE");
    private List<String> allowedHeaders = List.of("*");
    private List<String> exposedHeaders = List.of("*");
    private Boolean allowCredentials = true;
    private Long maxAge = 3600L;


    @Bean
    CorsConfigurationSource corsConfigurationSource(final CorsProperties corsProperties) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(corsProperties.getAllowedOriginPatterns());
        configuration.setAllowedMethods(corsProperties.getAllowedMethods());
        configuration.setAllowCredentials(corsProperties.getAllowCredentials());
        configuration.setMaxAge(corsProperties.getMaxAge());
        configuration.setExposedHeaders(corsProperties.getExposedHeaders());
        configuration.setAllowedHeaders(corsProperties.getAllowedHeaders());
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(corsProperties.getPathPattern(), configuration);
        return source;
    }
}
