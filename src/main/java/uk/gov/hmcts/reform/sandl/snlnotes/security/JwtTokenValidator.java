package uk.gov.hmcts.reform.sandl.snlnotes.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenValidator {

    @Value("${jwt.secret}")
    private String secret;

    public String parseToken(String token) {
        String username = null;

        try {
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            username = body.getSubject();

        } catch (JwtException e) {
            log.error("Couldn't validate token. Message - {}" , e.getMessage());
        }
        return username;
    }

}
