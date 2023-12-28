package com.yunxiao.service.demooauth;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jwt.JWTClaimsSet;
import com.yunxiao.service.demooauth.client.SimpleFilterChainFactory;
import com.yunxiao.service.demooauth.client.auth.TokenAuthenticationManager;
import com.yunxiao.service.demooauth.client.login.LoginUserDetails;
import com.yunxiao.service.demooauth.client.token.JwtService;
import com.yunxiao.service.demooauth.client.token.JwtServiceFactory;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.security.KeyPairGenerator;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;


@SpringBootApplication
public class DemoOauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoOauthApplication.class, args);
    }

    //    Authorization: Bearer 123token
    @Bean
    SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http) {
        TokenAuthenticationManager manager = new TokenAuthenticationManager(jwtService(), reactiveUserDetailsService());
        return SimpleFilterChainFactory.simpleAuthenticationFilter(http, manager);
    }


    @Bean
    @SneakyThrows
    JwtService jwtService() {
        JwtService jwtService = JwtServiceFactory.createRSAJwtService(KeyPairGenerator.getInstance("RSA").generateKeyPair(), JWSAlgorithm.RS256);
        jwtService.baseJwtClaimsSet(new JWTClaimsSet.Builder()
                .expirationTime(new Date(LocalDateTime.now().plusDays(1).toEpochSecond(ZoneOffset.ofHours(8)) * 1000))
                .build());
        return jwtService;
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
