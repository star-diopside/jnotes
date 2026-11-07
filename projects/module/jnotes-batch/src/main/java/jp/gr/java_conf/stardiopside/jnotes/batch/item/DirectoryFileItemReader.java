package jp.gr.java_conf.stardiopside.jnotes.batch.item;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.ItemStreamException;
import org.springframework.batch.infrastructure.item.ItemStreamReader;
import org.springframework.batch.infrastructure.item.ItemStreamSupport;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DirectoryFileItemReader extends ItemStreamSupport implements ItemStreamReader<Path> {

    private static final String READ_ITEM_NAME = "read.item.name";
    private final Path basePath;
    private @Nullable Stream<Path> pathStream;
    private @Nullable Iterator<Path> pathIterator;
    private @Nullable Path currentItemName;

    public DirectoryFileItemReader(Path basePath) {
        this.basePath = Objects.requireNonNull(basePath);
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        if (pathStream != null) {
            throw new IllegalStateException("pathStream must be null");
        }

        try {
            pathStream = findFiles(basePath);
        } catch (IOException e) {
            throw new ItemStreamException(e);
        }

        if (executionContext.containsKey(getExecutionContextKey(READ_ITEM_NAME))) {
            currentItemName = Path.of(executionContext.getString(getExecutionContextKey(READ_ITEM_NAME)));
            pathStream = pathStream.dropWhile(Predicate.isEqual(currentItemName).negate()).skip(1);
        }

        pathIterator = pathStream.iterator();
    }

    @SuppressWarnings("StreamResourceLeak")
    private static Stream<Path> findFiles(Path basePath) throws IOException {
        return Files.find(basePath, Integer.MAX_VALUE, (_, attr) -> attr.isRegularFile());
    }

    @Override
    public @Nullable Path read() throws Exception {
        if (pathIterator == null) {
            throw new IllegalStateException("pathIterator must not be null");
        }

        currentItemName = pathIterator.hasNext() ? pathIterator.next() : null;
        return currentItemName;
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        if (currentItemName == null) {
            executionContext.remove(getExecutionContextKey(READ_ITEM_NAME));
        } else {
            executionContext.putString(getExecutionContextKey(READ_ITEM_NAME), currentItemName.toString());
        }
    }

    @Override
    public void close() throws ItemStreamException {
        if (pathStream != null) {
            pathStream.close();
            pathStream = null;
            pathIterator = null;
            currentItemName = null;
        }
    }
}
