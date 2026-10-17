package jp.gr.java_conf.stardiopside.jnotes.batch.item;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemStreamException;
import org.springframework.batch.infrastructure.item.ItemStreamReader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

public class RegisterFileItemReader implements ItemStreamReader<Path> {

    private final Path basePath;
    private @Nullable Stream<Path> pathStream;
    private @Nullable Iterator<Path> pathIterator;

    public RegisterFileItemReader(Path basePath) {
        this.basePath = Objects.requireNonNull(basePath);
    }

    @Override
    public @Nullable Path read() throws Exception {
        if (pathStream == null) {
            pathStream = Files.find(basePath, Integer.MAX_VALUE, (_, attr) -> attr.isRegularFile());
        }

        if (pathIterator == null) {
            pathIterator = pathStream.iterator();
        }

        if (pathIterator.hasNext()) {
            return pathIterator.next();
        } else {
            return null;
        }
    }

    @Override
    public void close() throws ItemStreamException {
        if (pathStream != null) {
            pathStream.close();
        }
    }
}
