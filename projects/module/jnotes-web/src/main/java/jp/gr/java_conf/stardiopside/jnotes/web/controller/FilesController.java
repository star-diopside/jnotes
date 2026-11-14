package jp.gr.java_conf.stardiopside.jnotes.web.controller;

import jakarta.validation.Valid;
import jp.gr.java_conf.stardiopside.jnotes.data.entity.FileInfo;
import jp.gr.java_conf.stardiopside.jnotes.service.FileService;
import jp.gr.java_conf.stardiopside.jnotes.value.DownloadData;
import jp.gr.java_conf.stardiopside.jnotes.web.form.FileCreateForm;
import jp.gr.java_conf.stardiopside.jnotes.web.form.FileEditForm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.io.InputStreamResource;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Locale;
import java.util.Objects;

@Controller
@RequestMapping("/files")
@Slf4j
public class FilesController {

    private final FileService fileService;
    private final MessageSourceAccessor messages;

    public FilesController(FileService fileService, MessageSource messageSource) {
        this.fileService = fileService;
        this.messages = new MessageSourceAccessor(messageSource);
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
    public ResponseEntity<? extends Resource> downloadInline(@PathVariable Long id) {
        return download(id, ContentDisposition.inline());
    }

    @GetMapping(value = "/{id}/data", params = "download")
    public ResponseEntity<? extends Resource> downloadAttachment(@PathVariable Long id) {
        return download(id, ContentDisposition.attachment());
    }

    private ResponseEntity<? extends Resource> download(Long id, ContentDisposition.Builder builder) {
        return fileService.findDownloadData(id)
                .map(downloadData -> ResponseEntity.ok()
                        .headers(headers -> headers.setContentDisposition(builder
                                .filename(downloadData.fileName(), StandardCharsets.UTF_8)
                                .build()))
                        .contentType(parseMediaType(downloadData.contentType()))
                        .body(newDownloadDataResource(downloadData)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private static Resource newDownloadDataResource(DownloadData downloadData) {
        try {
            Path dataTempFile = downloadData.dataTempFile();
            log.debug("Download data file: {}", dataTempFile);
            InputStream inputStream = Files.newInputStream(dataTempFile, StandardOpenOption.DELETE_ON_CLOSE);
            return new InputStreamResource(inputStream) {
                @Override
                public long contentLength() throws IOException {
                    return Files.size(dataTempFile);
                }
            };
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
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

        var fileInfo = fileService.save(Objects.requireNonNull(form.getFile()));
        redirectAttributes.addFlashAttribute("success",
                messages.getMessage("messages.success-create", locale));
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

        return fileService.update(form.getFile(), Objects.requireNonNull(form.getId()), form.getUpdateFileName(),
                        form.getContentType(), Objects.requireNonNull(form.getFileInfoVersion()),
                        Objects.requireNonNull(form.getFileDataVersion()))
                .map(fileInfo -> {
                    redirectAttributes.addFlashAttribute("success",
                            messages.getMessage("messages.success-update", locale));
                    return new ModelAndView("redirect:/files/{id}")
                            .addObject("id", fileInfo.getId());
                })
                .orElseGet(() -> new ModelAndView("errors/404", HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public String delete(@ModelAttribute FileInfo fileInfo, RedirectAttributes redirectAttributes, Locale locale) {
        fileService.delete(fileInfo);
        redirectAttributes.addFlashAttribute("success",
                messages.getMessage("messages.success-delete", locale));
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
