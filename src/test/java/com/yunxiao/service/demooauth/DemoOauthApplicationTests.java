package com.yunxiao.service.demooauth;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.ParseException;
import com.yunxiao.service.demooauth.client.token.PersistenceTokenProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.token.Token;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
class DemoOauthApplicationTests {

    @Autowired
    private PersistenceTokenProcessor tokenService;
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
        String join = JSONUtil.readJSONArray(new File("C:/Users/lenovo/Desktop/tmp.json"), Charset.defaultCharset()).join(",\n");
        System.out.println(join);
    }


}
