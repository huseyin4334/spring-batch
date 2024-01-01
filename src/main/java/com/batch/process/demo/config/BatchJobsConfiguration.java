package com.batch.process.demo.config;

import com.batch.process.demo.chunks.models.BillingData;
import com.batch.process.demo.chunks.models.ReportingData;
import com.batch.process.demo.chunks.processors.BillingItemProcessor;
import com.batch.process.demo.steps.FilePreparationTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Component
public class BatchJobsConfiguration {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private DataSource dataSource;

    /*
        JdbcCursorItemReader is used to read data from the database.
        JdbcItemReader is used to read data from the database.
        Difference between JdbcCursorItemReader and JdbcItemReader is that JdbcCursorItemReader uses a cursor to read data from the database.
        JdbcCursorItemReader is faster than JdbcItemReader.
        Cursor is a database object that points to a row in a database. Cursor is used to process rows returned by a query one at a time.
     */

    @Bean
    public JdbcCursorItemReader<BillingData> billingDataReader() {
        String sql = "select * from BILLING_DATA";
        return new JdbcCursorItemReaderBuilder<BillingData>()
                .name("billingDataDbReader")
                .sql(sql)
                .dataSource(dataSource)
                .rowMapper(new DataClassRowMapper<>(BillingData.class))
                .build();
    }

    @Bean
    public ItemProcessor<BillingData, ReportingData> processBillingItem() {
        return new BillingItemProcessor();
    }

    @Bean
    public FlatFileItemWriter<ReportingData> billingDataWriter() {
        return new FlatFileItemWriterBuilder<ReportingData>()
                .name("billingDataFileWriter")
                .resource(new FileSystemResource("staging/billing-2023-01.csv"))
                .delimited()
                .names("billingData.dataYear", "billingData.dataMonth", "billingData.accountId", "billingData.phoneNumber",
                        "billingData.dataUsage", "billingData.callDuration", "billingData.smsCount", "billingTotal")
                .build();
    }

    @Bean
    public Step fileCopyStep() {
        return new StepBuilder("filePreparationStep", jobRepository)
                .tasklet(new FilePreparationTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step readProcessAndWriteStep(
            ItemReader<BillingData> billingDataReader,
            ItemProcessor<BillingData, ReportingData> processBillingItem,
            ItemWriter<ReportingData> billingDataWriter
    ) {
        return new StepBuilder("readAndWriteStep", jobRepository)
                .<BillingData, ReportingData>chunk(100, transactionManager)
                .reader(billingDataReader)
                .processor(processBillingItem)
                .writer(billingDataWriter)
                .build();
    }

    @Bean
    public Job billingJob(
            Step fileCopyStep,
            Step readProcessAndWriteStep
    ) {
        return new JobBuilder("BillingJob", jobRepository)
                .start(fileCopyStep)
                .next(readProcessAndWriteStep)
                .build();
    }

}
