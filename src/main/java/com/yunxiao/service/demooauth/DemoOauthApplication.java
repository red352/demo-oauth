package com.yunxiao.service.demooauth;

import com.yunxiao.service.demooauth.client.auth.PersistenceTokenService;
import com.yunxiao.service.demooauth.client.auth.TokenAuthenticationManager;
import com.yunxiao.service.demooauth.client.auth.TokenConvert;
import com.yunxiao.service.demooauth.client.login.LoginUserDetails;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import reactor.core.publisher.Mono;


@SpringBootApplication
public class DemoOauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoOauthApplication.class, args);
    }

    //    Authorization: Bearer 123token
    @Bean
    SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http) {
        AuthenticationWebFilter tokenFilter = new AuthenticationWebFilter(new TokenAuthenticationManager(persistenceTokenService(), reactiveUserDetailsService()));
        tokenFilter.setServerAuthenticationConverter(new TokenConvert());
        return http
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorize ->
                        authorize.pathMatchers("/login").permitAll()
                                .anyExchange().authenticated())
                .addFilterAt(tokenFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }


    @Bean
    PersistenceTokenService persistenceTokenService() {
        return PersistenceTokenService.defaultService();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    ReactiveUserDetailsService reactiveUserDetailsService() {
        LoginUserDetails user = new LoginUserDetails(1, "admin", passwordEncoder().encode("admin"), 1, null);
        return username -> Mono.just(user);
    }

    @Bean
    UserDetailsRepositoryReactiveAuthenticationManager userDetailsRepositoryReactiveAuthenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager manager = new UserDetailsRepositoryReactiveAuthenticationManager(reactiveUserDetailsService());
        manager.setPasswordEncoder(passwordEncoder());
        return manager;
    }

}
