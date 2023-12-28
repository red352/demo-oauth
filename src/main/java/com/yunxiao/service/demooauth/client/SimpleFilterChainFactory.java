package com.yunxiao.service.demooauth.client;

import com.yunxiao.service.demooauth.client.auth.BearerTokenAuthenticationConvert;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

/**
 * @author LuoYunXiao
 * @since 2023/12/27 22:01
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleFilterChainFactory {

    public static SecurityWebFilterChain simpleAuthenticationFilter(final ServerHttpSecurity http, ReactiveAuthenticationManager authenticationManager) {
        AuthenticationWebFilter tokenFilter = new AuthenticationWebFilter(authenticationManager);
        tokenFilter.setServerAuthenticationConverter(new BearerTokenAuthenticationConvert());
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
}
