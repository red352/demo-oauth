package com.yunxiao.service.demooauth.controller;

import com.yunxiao.service.demooauth.client.login.LoginAuthentication;
import com.yunxiao.service.demooauth.client.login.LoginUserDetails;
import com.yunxiao.service.demooauth.client.token.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author LuoYunXiao
 * @since 2023/12/25 19:29
 */
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final JwtService jwtService;
    private final UserDetailsRepositoryReactiveAuthenticationManager manager;

    @GetMapping
    public Mono<?> hello() {
//        return ReactiveSecurityContextHolder.getContext()
//                .map(SecurityContext::getAuthentication);
        return Mono.just("hello");
    }

    @PostMapping("/login")
    public Mono<String> login(@RequestBody LoginAuthentication loginAuthentication) {
        return manager.authenticate(loginAuthentication)
                .map(Authentication::getPrincipal)
                .cast(LoginUserDetails.class)
                .map(authed -> jwtService.generateBuilder()
                        .baseJwtClaimsSet(builder -> builder.subject(authed.getUsername()))
                        .claimObj(Map.of("time", LocalDateTime.now().toString()))
                        .generate());
    }
}

