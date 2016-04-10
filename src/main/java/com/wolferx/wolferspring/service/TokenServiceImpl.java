package com.wolferx.wolferspring.service;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.wolferx.wolferspring.common.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenServiceImpl implements TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    private static final JWTSigner jwtSigner = new JWTSigner(Constant.AUTH_JWT_SECRET);
    private static final JWTVerifier jwtVerifier = new JWTVerifier(Constant.AUTH_JWT_SECRET);
    private static final JWTSigner.Options jwtOptions = new JWTSigner.Options()
        .setExpirySeconds(Constant.AUTH_JWT_TOKEN_EXPIRE).setNotValidBeforeLeeway(5).setIssuedAt(true).setJwtId(true);

    @Override
    public String signToken (final Long userId ) {

        final Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);

        return jwtSigner.sign(claims, jwtOptions);
    }

    @Override
    public Map<String,Object> verifyToken (final String token) {

        final Map<String,Object> payload;
        try {
            payload = jwtVerifier.verify(token);

            // validate payload
            if (payload.isEmpty()) {
                logger.error("<In> verifyToken() Empty payload in verified token");
                throw new InternalAuthenticationServiceException("Empty payload in verified token");
            }

        } catch (final JWTVerifyException | IllegalStateException | IOException jwtVerifyException) {
            logger.error("<In> verifyToken() Failed to verify token", jwtVerifyException);
            throw new InternalAuthenticationServiceException("Failed to verify token", jwtVerifyException);
        } catch (final InvalidKeyException | NoSuchAlgorithmException | SignatureException invalidTokenException) {
            logger.error("<In> verifyToken() Invalid token", invalidTokenException);
            throw new InternalAuthenticationServiceException("Invalid Token!", invalidTokenException);
        }

        return payload;
    }
}
