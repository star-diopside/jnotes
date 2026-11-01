package jp.gr.java_conf.stardiopside.jnotes.batch.config;

import jp.gr.java_conf.stardiopside.jnotes.batch.item.DirectoryFileItemReader;
import jp.gr.java_conf.stardiopside.jnotes.batch.item.RegisterFileItemConverter;
import jp.gr.java_conf.stardiopside.jnotes.data.entity.FileInfo;
import jp.gr.java_conf.stardiopside.jnotes.data.repository.FileInfoRepository;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.DefaultJobParametersValidator;
import org.springframework.batch.core.job.parameters.JobParametersValidator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.data.RepositoryItemWriter;
import org.springframework.batch.infrastructure.item.data.builder.RepositoryItemWriterBuilder;
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
        return new JobBuilder(jobRepository)
                .validator(registerFileJobParametersValidator)
                .start(registerFileStep)
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
                                 DirectoryFileItemReader registerFileItemReader,
                                 RegisterFileItemConverter registerFileItemConverter,
                                 ItemWriter<FileInfo> registerFileItemWriter) {
        return new StepBuilder(jobRepository)
                .<Path, FileInfo>chunk(10)
                .transactionManager(transactionManager)
                .reader(registerFileItemReader)
                .processor(registerFileItemConverter)
                .writer(registerFileItemWriter)
                .build();
    }

    @Bean
    @StepScope
    public DirectoryFileItemReader registerFileItemReader(
            @Value("#{jobParameters['path']}") Path path) {
        return new DirectoryFileItemReader(path);
    }

    @Bean
    public RegisterFileItemConverter registerFileItemConverter() {
        return new RegisterFileItemConverter();
    }

    @Bean
    public RepositoryItemWriter<FileInfo> registerFileItemWriter(FileInfoRepository fileInfoRepository) {
        return new RepositoryItemWriterBuilder<FileInfo>()
                .repository(fileInfoRepository)
                .build();
    }
}
