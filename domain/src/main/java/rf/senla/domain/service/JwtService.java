package rf.senla.domain.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import rf.senla.domain.entity.User;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Сервис для работы с JWT.
 */
@Slf4j
@Service
public class JwtService implements IJwtService {
    @Value("${token.signing.key}")
    private String jwtSigningKey;

    @Override
    public String extractUsername(String token) {
        log.info("Извлечение имени пользователя из токена {}", token);
        String claim = extractClaim(token, Claims::getSubject);
        log.info("Удалось извлечь имя пользователя {}", claim);
        return claim;
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        log.info("Генерация токена");
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof User customUserDetails) {
            claims.put("id", customUserDetails.getId());
            claims.put("email", customUserDetails.getEmail());
            claims.put("role", customUserDetails.getRole());
        }

        String token = generateToken(claims, userDetails);
        log.info("Генерация токена прошла успешно");
        return token;
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        log.info("Валидация токена {}", token);
        boolean result = (extractUsername(token).equals(userDetails.getUsername())) && !isTokenExpired(token);
        log.info("Результат валидации токена - {}", result);
        return result;
    }

    /**
     * Извлечение данных из токена
     * @param token токен
     * @param claimsResolvers функция извлечения данных
     * @return данные
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        log.info("Извлечение данных из токена");
        final Claims claims = extractAllClaims(token);
        T data = claimsResolvers.apply(claims);
        log.info("Данные из токена извлечены успешно - {}", data);
        return data;
    }

    /**
     * Генерация токена
     * @param extraClaims дополнительные данные
     * @param userDetails данные пользователя
     * @return токен
     */
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        log.info("Генерация токена с дополнительными параметрами {}", extraClaims);
        String token = Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 100_000 * 60 * 24 * 10))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
        log.info("Генерация токена с дополнительными параметрами прошла успешно - {}", token);
        return token;
    }

    /**
     * Проверка токена на просроченность
     * @param token токен
     * @return true, если токен просрочен
     */
    private boolean isTokenExpired(String token) {
        log.info("Проверка токена {} на просроченность", token);
        boolean result = extractExpiration(token).before(new Date());
        log.info("Результат проверки токена на просроченность - {}", result);
        return result;
    }

    /**
     * Извлечение даты истечения токена
     * @param token токен
     * @return дата истечения
     */
    private Date extractExpiration(String token) {
        log.info("Извлечение даты истечения токена {}", token);
        Date date = extractClaim(token, Claims::getExpiration);
        log.info("Дата истечения токена - {}", date);
        return date;
    }

    /**
     * Извлечение всех данных из токена
     * @param token токен
     * @return данные
     */
    private Claims extractAllClaims(String token) {
        log.info("Извлечение всех данных из токена {}", token);
        Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                .getBody();
        log.info("Извлечение данных прошло успешно - {}", claims);
        return claims;
    }

    /**
     * Получение ключа для подписи токена
     * @return ключ
     */
    private Key getSigningKey() {
        log.info("Получение ключа для подписи токена");
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSigningKey));
        log.info("Ключ для подписи токена успешно получен");
        return key;
    }
}
