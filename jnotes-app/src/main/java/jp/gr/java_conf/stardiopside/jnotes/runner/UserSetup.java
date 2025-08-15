package jp.gr.java_conf.stardiopside.jnotes.runner;

import jakarta.transaction.Transactional;
import jp.gr.java_conf.stardiopside.jnotes.data.repository.UserRepository;
import jp.gr.java_conf.stardiopside.jnotes.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserSetup implements CommandLineRunner {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserSetup(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            userService.create("admin", "admin", true, "ADMIN", "USER");
        }
    }
}
