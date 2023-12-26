package com.yunxiao.service.demooauth.controller;

import com.yunxiao.service.demooauth.client.auth.PersistenceTokenService;
import com.yunxiao.service.demooauth.client.login.LoginAuthentication;
import com.yunxiao.service.demooauth.client.login.LoginUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
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

    private final PersistenceTokenService persistenceTokenService;
    private final UserDetailsRepositoryReactiveAuthenticationManager manager;

    @GetMapping
    public Mono<?> hello() {
//        return ReactiveSecurityContextHolder.getContext()
//                .map(SecurityContext::getAuthentication);
        return Mono.just("hello");
    }

    @PostMapping("/login")
    public Mono<?> login(@RequestBody LoginAuthentication loginAuthentication) {
        return manager.authenticate(loginAuthentication)
                .map(Authentication::getPrincipal)
                .cast(LoginUserDetails.class)
                .map(authed -> {
                    authed.setAccessToken(persistenceTokenService.allocateToken(authed.getUsername()).getKey());
                    return authed;
                });
    }
}

