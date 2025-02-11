package frank.app.socialmedia.service;

import frank.app.socialmedia.appUser.Role;
import frank.app.socialmedia.appUser.SocialMediaUser;
import frank.app.socialmedia.config.JwtService;
import frank.app.socialmedia.repository.SocialMediaUserRepository;
import frank.app.socialmedia.utils.AuthenticationRequest;
import frank.app.socialmedia.utils.AuthenticationResponse;
import frank.app.socialmedia.utils.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static frank.app.socialmedia.appUser.Role.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final SocialMediaUserRepository socialMediaUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticationResponse register(RegistrationRequest registrationRequest) {

        SocialMediaUser user = SocialMediaUser.builder()
                .firstname(registrationRequest.getFirstname())
                .lastname(registrationRequest.getLastname())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .role(NORMALUSER)
                .build();

        socialMediaUserRepository.save(user);
        var jwtToken = jwtService.generateJwt(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {

    }

}
