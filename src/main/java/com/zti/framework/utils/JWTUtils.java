package com.zti.framework.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

public class JWTUtils {

    static Logger log = LogManager.getLogger(JWTUtils.class);

    /**
     * Create JWT
     * @param secretKey
     * @param id
     * @param issuer
     * @param subject
     * @param ttlMillis
     * @return
     */
    public static String createJWT(String secretKey, String id, String issuer, String subject, long ttlMillis) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);

        //if it has been specified, let's add the expiration
        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
            log.info("expire : " + exp.toString());
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    /**
     * Decode JWT
     * @param secretKey
     * @param token
     * @return
     */
    public static Claims decodeJWT(String secretKey, String token) {
        Claims claims = null;
        try{
            if(!Utils.isEmpty(token)) {
                claims = Jwts.parser()
                        .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                        .parseClaimsJws(token).getBody();
//            log.info("ID: " + claims.getId());
//            log.info("Subject: " + claims.getSubject());
//            log.info("Issuer: " + claims.getIssuer());
//            log.info("Expiration: " + claims.getExpiration());
            }

        }catch(Exception ex){
            log.error(ex);
        }
        return claims;
    }
}
