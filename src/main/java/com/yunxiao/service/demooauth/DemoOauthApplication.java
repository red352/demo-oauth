package com.yunxiao.service.demooauth;

import com.yunxiao.service.demooauth.security.UserDetailServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.server.AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@SpringBootApplication
public class DemoOauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoOauthApplication.class, args);
    }

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        // TODO:
        InMemoryReactiveClientRegistrationRepository repository = new InMemoryReactiveClientRegistrationRepository();
        ReactiveOAuth2AuthorizedClientManager manager = new DefaultReactiveOAuth2AuthorizedClientManager(
                repository,
                new AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository(new InMemoryReactiveOAuth2AuthorizedClientService(repository))
        );
        return http
                .authorizeExchange(authorize ->
                        authorize.pathMatchers("/login").permitAll()
                                .anyExchange().authenticated())
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                // TODO:
                .oauth2Login(oauth2Login -> oauth2Login
                        .authenticationConverter(new ServerAuthenticationConverter() {
                            @Override
                            public Mono<Authentication> convert(ServerWebExchange exchange) {
                                return null;
                            }
                        })
                        .authenticationManager(userDetailsRepositoryReactiveAuthenticationManager()))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    ReactiveUserDetailsService userDetailsService() {
        return new UserDetailServiceImpl(passwordEncoder());
    }

    @Bean
    UserDetailsRepositoryReactiveAuthenticationManager userDetailsRepositoryReactiveAuthenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager manager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService());
        manager.setPasswordEncoder(passwordEncoder());
        return manager;
    }

}
