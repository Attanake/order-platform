package arch.attanake.security.jwt;

import arch.attanake.dto.JwtAuthenticationDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@Slf4j
public class JwtService {

    private static final Logger LOGGER = LogManager.getLogger(JwtService.class);
    private final JwtProperties jwtProperties;
    @Value("${jwt.secret}")
    private String JwtSecret;

    public JwtService(@Qualifier("jwt-arch.attanake.security.jwt.JwtProperties") JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        log.info("ACCESS TOKEN TTL = {}", jwtProperties.getAccessTokenTtl());
        log.info("REFRESH TOKEN TTL = {}", jwtProperties.getRefreshTokenTtl());
    }


    public JwtAuthenticationDto generateAuthToken(String username) {
        JwtAuthenticationDto jwtDto = new JwtAuthenticationDto();
        jwtDto.setToken(generateJwtToken(username));
        jwtDto.setRefreshToken(generateRefreshToken(username));
        return jwtDto;
    }

    public JwtAuthenticationDto refreshBaseToken(String username, String refreshToken) {
        JwtAuthenticationDto jwtDto = new JwtAuthenticationDto();
        jwtDto.setToken(generateJwtToken(username));
        jwtDto.setRefreshToken(generateJwtToken(refreshToken));
        return jwtDto;
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public Boolean validateJwtToken(String token) {
        try{
            Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        }catch (ExpiredJwtException expiredEx){
            LOGGER.error("Expired JWT token", expiredEx);
        }catch (UnsupportedJwtException unsupportedEx){
            LOGGER.error("Unsopported JWT token", unsupportedEx);
        }catch (MalformedJwtException malformedEx){
            LOGGER.error("Malformed JWT token", malformedEx);
        }catch (SecurityException securityEx){
            LOGGER.error("Security exception", securityEx);
        }catch (Exception ex){
            LOGGER.error("Exception", ex);
        }
        return false;
    }

    private String generateJwtToken(String username) {
        return Jwts.builder()
                .subject(username)
                .expiration(Date.from(Instant.now().plus(jwtProperties.getAccessTokenTtl())))
                .signWith(getSignInKey())
                .compact();
    }

    private String generateRefreshToken(String username) {
        return Jwts.builder()
                .subject(username)
                .expiration(Date.from(Instant.now().plus(jwtProperties.getRefreshTokenTtl())))
                .signWith(getSignInKey())
                .compact();
    }

    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(JwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}
