package jp.gr.java_conf.stardiopside.jnotes.batch.config;

import jakarta.persistence.EntityManagerFactory;
import jp.gr.java_conf.stardiopside.jnotes.batch.item.RegisterFileItemConverter;
import jp.gr.java_conf.stardiopside.jnotes.batch.item.RegisterFileItemReader;
import jp.gr.java_conf.stardiopside.jnotes.data.entity.FileInfo;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.DefaultJobParametersValidator;
import org.springframework.batch.core.job.parameters.JobParametersValidator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.nio.file.Path;

@Configuration(proxyBeanMethods = false)
public class RegisterFileJobConfig {

    @Bean
    public Job registerFileJob(JobRepository jobRepository,
                               JobParametersValidator registerFileJobParametersValidator,
                               Step registerFileStep) {
        return new JobBuilder("registerFileJob", jobRepository)
                .validator(registerFileJobParametersValidator)
                .start(registerFileStep)
                .preventRestart()
                .build();
    }

    @Bean
    public JobParametersValidator registerFileJobParametersValidator() {
        var validator = new DefaultJobParametersValidator();
        validator.setRequiredKeys(new String[]{"path"});
        return validator;
    }

    @Bean
    public Step registerFileStep(JobRepository jobRepository,
                                 PlatformTransactionManager transactionManager,
                                 RegisterFileItemReader registerFileItemReader,
                                 RegisterFileItemConverter registerFileItemConverter,
                                 ItemWriter<FileInfo> registerFileItemWriter) {
        return new StepBuilder("registerFileStep", jobRepository)
                .<Path, FileInfo>chunk(10)
                .transactionManager(transactionManager)
                .reader(registerFileItemReader)
                .processor(registerFileItemConverter)
                .writer(registerFileItemWriter)
                .build();
    }

    @Bean
    @StepScope
    public RegisterFileItemReader registerFileItemReader(
            @Value("#{jobParameters['path']}") Path path) {
        return new RegisterFileItemReader(path);
    }

    @Bean
    public RegisterFileItemConverter registerFileItemConverter() {
        return new RegisterFileItemConverter();
    }

    @Bean
    public JpaItemWriter<FileInfo> registerFileItemWriter(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriterBuilder<FileInfo>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true)
                .build();
    }
}
