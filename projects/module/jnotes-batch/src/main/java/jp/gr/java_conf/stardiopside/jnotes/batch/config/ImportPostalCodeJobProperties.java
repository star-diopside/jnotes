package jp.gr.java_conf.stardiopside.jnotes.batch.config;

import org.jspecify.annotations.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.core.io.Resource;

import java.nio.charset.Charset;
import java.nio.file.Path;

@ConfigurationProperties("application.batch.import-postal-code-job")
public record ImportPostalCodeJobProperties(
        @DefaultValue Postcode postcode,
        @DefaultValue TempDir tempDir,
        @DefaultValue("true") boolean resetPostalCodeIdSequence) {

    public record Postcode(
            Resource resource,
            @DefaultValue("UTF-8") Charset zipEntryCharset,
            @DefaultValue("UTF-8") Charset contentCharset) {
    }

    public record TempDir(
            @Nullable Path path,
            @Nullable String prefix,
            @DefaultValue("true") boolean deleteOnExit) {
    }
}
