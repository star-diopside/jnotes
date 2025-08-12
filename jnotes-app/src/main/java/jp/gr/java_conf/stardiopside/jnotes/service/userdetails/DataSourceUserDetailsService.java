package jp.gr.java_conf.stardiopside.jnotes.service.userdetails;

import jakarta.transaction.Transactional;
import jp.gr.java_conf.stardiopside.jnotes.data.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DataSourceUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public DataSourceUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));

        if (user.getAuthorities().isEmpty()) {
            throw new UsernameNotFoundException("User " + username + " has no GrantedAuthority");
        }

        return new User(user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true,
                user.getAuthorities().stream()
                        .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                        .toList());
    }
}
