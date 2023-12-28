package com.yunxiao.service.demooauth.client.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author LuoYunXiao
 * @since 2023/12/26 20:05
 */
public class BearerTokenAuthenticationConvert implements ServerAuthenticationConverter {
    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        List<String> list = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (list != null && !list.isEmpty() && list.getFirst().startsWith("Bearer ")) {
            String token = list.getFirst().substring(7);
            return Mono.just(new PreAuthenticatedAuthenticationToken(token, token));
        }
        return Mono.empty();
    }
}
