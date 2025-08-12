package jp.gr.java_conf.stardiopside.jnotes.config;

import jp.gr.java_conf.stardiopside.jnotes.data.repository.UserRepository;
import jp.gr.java_conf.stardiopside.jnotes.service.userdetails.DataSourceUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new DataSourceUserDetailsService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
