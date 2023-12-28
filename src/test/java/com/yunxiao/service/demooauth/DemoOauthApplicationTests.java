package com.yunxiao.service.demooauth;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

@SpringBootTest
class DemoOauthApplicationTests {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Test
    void test1() throws ParseException, IOException {
        String join = JSONUtil.readJSONArray(new File("C:/Users/lenovo/Desktop/tmp.json"), Charset.defaultCharset()).join(",\n");
        System.out.println(join);
    }


}
