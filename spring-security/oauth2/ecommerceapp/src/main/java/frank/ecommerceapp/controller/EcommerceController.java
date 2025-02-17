package frank.ecommerceapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EcommerceController {

    @GetMapping("/homepage")
    public ResponseEntity<String> homepage() {
        return ResponseEntity.ok("Welcome to the homepage");
    }

    @GetMapping("/user-info")
    public ResponseEntity<String> userInformation(Authentication authentication) {
        OAuth2AuthenticatedPrincipal principal = (OAuth2AuthenticatedPrincipal) authentication.getPrincipal();
        var userEmail = principal.getAttribute("email");
        System.out.println("Principal email: " + userEmail);
        return ResponseEntity.ok("Welcome here!!!!" + userEmail );
    }

    @GetMapping("/the-user")
    public ResponseEntity<String> theUser(Authentication authentication) {
        OAuth2User auth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("OAuth2User: " + auth2User);
        return ResponseEntity.ok("Auth2 user");
    }

}
