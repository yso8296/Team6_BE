package supernova.whokie.global.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import supernova.whokie.global.exception.AuthenticationException;
import supernova.whokie.user.Role;

import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.validate-time}")
    private long validityInMilliseconds;
    @Value("${jwt.secret}")
    private String secretKey;

    public String createToken(Long id, Role role) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("role", role.name())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Claims getClaim(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (SecurityException | MalformedJwtException e) {
            throw new JwtException("유효하지 않은 토큰입니다.");
        } catch (ExpiredJwtException e) {
            throw new JwtException("만료된 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("지원되지 않는 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new JwtException("토큰이 존재하지 않습니다.");
        } catch (Exception e) {
            throw new AuthenticationException("토큰 에러(수정 예정)"); // 이거 왜 항상 시그니처 에러가 뜰까
        }
    }

}
