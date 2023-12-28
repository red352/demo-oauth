package com.yunxiao.service.demooauth.client.token;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;

/**
 * @author LuoYunXiao
 * @since 2023/12/27 21:24
 */
public class JwtServiceFactory {

    public static JwtService createJwtService(JWSSigner signer, JWSVerifier verifier, JWSAlgorithm algorithm) {
        return new JwtService(signer, verifier, algorithm);
    }

    public static JwtService createRSAJwtService(KeyPair keyPair, JWSAlgorithm algorithm) {
        return new JwtService(new RSASSASigner(keyPair.getPrivate()), new RSASSAVerifier((RSAPublicKey) keyPair.getPublic()), algorithm);
    }

    public static JwtService createHSJwtService(byte[] keyBytes, JWSAlgorithm algorithm) {
        try {
            return new JwtService(new MACSigner(keyBytes), new MACVerifier(keyBytes), algorithm);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

}
