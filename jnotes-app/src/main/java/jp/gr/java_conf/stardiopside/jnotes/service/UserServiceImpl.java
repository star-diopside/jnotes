package jp.gr.java_conf.stardiopside.jnotes.service;

import jakarta.transaction.Transactional;
import jp.gr.java_conf.stardiopside.jnotes.core.exception.BusinessException;
import jp.gr.java_conf.stardiopside.jnotes.core.exception.ResourceNotFoundException;
import jp.gr.java_conf.stardiopside.jnotes.core.message.ResultMessage;
import jp.gr.java_conf.stardiopside.jnotes.data.entity.Authority;
import jp.gr.java_conf.stardiopside.jnotes.data.entity.User;
import jp.gr.java_conf.stardiopside.jnotes.data.repository.AuthorityRepository;
import jp.gr.java_conf.stardiopside.jnotes.data.repository.UserRepository;
import jp.gr.java_conf.stardiopside.jnotes.value.UserData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           AuthorityRepository authorityRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
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
    public User create(String username, String rawPassword, boolean enabled, String... roles) {
        if (userRepository.existsByUsername(username)) {
            throw new BusinessException(new ResultMessage("messages.error-deleteCurrentUser"));
        }

        var user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .enabled(enabled)
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

    @Override
    @Transactional
    public Optional<User> update(UserData userData) {
        var existsUsername = userRepository.findByUsername(userData.username())
                .map(user -> !Objects.equals(user.getId(), userData.id()))
                .orElse(false);
        if (existsUsername) {
            throw new BusinessException(new ResultMessage("messages.error-alreadyExistsUser"));
        }

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var authUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(ResourceNotFoundException::new);

        if (Objects.equals(authUser.getId(), userData.id())) {
            if (!userData.enabled()) {
                throw new BusinessException(new ResultMessage("messages.error-disableCurrentUser"));
            }

            if (!userData.roles().contains("ADMIN")) {
                throw new BusinessException(new ResultMessage("messages.error-disableAdminCurrentUser"));
            }
        }

        return userRepository.findById(userData.id()).map(user -> {
            var newUser = new User();

            newUser.setId(userData.id());
            newUser.setUsername(userData.username());
            newUser.setPassword(StringUtils.isEmpty(userData.rawPassword())
                    ? user.getPassword()
                    : passwordEncoder.encode(userData.rawPassword()));
            newUser.setEnabled(userData.enabled());
            newUser.setVersion(userData.version());

            List<String> roleNames = userData.roles().stream()
                    .map(role -> "ROLE_" + role).toList();
            Map<Boolean, List<Authority>> groupingMap = user.getAuthorities().stream()
                    .collect(Collectors.groupingBy(a -> roleNames.contains(a.getAuthority())));
            List<Authority> deleteAuthorities = groupingMap.getOrDefault(false, List.of());
            List<Authority> existsAuthorities = groupingMap.getOrDefault(true, List.of());

            List<String> existsRoleNames = existsAuthorities.stream()
                    .map(Authority::getAuthority).toList();
            List<Authority> addAuthorities = roleNames.stream()
                    .filter(Predicate.not(existsRoleNames::contains))
                    .map(role -> Authority.builder()
                            .user(user)
                            .authority(role)
                            .build())
                    .toList();

            newUser.setAuthorities(Stream.concat(
                            existsAuthorities.stream(),
                            addAuthorities.stream())
                    .toList());
            authorityRepository.deleteAll(deleteAuthorities);
            return userRepository.save(newUser);
        });
    }

    @Override
    @Transactional
    public void delete(User user) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var authUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(ResourceNotFoundException::new);

        if (Objects.equals(authUser.getId(), user.getId())) {
            throw new BusinessException(new ResultMessage("messages.error-deleteCurrentUser"));
        }

        var targetUser = userRepository.findById(user.getId())
                .orElseThrow(ResourceNotFoundException::new);
        user.setAuthorities(targetUser.getAuthorities());
        userRepository.delete(user);
    }
}
