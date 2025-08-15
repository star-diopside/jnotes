package jp.gr.java_conf.stardiopside.jnotes.web.controller;

import jakarta.validation.Valid;
import jp.gr.java_conf.stardiopside.jnotes.data.entity.FileInfo;
import jp.gr.java_conf.stardiopside.jnotes.service.FileService;
import jp.gr.java_conf.stardiopside.jnotes.web.form.FileCreateForm;
import jp.gr.java_conf.stardiopside.jnotes.web.form.FileEditForm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Controller
@RequestMapping("/files")
@Slf4j
public class FilesController {

    private final FileService fileService;
    private final MessageSource messageSource;

    public FilesController(FileService fileService, MessageSource messageSource) {
        this.fileService = fileService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("files/index")
                .addObject("files", fileService.list());
    }

    @GetMapping("/{id}")
    public ModelAndView show(@PathVariable Long id) {
        return fileService.findFileInfo(id)
                .map(fileInfo -> new ModelAndView("files/show")
                        .addObject("fileInfo", fileInfo))
                .orElseGet(() -> new ModelAndView("errors/404", HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}/data")
    public ResponseEntity<? extends Resource> download(@PathVariable Long id) {
        return fileService.findDownloadData(id)
                .map(downloadData -> ResponseEntity.ok()
                        .headers(headers -> headers.setContentDisposition(ContentDisposition
                                .attachment()
                                .filename(downloadData.fileName(), StandardCharsets.UTF_8)
                                .build()))
                        .contentType(parseMediaType(downloadData.contentType()))
                        .body(new ByteArrayResource(downloadData.data())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/create")
    public ModelAndView create() {
        return new ModelAndView("files/create")
                .addObject("form", new FileCreateForm());
    }

    @PostMapping
    public ModelAndView save(@Valid @ModelAttribute("form") FileCreateForm form, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes, Locale locale) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("files/create");
        }

        var fileInfo = fileService.save(form.getFile());
        var messages = new MessageSourceAccessor(messageSource, locale);
        redirectAttributes.addFlashAttribute("success", messages.getMessage("messages.success-create"));
        return new ModelAndView("redirect:/files/{id}")
                .addObject("id", fileInfo.getId());
    }

    @GetMapping("/{id}/edit")
    public ModelAndView edit(@PathVariable Long id) {
        return fileService.findFileInfoData(id)
                .map(fileInfoData -> new ModelAndView("files/edit")
                        .addObject("form", new FileEditForm(fileInfoData)))
                .orElseGet(() -> new ModelAndView("errors/404", HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ModelAndView update(@Valid @ModelAttribute("form") FileEditForm form, BindingResult bindingResult,
                               RedirectAttributes redirectAttributes, Locale locale) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("files/edit");
        }

        return fileService.update(form.getFile(), form.toFileInfo(), form.getFileDataVersion())
                .map(fileInfo -> {
                    var messages = new MessageSourceAccessor(messageSource, locale);
                    redirectAttributes.addFlashAttribute("success",
                            messages.getMessage("messages.success-update"));
                    return new ModelAndView("redirect:/files/{id}")
                            .addObject("id", fileInfo.getId());
                })
                .orElseGet(() -> new ModelAndView("errors/404", HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public String delete(@ModelAttribute FileInfo fileInfo, RedirectAttributes redirectAttributes, Locale locale) {
        fileService.delete(fileInfo);
        var messages = new MessageSourceAccessor(messageSource, locale);
        redirectAttributes.addFlashAttribute("success", messages.getMessage("messages.success-delete"));
        return "redirect:/files";
    }

    private static MediaType parseMediaType(@Nullable String mediaType) {
        try {
            return StringUtils.isEmpty(mediaType)
                    ? MediaType.APPLICATION_OCTET_STREAM
                    : MediaType.parseMediaType(mediaType);
        } catch (InvalidMediaTypeException e) {
            log.atDebug().setCause(e).log(e::getMessage);
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
