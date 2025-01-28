package frank.app.socialmedia.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AppUtils {

    @Value("${SOCIAL_MEDIA_APP_JWT_EXPIRATION_HOURS}")
    private int hoursToAdd;

    public Date getExpiration() {
        long currentTimeMillis = System.currentTimeMillis();
        long expirationTimeMillis = currentTimeMillis + (hoursToAdd * 60 * 60 * 1000L);
        return new Date(expirationTimeMillis);
    }

    public Date getCurrentDate(){
        return new Date(System.currentTimeMillis());
    }
}
