package jp.gr.java_conf.stardiopside.jnotes.web.controller;

import jakarta.validation.Valid;
import jp.gr.java_conf.stardiopside.jnotes.core.exception.BusinessException;
import jp.gr.java_conf.stardiopside.jnotes.data.entity.User;
import jp.gr.java_conf.stardiopside.jnotes.service.UserService;
import jp.gr.java_conf.stardiopside.jnotes.web.form.UserCreateForm;
import jp.gr.java_conf.stardiopside.jnotes.web.form.UserEditForm;
import jp.gr.java_conf.stardiopside.jnotes.web.form.UserRole;
import jp.gr.java_conf.stardiopside.jnotes.web.validation.PasswordMatchValidator;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;
    private final PasswordMatchValidator passwordMatchValidator;
    private final MessageSourceAccessor messages;

    public UsersController(UserService userService,
                           PasswordMatchValidator passwordMatchValidator,
                           MessageSource messageSource) {
        this.userService = userService;
        this.passwordMatchValidator = passwordMatchValidator;
        this.messages = new MessageSourceAccessor(messageSource);
    }

    @ModelAttribute("createForm")
    public UserCreateForm newUserCreateForm() {
        var form = new UserCreateForm();
        form.setEnabled(true);
        form.setRoles(new ArrayList<>(List.of(UserRole.USER)));
        return form;
    }

    @InitBinder({"createForm", "editForm"})
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(passwordMatchValidator);
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
    public String create(@ModelAttribute("createForm") UserCreateForm form) {
        return "users/create";
    }

    @PostMapping
    public ModelAndView save(@Valid @ModelAttribute("createForm") UserCreateForm form,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes,
                             Locale locale) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("users/create");
        }

        try {
            var user = userService.create(form.getUsername(), form.getPassword(), form.getEnabled(),
                    form.getRoles().stream().map(Enum::name).toArray(String[]::new));
            redirectAttributes.addFlashAttribute("success",
                    messages.getMessage("messages.success-create", locale));
            return new ModelAndView("redirect:/users/{id}")
                    .addObject("id", user.getId());
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error",
                    e.getResultMessage().getMessage(messages, locale));
            redirectAttributes.addFlashAttribute("createForm", form);
            return new ModelAndView("redirect:/users/create");
        }
    }

    @GetMapping("/{id}/edit")
    public ModelAndView edit(@ModelAttribute("editForm") UserEditForm form) {
        if (form.getVersion() != null) {
            return new ModelAndView("users/edit");
        }
        return userService.find(form.getId())
                .map(user -> new ModelAndView("users/edit")
                        .addObject("editForm", new UserEditForm(user)))
                .orElseGet(() -> new ModelAndView("errors/404", HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ModelAndView update(@Valid @ModelAttribute("editForm") UserEditForm form,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes,
                               Locale locale) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("users/edit");
        }

        try {
            return userService.update(form.toUserData())
                    .map(user -> {
                        redirectAttributes.addFlashAttribute("success",
                                messages.getMessage("messages.success-update", locale));
                        return new ModelAndView("redirect:/users/{id}")
                                .addObject("id", user.getId());
                    })
                    .orElseGet(() -> new ModelAndView("errors/404", HttpStatus.NOT_FOUND));
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error",
                    e.getResultMessage().getMessage(messages, locale));
            redirectAttributes.addFlashAttribute("editForm", form);
            return new ModelAndView("redirect:/users/{id}/edit");
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@ModelAttribute User user, RedirectAttributes redirectAttributes, Locale locale) {
        try {
            userService.delete(user);
            redirectAttributes.addFlashAttribute("success",
                    messages.getMessage("messages.success-delete", locale));
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error",
                    e.getResultMessage().getMessage(messages, locale));
        }
        return "redirect:/users";
    }
}
