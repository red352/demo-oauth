package com.yunxiao.service.demooauth.client.login;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * @author LuoYunXiao
 * @since 2023/12/25 19:32
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class LoginAuthentication extends UsernamePasswordAuthenticationToken{


    private String username;
    private String password;

    public LoginAuthentication(String username, String password) {
        super(username, password);
    }


}
