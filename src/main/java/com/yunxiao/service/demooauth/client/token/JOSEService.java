package com.yunxiao.service.demooauth.client.token;

import com.nimbusds.jose.JOSEObject;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;

/**
 * @author LuoYunXiao
 * @since 2023/12/27 22:08
 */
public interface JOSEService {

    JOSEObject verify(String token);

    /**
     * 获取认证主体，如果是登录，存放username或者id
     * @return principal
     * @see ReactiveUserDetailsService#findByUsername(String)
     */
    String getPrincipal(JOSEObject joseObject);
}
