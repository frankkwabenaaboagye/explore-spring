package frank.app.socialmedia.controller;


import frank.app.socialmedia.utils.AuthResponse;
import frank.app.socialmedia.utils.RegistrationAuthRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody RegistrationAuthRequest regAuthReq) {
        return null;
    }
}
