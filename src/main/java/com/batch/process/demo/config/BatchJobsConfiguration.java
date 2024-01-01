package com.batch.process.demo.config;

import com.batch.process.demo.steps.FilePreparationTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public class BatchJobsConfiguration {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    /*
        PlatformTransactionManager is an interface that defines the methods that a transaction manager must implement.
        Spring provides several implementations of this interface, including DataSourceTransactionManager, HibernateTransactionManager, and JpaTransactionManager.
        The transaction manager is responsible for creating and managing transactions.
        The transaction manager is used by the StepBuilder to create a Step instance.
        TaskletStep will use the transaction manager to create a transaction for the tasklet.
        Because step is execute some operations in the database, it needs to be transactional.
     */

    @Bean
    public Step fileCopyStep() {
        return new StepBuilder("filePreparationStep", jobRepository)
                .tasklet(new FilePreparationTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Job billingJob() {
        return new JobBuilder("BillingJob", jobRepository)
                .start(fileCopyStep())
                .build();
    }

}
