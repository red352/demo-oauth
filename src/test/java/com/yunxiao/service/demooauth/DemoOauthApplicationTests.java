package com.yunxiao.service.demooauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.ParseException;
import com.yunxiao.service.demooauth.client.auth.PersistenceTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.token.Token;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
class DemoOauthApplicationTests {

    @Autowired
    private PersistenceTokenService tokenService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() throws NoSuchAlgorithmException {
        Token token = tokenService.allocateToken("123");
        System.out.println(token.getKey());
        System.out.println(tokenService.verifyToken(token.getKey()).getExtendedInformation());

    }

    @Test
    void test1() throws ParseException, IOException {

    }

}
