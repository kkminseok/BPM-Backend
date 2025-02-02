package d83t.bpmbackend.security.jwt;

import d83t.bpmbackend.domain.aggregate.profile.entity.Profile;
import d83t.bpmbackend.domain.aggregate.profile.service.UserServiceDetail;
import d83t.bpmbackend.domain.aggregate.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
@Getter
@AllArgsConstructor
@Slf4j
public class JwtService {

    private final JwtConfig jwtConfig;

    private final UserServiceDetail userServiceDetail;

    private Key getSignKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractAllClaims(String token) throws ExpiredJwtException {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey(jwtConfig.getKey()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = extractAllClaims(token).getExpiration();
        return expiration.after(new Date());
    }

    public String getUUID(String token) {
        return extractAllClaims(token).get("uuid", String.class);
    }

    public String createToken(String uuid) {
        Claims claims = Jwts.claims();
        claims.put("uuid", uuid);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiry() * 1000))
                .signWith(getSignKey(jwtConfig.getKey()))
                .compact();
    }

    public Boolean validateToken(String token) {
        return isTokenExpired(token);
    }

    public Authentication getAuthentication(String jwtToken) {
        log.info("token user uuid: " + getUUID(jwtToken));
        UserDetails userDetails = userServiceDetail.loadUserByUsername(getUUID(jwtToken));
        User user = (User)userDetails;
        return new UsernamePasswordAuthenticationToken(user, "", userDetails.getAuthorities());
    }

}
