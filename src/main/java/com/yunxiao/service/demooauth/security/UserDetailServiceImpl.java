package com.yunxiao.service.demooauth.security;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

/**
 * @author LuoYunXiao
 * @since 2023/12/25 19:22
 */
public class UserDetailServiceImpl implements ReactiveUserDetailsService {

    private final PasswordEncoder encoder;

    public UserDetailServiceImpl(PasswordEncoder encoder) {
        this.encoder = encoder;
    }


    @Override
    public Mono<UserDetails> findByUsername(String username) {
        UserDetails user = User.withUsername("admin").password("admin").roles("admin").passwordEncoder(encoder::encode).build();
        return Mono.just(user);
    }
}
