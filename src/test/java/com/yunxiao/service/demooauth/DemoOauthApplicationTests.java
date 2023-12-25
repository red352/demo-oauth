package com.yunxiao.service.demooauth;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.core.token.Token;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

//@SpringBootTest
class DemoOauthApplicationTests {

    @Test
    void contextLoads() throws NoSuchAlgorithmException {
        KeyBasedPersistenceTokenService tokenService = new KeyBasedPersistenceTokenService();
        tokenService.setServerInteger(1);
        tokenService.setSecureRandom(SecureRandom.getInstanceStrong());
        tokenService.setServerSecret("hhhhhhhh");
        tokenService.setPseudoRandomNumberBytes(5);
        Token token = tokenService.allocateToken("123456");
        System.out.println(tokenService.verifyToken(token.getKey()).getExtendedInformation());

    }

}
