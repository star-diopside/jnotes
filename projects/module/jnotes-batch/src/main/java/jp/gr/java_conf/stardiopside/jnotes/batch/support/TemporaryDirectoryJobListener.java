package jp.gr.java_conf.stardiopside.jnotes.batch.support;

import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

/// 一時ディレクトリを生成する [JobExecutionListener]
public class TemporaryDirectoryJobListener implements JobExecutionListener {

    /// 生成した一時ディレクトリ名を ExecutionContext に格納するキー
    private final String key;

    /// 一時ディレクトリ名の接頭辞文字列
    @Setter
    @Nullable
    private String prefix;

    /// 一時ディレクトリを生成するディレクトリ
    @Setter
    @Nullable
    private Path directory;

    /// 終了時に一時ディレクトリを削除するかを示すフラグ
    @Setter
    private boolean deleteOnExit = true;

    /// コンストラクタ
    ///
    /// @param key 生成した一時ディレクトリ名を ExecutionContext に格納するキー
    public TemporaryDirectoryJobListener(String key) {
        this.key = Objects.requireNonNull(key);
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        try {
            Path tempDir;
            if (directory == null) {
                tempDir = Files.createTempDirectory(prefix);
            } else {
                if (Files.notExists(directory)) {
                    Files.createDirectories(directory);
                }
                tempDir = Files.createTempDirectory(directory, prefix);
            }
            jobExecution.getExecutionContext().putString(key, tempDir.toString());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (!deleteOnExit) {
            return;
        }

        Path tempDir = Path.of(jobExecution.getExecutionContext().getString(key));
        try {
            Files.walkFileTree(tempDir, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, @Nullable IOException exc) throws IOException {
                    if (exc != null) {
                        throw exc;
                    }
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
