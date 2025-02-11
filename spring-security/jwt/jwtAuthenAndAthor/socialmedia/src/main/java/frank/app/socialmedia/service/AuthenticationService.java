package frank.app.socialmedia.service;

import frank.app.socialmedia.appUser.SocialMediaUser;
import frank.app.socialmedia.config.JwtService;
import frank.app.socialmedia.repository.SocialMediaUserRepository;
import frank.app.socialmedia.utils.AuthenticationRequest;
import frank.app.socialmedia.utils.AuthenticationResponse;
import frank.app.socialmedia.utils.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static frank.app.socialmedia.appUser.Role.NORMALUSER;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final SocialMediaUserRepository socialMediaUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegistrationRequest registrationRequest) {

        checkIfUserExists(registrationRequest.getEmail());

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
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );

        SocialMediaUser user = socialMediaUserRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateJwt(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    private void checkIfUserExists(String email) {
        socialMediaUserRepository.findByEmail(email).ifPresent(user -> {
            throw new IllegalStateException("User with email " + email + " already exists");
        });

    }
}
