package com.yunxiao.service.demooauth.client.token;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.nimbusds.jose.*;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.Getter;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * @author LuoYunXiao
 * @since 2023/12/27 17:16
 */
public class JwtService implements JOSEService {

    private final JWSSigner signer;
    private final JWSVerifier verifier;
    @Getter
    private final JWSAlgorithm algorithm;
    @Getter
    private final JWSHeader jwsHeader;
    @Getter
    private JWTClaimsSet baseJwtClaimsSet;

    public JwtService(JWSSigner signer, JWSVerifier verifier, JWSAlgorithm algorithm) {
        this.signer = signer;
        this.verifier = verifier;
        this.algorithm = algorithm;
        this.jwsHeader = new JWSHeader(algorithm);
    }


    /**
     * 生成jwt
     *
     * @return jwt string
     */
    public String generate(JWTClaimsSet jwtClaimsSet) {
        SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
        try {
            signedJWT.sign(signer);
        } catch (JOSEException e) {
            throw new RuntimeException("生成签名错误", e);
        }
        return signedJWT.serialize();
    }

    /**
     * 设置基本payload
     *
     * @param baseJwtClaimsSet base payload
     */
    public void baseJwtClaimsSet(JWTClaimsSet baseJwtClaimsSet) {
        this.baseJwtClaimsSet = baseJwtClaimsSet;
    }


    public GenerateBuilder generateBuilder() {
        return new GenerateBuilder(this);
    }

    public SignedJWT parse(String jwt) {
        SignedJWT signedJWT;
        try {
            signedJWT = SignedJWT.parse(jwt);
        } catch (ParseException e) {
            throw new RuntimeException("解析错误", e);
        }
        return signedJWT;
    }

    /**
     * 验证是否过期
     *
     * @return true 有效期内 false 过期
     */
    public boolean isNonExpire(SignedJWT signedJWT) {
        try {
            Date date = signedJWT.getJWTClaimsSet().getExpirationTime();
            return date.after(new Date());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 验证jwt是否有效
     *
     * @return 无效返回null
     */
    public SignedJWT verify(String jwt) {
        SignedJWT signedJWT = parse(jwt);
        try {
            return isNonExpire(signedJWT) && signedJWT.verify(verifier) ? signedJWT : null;
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getPrincipal(JOSEObject joseObject) {
        if (joseObject instanceof SignedJWT signedJWT) {
            try {
                return signedJWT.getJWTClaimsSet().getSubject();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    /**
     * 验证jwt是否有效
     *
     * @param jwt            jwt string
     * @param verifyFunction 自定义验证方式
     * @return 无效返回null
     */
    public SignedJWT verify(String jwt, Function<SignedJWT, Boolean> verifyFunction) {
        SignedJWT signedJWT = parse(jwt);
        return verifyFunction.apply(signedJWT) ? signedJWT : null;
    }


    public static class GenerateBuilder {
        private final JwtService jwtService;
        private JWTClaimsSet.Builder builder;

        public GenerateBuilder(JwtService jwtService) {
            this.jwtService = jwtService;
            if (jwtService.baseJwtClaimsSet == null) {
                this.builder = new JWTClaimsSet.Builder();
            } else {
                this.builder = new JWTClaimsSet.Builder(jwtService.baseJwtClaimsSet);
            }

        }

        public GenerateBuilder baseJwtClaimsSet(Function<JWTClaimsSet.Builder, JWTClaimsSet.Builder> function) {
            this.builder = function.apply(builder);
            return this;
        }

        public GenerateBuilder claimObj(Object object) {
            JSONObject jsonObject = JSONUtil.parseObj(object);
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                builder.claim(entry.getKey(), entry.getValue());
            }
            return this;
        }

        public String generate() {
            return jwtService.generate(builder.build());
        }
    }
}
