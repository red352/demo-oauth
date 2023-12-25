package com.yunxiao.service.demooauth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author LuoYunXiao
 * @since 2023/12/25 19:29
 */
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserDetailsRepositoryReactiveAuthenticationManager manager;

    @GetMapping
    public String hello() {
        return "hello world";
    }

    @PostMapping("/login")
    public Mono<Authentication> login(@RequestBody LoginDto loginDto) {
        return manager.authenticate(loginDto)
                .flatMap(authentication -> ReactiveSecurityContextHolder.getContext()
                        .doOnNext(securityContext -> securityContext.setAuthentication(authentication))
                        .thenReturn(authentication));
    }
}

