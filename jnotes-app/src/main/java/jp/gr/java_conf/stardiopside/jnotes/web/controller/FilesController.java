package jp.gr.java_conf.stardiopside.jnotes.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/files")
public class FilesController {

    @GetMapping
    public String index() {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id) {
        throw new UnsupportedOperationException();
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
