package com.batch.process.demo.jobs;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.repository.JobRepository;

public class BillingThrowJob implements Job {


    private JobRepository jobRepository;

    public BillingThrowJob(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public String getName() {
        return "BillingThrowJob";
    }

    @Override
    public void execute(JobExecution execution) {
        try {
            System.out.println("BillingThrowJob was executed");

            throw new Exception("BillingThrowJob failed");
        } catch (Exception e) {
            execution.addFailureException(e);

            execution.setStatus(BatchStatus.COMPLETED);

            execution.setExitStatus(
                    ExitStatus.FAILED
                            .addExitDescription(e.getMessage())
            );

        } finally {
            System.out.println("BillingThrowJob in finally");

            jobRepository.update(execution);
        }
    }
}
