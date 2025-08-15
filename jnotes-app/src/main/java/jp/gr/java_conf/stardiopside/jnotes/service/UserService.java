package jp.gr.java_conf.stardiopside.jnotes.service;

import jp.gr.java_conf.stardiopside.jnotes.data.entity.User;
import jp.gr.java_conf.stardiopside.jnotes.value.UserData;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> list();

    Optional<User> find(Long id);

    User create(String username, String rawPassword, boolean enabled, String... roles);

    Optional<User> update(UserData userData);

    void delete(User user);

}
