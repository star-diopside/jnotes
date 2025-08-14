package jp.gr.java_conf.stardiopside.jnotes.web.controller;

import jp.gr.java_conf.stardiopside.jnotes.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("users/index")
                .addObject("users", userService.list());
    }

    @GetMapping("/{id}")
    public ModelAndView show(@PathVariable Long id) {
        return userService.find(id)
                .map(user -> new ModelAndView("users/show")
                        .addObject("user", user))
                .orElseGet(() -> new ModelAndView("errors/404", HttpStatus.NOT_FOUND));
    }

    @GetMapping("/create")
    public String create() {
        throw new UnsupportedOperationException();
    }

    @PostMapping
    public String save() {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id) {
        throw new UnsupportedOperationException();
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id) {
        throw new UnsupportedOperationException();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        throw new UnsupportedOperationException();
    }
}
