package frank.app.socialmedia.config;

import frank.app.socialmedia.repository.SocialMediaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SocialMediaUserDetailsService  implements UserDetailsService {

    private final SocialMediaUserRepository socialMediaUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return socialMediaUserRepository
                .findByEmail(username)
                .orElseThrow(
                        ()-> new UsernameNotFoundException( username + " no found")
                );

    }
}
