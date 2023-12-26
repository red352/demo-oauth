package com.yunxiao.service.demooauth.client.auth;

import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.core.token.Token;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;


/**
 * @author LuoYunXiao
 * @since 2023/12/26 10:29
 */
public class PersistenceTokenService {

    private static final String SERVER_SECRET = UUID.randomUUID().toString();

    private final KeyBasedPersistenceTokenService tokenService;

    public static PersistenceTokenService defaultService() {
        KeyBasedPersistenceTokenService tokenService = new KeyBasedPersistenceTokenService();
        try {
            tokenService.setSecureRandom(SecureRandom.getInstanceStrong());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        tokenService.setServerInteger(1);
        tokenService.setServerSecret(SERVER_SECRET);
        tokenService.afterPropertiesSet();
        return new PersistenceTokenService(tokenService);
    }

    public PersistenceTokenService(KeyBasedPersistenceTokenService tokenService) {
        this.tokenService = tokenService;
    }

    public Token allocateToken(String extendedInformation) {
        return tokenService.allocateToken(extendedInformation);
    }

    public Token verifyToken(String key) {
        return tokenService.verifyToken(key);
    }


}
