package jp.gr.java_conf.stardiopside.jnotes.web.controller;

import jakarta.validation.Valid;
import jp.gr.java_conf.stardiopside.jnotes.data.entity.Todo;
import jp.gr.java_conf.stardiopside.jnotes.service.TodoService;
import jp.gr.java_conf.stardiopside.jnotes.value.Around;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;
import java.util.OptionalLong;

@Controller
@RequestMapping("/todos")
public class TodosController {

    private final TodoService todoService;
    private final MessageSource messageSource;

    public TodosController(TodoService todoService, MessageSource messageSource) {
        this.todoService = todoService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("todos/index")
                .addObject("todos", todoService.list());
    }

    @GetMapping("/{id}")
    public ModelAndView show(@PathVariable Long id) {
        return show(id, "todos/show");
    }

    @GetMapping("/create")
    public ModelAndView create() {
        return new ModelAndView("todos/create")
                .addObject(new Todo());
    }

    @PostMapping
    public ModelAndView save(@Valid @ModelAttribute Todo todo, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes, Locale locale) {
        return save(todo, bindingResult, redirectAttributes, locale,
                "todos/create", "messages.success-create");
    }

    @GetMapping("/{id}/edit")
    public ModelAndView edit(@PathVariable Long id) {
        return show(id, "todos/edit");
    }

    @PutMapping("/{id}")
    public ModelAndView update(@Valid @ModelAttribute Todo todo, BindingResult bindingResult,
                               RedirectAttributes redirectAttributes, Locale locale) {
        return save(todo, bindingResult, redirectAttributes, locale,
                "todos/edit", "messages.success-update");
    }

    @DeleteMapping("/{id}")
    public String delete(@ModelAttribute Todo todo, RedirectAttributes redirectAttributes, Locale locale) {
        todoService.delete(todo);
        var messages = new MessageSourceAccessor(messageSource, locale);
        redirectAttributes.addFlashAttribute("success", messages.getMessage("messages.success-delete"));
        return "redirect:/todos";
    }

    private ModelAndView show(Long id, String viewName) {
        var node = todoService.findWithAround(id);
        return node.item()
                .map(todo -> new ModelAndView(viewName)
                        .addObject("todo", todo)
                        .addObject("prev", node.prev())
                        .addObject("next", node.next()))
                .orElseGet(() -> new ModelAndView("errors/404", HttpStatus.NOT_FOUND));
    }

    private ModelAndView save(Todo todo, BindingResult bindingResult,
                              RedirectAttributes redirectAttributes, Locale locale,
                              String errorViewName, String successMessage) {
        if (bindingResult.hasErrors()) {
            var around = todo.getId() == null
                    ? new Around<>(OptionalLong.empty(), OptionalLong.empty())
                    : todoService.findAround(todo.getId());
            return new ModelAndView(errorViewName)
                    .addObject("todo", todo)
                    .addObject("prev", around.prev())
                    .addObject("next", around.next());
        }

        todo = todoService.save(todo);
        var messages = new MessageSourceAccessor(messageSource, locale);
        redirectAttributes.addFlashAttribute("success", messages.getMessage(successMessage));
        return new ModelAndView("redirect:/todos/{id}")
                .addObject("id", todo.getId());
    }
}
