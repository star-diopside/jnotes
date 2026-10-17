package jp.gr.java_conf.stardiopside.jnotes.batch.tasklet;

import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;

public class ClearPostalCodeTasklet implements Tasklet {

    private final JdbcTemplate jdbcTemplate;

    @Setter
    private boolean resetSequence;

    public ClearPostalCodeTasklet(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        jdbcTemplate.execute("truncate table postal_codes");
        if (resetSequence) {
            jdbcTemplate.execute("select setval('postal_codes_id_seq', 1, false)");
        }
        return RepeatStatus.FINISHED;
    }
}
