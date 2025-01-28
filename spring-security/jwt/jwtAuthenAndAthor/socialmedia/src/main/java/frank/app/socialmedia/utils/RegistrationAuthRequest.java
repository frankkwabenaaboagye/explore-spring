package frank.app.socialmedia.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationAuthRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
}
