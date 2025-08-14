package jp.gr.java_conf.stardiopside.jnotes.service;

import jakarta.transaction.Transactional;
import jp.gr.java_conf.stardiopside.jnotes.data.entity.Authority;
import jp.gr.java_conf.stardiopside.jnotes.data.entity.User;
import jp.gr.java_conf.stardiopside.jnotes.data.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public List<User> list() {
        return userRepository.findAll(Sort.by("id").ascending());
    }

    @Override
    @Transactional
    public Optional<User> find(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public User create(String username, String rawPassword, String... roles) {
        var user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .enabled(true)
                .build();
        var authorities = Arrays.stream(roles)
                .map(role -> Authority.builder()
                        .user(user)
                        .authority("ROLE_" + role)
                        .build())
                .toList();
        user.setAuthorities(authorities);
        userRepository.save(user);
        return user;
    }
}
