package login.jwtlogin.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


public class JWTTokenUtil {
    public String generateToken(String email, SecretKey key, Date expDate) {
        String jwt = Jwts.builder()
                .issuer("Login")
                .subject(email)
                .issuedAt(new Date())
                .expiration(expDate)
                .signWith(key)
                .compact();
        return "Bearer " + jwt;

    }

    public Claims parseToken(SecretKey key, String jwt) {
        jwt = jwt.substring(7);
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
        return claims;
    }

    //키생성 메서드
    public SecretKey getSecretKey(String genKey) {
        return Keys.hmacShaKeyFor(genKey.getBytes(StandardCharsets.UTF_8));
    }

    public Date getAccessTokenDate() {
        return new Date(System.currentTimeMillis() + 300 * 1000);
    }

    public Date getRefreshTokenDate() {
        return new Date(System.currentTimeMillis() + 600 * 1000);
    }

    public String getTokenEmail(Claims claims) {
        return String.valueOf(claims.getSubject());
    }
}
