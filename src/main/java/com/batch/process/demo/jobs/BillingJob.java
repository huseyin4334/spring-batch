package com.batch.process.demo.jobs;

import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.JobRepository;

public class BillingJob implements Job {


    private JobRepository jobRepository;

    public BillingJob(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public String getName() {
        return "BillingJob";
    }

    @Override
    public void execute(JobExecution execution) {

        // Get the job parameters from the job execution
        // job execution prepared by the job launcher
        JobParameters jobParameters = execution.getJobParameters();

        String file = jobParameters.getString("input.file");

        System.out.println("Processing billing information from file " + file);

        execution.setStatus(BatchStatus.COMPLETED);
        execution.setExitStatus(ExitStatus.COMPLETED);
        jobRepository.update(execution);
    }
}
