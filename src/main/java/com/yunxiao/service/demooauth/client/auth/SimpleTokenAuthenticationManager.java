package com.yunxiao.service.demooauth.client.auth;

import com.nimbusds.jose.JOSEObject;
import com.yunxiao.service.demooauth.client.token.JOSEService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import reactor.core.publisher.Mono;

/**
 * @author LuoYunXiao
 * @since 2023/12/26 18:56
 */
@AllArgsConstructor
public class SimpleTokenAuthenticationManager implements ReactiveAuthenticationManager {

    private final JOSEService joseService;
    private final ReactiveUserDetailsService userDetailsService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        if (authentication instanceof PreAuthenticatedAuthenticationToken authenticationToken) {
            String accessToken = authenticationToken.getPrincipal().toString();
            JOSEObject joseObject;
            try {
                joseObject = joseService.verify(accessToken);
            } catch (Exception e) {
                // 返回未认证对象
                // 如果返回为 empty Mono 或者 error Mono 会导致整个过滤链中断
                return Mono.just(authentication);
            }
            if (joseObject != null) {
                return userDetailsService.findByUsername(joseService.getPrincipal(joseObject))
                        .map(user -> new PreAuthenticatedAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities()));
            }
        }
        return Mono.just(authentication);
    }
}
