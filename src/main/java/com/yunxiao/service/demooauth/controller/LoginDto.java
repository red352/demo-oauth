package com.yunxiao.service.demooauth.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * @author LuoYunXiao
 * @since 2023/12/25 19:32
 */
@Getter
@Setter
public class LoginDto extends UsernamePasswordAuthenticationToken {


    private String username;
    private String password;

    public LoginDto(String username, String password) {
        super(username, password);
    }
}
