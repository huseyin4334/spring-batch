package com.batch.process.demo.config;

import com.batch.process.demo.chunks.models.BillingData;
import com.batch.process.demo.steps.FilePreparationTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
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
        Readers read data from the input source and pass it to the processor one by one.
        But writes are done in chunks. Because bulk writes are more efficient than single writes.
     */

    @Bean
    public FlatFileItemReader<BillingData> billingDataReader() {
        return new FlatFileItemReaderBuilder<BillingData>()
                .name("billingDataReader")
                .resource(new FileSystemResource("staging/billing-2023-01.csv"))
                .delimited()
                .names("dataYear", "dataMonth", "accountId", "phoneNumber", "dataUsage", "callDuration", "smsCount")
/*                .fieldSetMapper(fieldSet -> new BillingData(
                        fieldSet.readInt("dataYear"),
                        fieldSet.readInt("dataMonth"),
                        fieldSet.readInt("accountId"),
                        fieldSet.readString("phoneNumber"),
                        fieldSet.readFloat("dataUsage"),
                        fieldSet.readInt("callDuration"),
                        fieldSet.readInt("smsCount")
                ))*/
                .targetType(BillingData.class)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<BillingData> billingDataWriter() {
        String sql = "INSERT INTO BILLING_DATA (DATA_YEAR, DATA_MONTH, ACCOUNT_ID, PHONE_NUMBER, DATA_USAGE, CALL_DURATION, SMS_COUNT) " +
                "VALUES (:dataYear, :dataMonth, :accountId, :phoneNumber, :dataUsage, :callDuration, :smsCount)";

        return new JdbcBatchItemWriterBuilder<BillingData>()
                .dataSource(dataSource)
                .sql(sql)
                .beanMapped() // This is necessary to map the object fields to the SQL parameters.
                .build();
    }

    @Bean
    public Step fileCopyStep() {
        return new StepBuilder("filePreparationStep", jobRepository)
                .tasklet(new FilePreparationTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step readAndWriteStep(
            ItemReader<BillingData> billingDataReader,
            JdbcBatchItemWriter<BillingData> billingDataWriter
    ) {
        return new StepBuilder("readAndWriteStep", jobRepository)
                .<BillingData, BillingData>chunk(100, transactionManager)
                .reader(billingDataReader)
                .writer(billingDataWriter)
                .build();
    }

    @Bean
    public Job billingJob(
            Step fileCopyStep,
            Step readAndWriteStep
    ) {
        return new JobBuilder("BillingJob", jobRepository)
                .start(fileCopyStep)
                .next(readAndWriteStep)
                .build();
    }

}
