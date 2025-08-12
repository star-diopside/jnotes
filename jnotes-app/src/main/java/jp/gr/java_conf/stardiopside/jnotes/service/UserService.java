package jp.gr.java_conf.stardiopside.jnotes.service;

import jp.gr.java_conf.stardiopside.jnotes.data.entity.User;

public interface UserService {

    User create(String username, String rawPassword, String... roles);

}
