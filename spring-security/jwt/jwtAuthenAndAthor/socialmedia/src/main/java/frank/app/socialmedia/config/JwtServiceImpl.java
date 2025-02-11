package frank.app.socialmedia.config;

import frank.app.socialmedia.utils.AppUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService{

    @Value("${SOCIAL_MEDIA_APP_SECRET_KEY}")
    private String SOCIAL_MEDIA_APP_SECRET_KEY;

    @Value("${SOCIAL_MEDIA_APP_ALGORITHM}")
    private String SOCIAL_MEDIA_APP_ALGORITHM;  // can be HmacSHA1, HmacSHA224, HmacSHA256, HmacSHA384 , HmacSHA512, e.t.c

    private final AppUtils appUtils;

    @Override
    public Claims extractAllClaims(String theJwtToken){
        return Jwts.parser()
                .verifyWith(getTheSignInKey())
                .build()
                .parseSignedClaims(theJwtToken)
                .getPayload();
    }

    private SecretKey getTheSignInKey() {

        System.out.println("algorithm is  => " + SOCIAL_MEDIA_APP_ALGORITHM);
        System.out.println("secree key is => " + SOCIAL_MEDIA_APP_SECRET_KEY );

        try {
            Mac mac = Mac.getInstance(SOCIAL_MEDIA_APP_ALGORITHM);
            mac.init(
                    new SecretKeySpec(
                            SOCIAL_MEDIA_APP_SECRET_KEY.getBytes(StandardCharsets.UTF_8), SOCIAL_MEDIA_APP_ALGORITHM
                    )
            );
            return new SecretKeySpec(mac.doFinal(), SOCIAL_MEDIA_APP_ALGORITHM);
        } catch (NoSuchAlgorithmException e){
            throw new RuntimeException("Error generating signing key", e);

        } catch (InvalidKeyException e) {
            throw new RuntimeException("Error associated with the key", e);
        }
    }

    @Override
    public <T> T extractClaimWithResolver(String theToken, Function<Claims, T> claimsResolverFunction){
        var claims = extractAllClaims(theToken);
        return claimsResolverFunction.apply(claims);
    }

    @Override
    public <T> T extractClaimByKey(String theJwtToken, String theClaimKey, Class<T> claimType){
        var claims = extractAllClaims(theJwtToken);
        return claims.get(theClaimKey, claimType);
    }


    @Override
    public String generateJwt(UserDetails userDetails, Map<String, Object> extraClaims){

        Date issuedAt = appUtils.getCurrentDate();
        Date expiration = appUtils.getExpiration();

        return Jwts.builder()
                .issuedAt(issuedAt)
                .expiration(expiration)
                .subject(userDetails.getUsername())
                .claims(extraClaims)
                .signWith(getTheSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    @Override
    public String generateJwt(UserDetails userDetails){
        return generateJwt(userDetails, Collections.emptyMap());
    }

    // validating if a token belongs to a user
    @Override
    public boolean isJwtValid(String theJwt, UserDetails userDetails){
        try {

            String tokenUsername = extractUsername(theJwt);
            boolean isUsernameValid = tokenUsername.equals(userDetails.getUsername());

            Date tokenExpiration = extractExpiration(theJwt);
            boolean isTokenExpired = tokenExpiration.before(appUtils.getCurrentDate());

            return isUsernameValid && !isTokenExpired;
        } catch (Exception e){
            return false; // will change this
        }
    }

    public String extractUsername(String theJwtToken){
        return extractClaimByKey(theJwtToken, Claims.SUBJECT, String.class);
    }

    public Date extractExpiration(String theJwtToken){
        return extractClaimByKey(theJwtToken, Claims.EXPIRATION, Date.class);
    }







}
