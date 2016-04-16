package com.wolferx.wolferspring.service;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.wolferx.wolferspring.common.constant.Constant;
import com.wolferx.wolferspring.jdbi.dao.TokenDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class TokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);

    private static final JWTSigner jwtSigner = new JWTSigner(Constant.AUTH_JWT_SECRET);
    private static final JWTVerifier jwtVerifier = new JWTVerifier(Constant.AUTH_JWT_SECRET);
    private static final JWTSigner.Options jwtOptions = new JWTSigner.Options()
        .setExpirySeconds(Constant.AUTH_JWT_TOKEN_EXPIRE)
        .setNotValidBeforeLeeway(5).setIssuedAt(true)
        .setJwtId(true);
    private static final JWTSigner.Options jwtRefreshOptions = new JWTSigner.Options()
        .setExpirySeconds(Constant.AUTH_JWT_REFRESH_TOKEN_EXPIRE)
        .setNotValidBeforeLeeway(5).setIssuedAt(true)
        .setJwtId(true);

    private TokenDao tokenDao;

    public TokenService() { }

    @Autowired
    public TokenService(TokenDao tokenDao) {
        this.tokenDao = tokenDao;
    }

    public String genToken(final Long userId) {

        final Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);

        return jwtSigner.sign(claims, jwtOptions);
    }

    public String genRefreshToken(final Long userId) {

        final Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        final String refreshToken = jwtSigner.sign(claims, jwtRefreshOptions);
        final Date timeNow = new Date();
        tokenDao.upsert(userId, "", "", refreshToken, timeNow);

        return refreshToken;
    }

    public Map<String,Object> verifyToken (final String token)
        throws InternalAuthenticationServiceException {

        final Map<String,Object> payload;
        try {
            payload = jwtVerifier.verify(token);

            if (payload.isEmpty()) {
                throw new InternalAuthenticationServiceException("Empty payload in token");
            }

        } catch (final IllegalStateException | IOException jwtVerifyException) {
            LOGGER.error("<In> verifyToken(): Failed to verify token", jwtVerifyException);
            throw new InternalAuthenticationServiceException("Failed to verify token!");
        } catch (final JWTVerifyException | InvalidKeyException | NoSuchAlgorithmException | SignatureException invalidTokenException) {
            LOGGER.error("<In> verifyToken(): Invalid token", invalidTokenException);
            throw new InternalAuthenticationServiceException("Invalid Token!");
        }

        return payload;
    }

    public Optional<String> getRefreshTokenByUserId (final Long userId) {
        return Optional.ofNullable(tokenDao.getRefreshTokenByUserId(userId));
    }
}
