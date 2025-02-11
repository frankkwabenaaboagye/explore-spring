package frank.app.socialmedia.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/social-media")
public class SocialMediaController {

    @GetMapping("/homepage")
    public ResponseEntity<String> homepage(){
        return ResponseEntity.ok("Welcome to Social Media App");
    }

    @GetMapping("/profile")
    public ResponseEntity<String> profile(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok("Welcome to your profile " + userDetails.getUsername());
    }
}
