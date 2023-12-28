package com.yunxiao.service.demooauth;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author LuoYunXiao
 * @since 2023/12/27 17:06
 */
public class TokenTests {

    @Test
    @SneakyThrows
    void testJws() {
        // Create an HMAC-protected JWS object with a string payload
        JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256),
                new Payload("Hello, world!"));

// We need a 256-bit key for HS256 which must be pre-shared
        byte[] sharedKey = new byte[32];
        new SecureRandom().nextBytes(sharedKey);

// Apply the HMAC to the JWS object
        jwsObject.sign(new MACSigner(sharedKey));

// Output in URL-safe format
        System.out.println(jwsObject.serialize());
        boolean verify = jwsObject.verify(new MACVerifier(sharedKey));
        System.out.println(verify);
    }


    @Test
    @SneakyThrows
    void testJwt() {
        // Generate random 256-bit (32-byte) shared secret
        SecureRandom random = new SecureRandom();
        byte[] sharedSecret = new byte[32];
        random.nextBytes(sharedSecret);

//        String sharedSecret = "123";

// Create HMAC signer
        JWSSigner signer = new MACSigner(sharedSecret);

// Prepare JWT with claims set
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("alice")
                .issuer("https://c2id.com")
                .expirationTime(new Date(new Date().getTime() + 60 * 1000))
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

// Apply the HMAC protection
        signedJWT.sign(signer);

// Serialize to compact form, produces something like
// eyJhbGciOiJIUzI1NiJ9.SGVsbG8sIHdvcmxkIQ.onO9Ihudz3WkiauDO2Uhyuz0Y18UASXlSc1eS0NkWyA
        String s = signedJWT.serialize();

// On the consumer side, parse the JWS and verify its HMAC
        signedJWT = SignedJWT.parse(s);

        JWSVerifier verifier = new MACVerifier(sharedSecret);

        assertTrue(signedJWT.verify(verifier));

// Retrieve / verify the JWT claims according to the app requirements
        assertEquals("alice", signedJWT.getJWTClaimsSet().getSubject());
        assertEquals("https://c2id.com", signedJWT.getJWTClaimsSet().getIssuer());
        assertTrue(new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime()));
    }

}
