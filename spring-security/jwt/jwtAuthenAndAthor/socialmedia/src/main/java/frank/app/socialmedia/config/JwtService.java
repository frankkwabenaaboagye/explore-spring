package frank.app.socialmedia.config;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.Date;
import java.util.function.Function;

public interface JwtService {

    Claims extractAllClaims(String theJwtToken);

    <T> T extractClaimWithResolver(String theToken, Function<Claims, T> claimsResolverFunction);

    <T> T extractClaimByKey(String theJwtToken, String theClaimKey, Class<T> claimType);

    String generateJwt(UserDetails userDetails, Map<String, Object> extraClaims);

    String generateJwt(UserDetails userDetails);

    boolean isJwtValid(String theJwt, UserDetails userDetails);

    String extractUsername(String theJwtToken);

    Date extractExpiration(String theJwtToken);
}