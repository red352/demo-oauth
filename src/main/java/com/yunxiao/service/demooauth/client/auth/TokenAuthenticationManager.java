package com.yunxiao.service.demooauth.client.auth;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import reactor.core.publisher.Mono;

/**
 * @author LuoYunXiao
 * @since 2023/12/26 18:56
 */
@AllArgsConstructor
public class TokenAuthenticationManager implements ReactiveAuthenticationManager {

    private final PersistenceTokenService tokenService;
    private final ReactiveUserDetailsService userDetailsService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        if (authentication instanceof PreAuthenticatedAuthenticationToken authenticationToken) {
            String accessToken = authenticationToken.getPrincipal().toString();
            Token token;
            try {
                token = tokenService.verifyToken(accessToken);
            } catch (Exception e) {
                throw new BadCredentialsException("token校验失败");
            }
            if (token != null) {
                authenticationToken.setAuthenticated(true);
                return userDetailsService.findByUsername(token.getKey())
                        .map(user -> new PreAuthenticatedAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities()));
            }
        }
        return Mono.empty();
    }
}
