package jp.gr.java_conf.stardiopside.jnotes.batch.config;

import jakarta.persistence.EntityManagerFactory;
import jp.gr.java_conf.stardiopside.jnotes.batch.item.PostalCodeConverter;
import jp.gr.java_conf.stardiopside.jnotes.batch.support.TemporaryDirectoryJobListener;
import jp.gr.java_conf.stardiopside.jnotes.batch.tasklet.ClearPostalCodeTasklet;
import jp.gr.java_conf.stardiopside.jnotes.batch.tasklet.UnarchiveZipFileTasklet;
import jp.gr.java_conf.stardiopside.jnotes.batch.value.PostalCodeAddress;
import jp.gr.java_conf.stardiopside.jnotes.data.entity.PostalCode;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.MultiResourceItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.batch.infrastructure.item.support.CompositeItemProcessor;
import org.springframework.batch.infrastructure.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.batch.infrastructure.item.validator.BeanValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import java.nio.file.Path;

@Configuration(proxyBeanMethods = false)
public class ImportPostalCodeJobConfig {

    private static final String TEMP_DIR_KEY = "postcode.tempDir";

    @Bean
    public Job importPostalCodeJob(JobRepository jobRepository,
                                   TemporaryDirectoryJobListener temporaryDirectoryJobListener,
                                   Step unarchiveStep,
                                   Step clearDataStep,
                                   Step importStep) {
        return new JobBuilder("importPostalCodeJob", jobRepository)
                .listener(temporaryDirectoryJobListener)
                .start(unarchiveStep)
                .next(clearDataStep)
                .next(importStep)
                .build();
    }

    @Bean
    public TemporaryDirectoryJobListener temporaryDirectoryJobListener(ImportPostalCodeJobProperties properties) {
        var listener = new TemporaryDirectoryJobListener(TEMP_DIR_KEY);
        listener.setPrefix(properties.tempDir().prefix());
        listener.setDirectory(properties.tempDir().path());
        listener.setDeleteOnExit(properties.tempDir().deleteOnExit());
        return listener;
    }

    @Bean
    public Step unarchiveStep(JobRepository jobRepository,
                              UnarchiveZipFileTasklet unarchiveZipFileTasklet) {
        return new StepBuilder("unarchiveStep", jobRepository)
                .tasklet(unarchiveZipFileTasklet)
                .build();
    }

    @Bean
    @StepScope
    public UnarchiveZipFileTasklet unarchiveZipFileTasklet(
            ImportPostalCodeJobProperties properties,
            @Value("#{jobExecutionContext['" + TEMP_DIR_KEY + "']}") Path tempDir) {
        return new UnarchiveZipFileTasklet(
                properties.postcode().resource(),
                properties.postcode().zipEntryCharset(),
                tempDir);
    }

    @Bean
    public Step clearDataStep(JobRepository jobRepository,
                              PlatformTransactionManager transactionManager,
                              ClearPostalCodeTasklet clearDataTasklet) {
        return new StepBuilder("clearDataStep", jobRepository)
                .tasklet(clearDataTasklet, transactionManager)
                .build();
    }

    @Bean
    public ClearPostalCodeTasklet clearDataTasklet(
            JdbcTemplate jdbcTemplate,
            ImportPostalCodeJobProperties properties) {
        var tasklet = new ClearPostalCodeTasklet(jdbcTemplate);
        tasklet.setResetSequence(properties.resetPostalCodeIdSequence());
        return tasklet;
    }

    @Bean
    public Step importStep(JobRepository jobRepository,
                           PlatformTransactionManager transactionManager,
                           MultiResourceItemReader<PostalCodeAddress> multiPostalCodeAddressReader,
                           ItemProcessor<PostalCodeAddress, PostalCode> postalCodeProcessor,
                           ItemWriter<PostalCode> postalCodeWriter) {
        return new StepBuilder("importStep", jobRepository)
                .<PostalCodeAddress, PostalCode>chunk(100)
                .transactionManager(transactionManager)
                .reader(multiPostalCodeAddressReader)
                .processor(postalCodeProcessor)
                .writer(postalCodeWriter)
                .build();
    }

    @Bean
    @StepScope
    public MultiResourceItemReader<PostalCodeAddress> multiPostalCodeAddressReader(
            FlatFileItemReader<PostalCodeAddress> postalCodeAddressReader,
            @Value("file:#{jobExecutionContext['" + TEMP_DIR_KEY + "']}/**/*.csv") Resource[] resources) {
        return new MultiResourceItemReaderBuilder<PostalCodeAddress>()
                .name("multiPostalCodeAddressReader")
                .resources(resources)
                .delegate(postalCodeAddressReader)
                .build();
    }

    @Bean
    public FlatFileItemReader<PostalCodeAddress> postalCodeAddressReader(ImportPostalCodeJobProperties properties) {
        return new FlatFileItemReaderBuilder<PostalCodeAddress>()
                .name("postalCodeAddressReader")
                .encoding(properties.postcode().contentCharset().name())
                .recordSeparatorPolicy(new DefaultRecordSeparatorPolicy())
                .delimited()
                .names("localGovernmentCode",
                        "oldPostalCode",
                        "postalCode",
                        "kanaPrefectureName",
                        "kanaMunicipalityName",
                        "kanaAreaName",
                        "kanjiPrefectureName",
                        "kanjiMunicipalityName",
                        "kanjiAreaName",
                        "flag1",
                        "flag2",
                        "flag3",
                        "flag4",
                        "flag5",
                        "flag6")
                .targetType(PostalCodeAddress.class)
                .build();
    }

    @Bean
    public CompositeItemProcessor<PostalCodeAddress, PostalCode> postalCodeProcessor(
            PostalCodeConverter postalCodeConverter,
            BeanValidatingItemProcessor<PostalCode> postalCodeValidatingProcessor) {
        return new CompositeItemProcessorBuilder<PostalCodeAddress, PostalCode>()
                .delegates(postalCodeConverter, postalCodeValidatingProcessor)
                .build();
    }

    @Bean
    public PostalCodeConverter postalCodeConverter() {
        return new PostalCodeConverter();
    }

    @Bean
    public BeanValidatingItemProcessor<PostalCode> postalCodeValidatingProcessor() {
        return new BeanValidatingItemProcessor<>();
    }

    @Bean
    public JpaItemWriter<PostalCode> postalCodeWriter(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriterBuilder<PostalCode>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true)
                .build();
    }
}
