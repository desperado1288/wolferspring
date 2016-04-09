package com.wolferx.wolferspring.service;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);
    private static final Cache restApiAuthTokenCache = CacheManager.getInstance().getCache("restApiAuthTokenCache");
    private static final int HALF_AN_HOUR_IN_MILLISECONDS = 30 * 60 * 1000;
    private static final String encodedSecret = "c3VwZXJfdG9rZW5feA==";
    private static final JWTSigner jwtSigner = new JWTSigner(encodedSecret);

    @Scheduled(fixedRate = HALF_AN_HOUR_IN_MILLISECONDS)
    public void evictExpiredTokens() {
        logger.info("Evicting expired tokens");
        restApiAuthTokenCache.evictExpiredElements();
    }

    public String generateNewToken() {
        return UUID.randomUUID().toString();
    }

    public void store(String token, Authentication authentication) {
        restApiAuthTokenCache.put(new Element(token, authentication));
    }

    public boolean contains(String token) {
        return restApiAuthTokenCache.get(token) != null;
    }

    public Authentication retrieve(String token) {
        return (Authentication) restApiAuthTokenCache.get(token).getObjectValue();
    }

    public String signToken (final Map<String, Object> claims ) {
        return jwtSigner.sign(claims);
    }

    @Override
    public String signToken (final Long userId ) {
        final Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return jwtSigner.sign(claims);
    }

    @Override
    public Map<String,Object> verifyToken (final String token) {

        Map<String,Object> payload;
        try {
            payload = new JWTVerifier(encodedSecret).verify(token);

            if (payload.isEmpty()) {
                logger.error("<In> verifyToken() Empty payload in verified token");
                throw new InternalAuthenticationServiceException("Empty payload in verified token");
            }

        } catch (final JWTVerifyException | IllegalStateException | IOException jwtVerifyException) {
            logger.error("<In> verifyToken() Failed to verify token", jwtVerifyException);
            throw new InternalAuthenticationServiceException("Failed to verify token", jwtVerifyException);
        } catch (final InvalidKeyException | NoSuchAlgorithmException | SignatureException invalidTokenException) {
            System.err.println("Invalid Token!" + invalidTokenException);
            throw new InternalAuthenticationServiceException("Invalid Token!", invalidTokenException);
        }

        return payload;
    }
}
