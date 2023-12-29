package com.batch.process.demo.config;

import com.batch.process.demo.jobs.BillingJob;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BatchJobsConfiguration {

    @Autowired
    private JobRepository jobRepository;

    @Bean
    public Job billingJob() {
        return new BillingJob(jobRepository);
    }

}
