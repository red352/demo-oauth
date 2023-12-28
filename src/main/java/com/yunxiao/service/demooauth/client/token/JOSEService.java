package com.yunxiao.service.demooauth.client.token;

import com.nimbusds.jose.JOSEObject;

/**
 * @author LuoYunXiao
 * @since 2023/12/27 22:08
 */
public interface JOSEService {

    JOSEObject verify(String token);

    /**
     * 获取认证主体
     * @return principal
     */
    String getPrincipal(JOSEObject joseObject);
}
