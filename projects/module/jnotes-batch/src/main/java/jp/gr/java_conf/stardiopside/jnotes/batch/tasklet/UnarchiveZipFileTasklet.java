package jp.gr.java_conf.stardiopside.jnotes.batch.tasklet;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.core.io.Resource;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

public class UnarchiveZipFileTasklet implements Tasklet {

    private final Resource resource;
    private final Charset charset;
    private final Path unarchiveDirectory;

    /// コンストラクタ
    ///
    /// @param resource           ZIPファイルリソース
    /// @param charset            ファイル名のデコードに使用する文字セット
    /// @param unarchiveDirectory ファイル解凍先ディレクトリ
    public UnarchiveZipFileTasklet(Resource resource, Charset charset, Path unarchiveDirectory) {
        this.resource = Objects.requireNonNull(resource);
        this.charset = Objects.requireNonNull(charset);
        this.unarchiveDirectory = unarchiveDirectory.normalize();
    }

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try (var zis = new ZipInputStream(resource.getInputStream(), charset)) {
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                Path path = unarchiveDirectory.resolve(ze.getName()).normalize();
                if (!path.startsWith(unarchiveDirectory)) {
                    throw new ZipException("Illegal entry name: " + ze.getName());
                }

                if (ze.isDirectory()) {
                    Files.createDirectories(path);
                } else {
                    Path parent = path.getParent();
                    if (Files.notExists(parent)) {
                        Files.createDirectories(parent);
                    }
                    Files.copy(zis, path);
                }
            }
        }
        return RepeatStatus.FINISHED;
    }
}
