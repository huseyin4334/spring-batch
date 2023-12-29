package com.batch.process.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.util.UUID;

@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest
class BillingJobAppTest {

    @Autowired
    private Job job;

    @Autowired
    private JobLauncher jobLauncher;

    @RepeatedTest(2)
    void repeatedlyJobTest(CapturedOutput output) throws Exception {
        // given
        JobParameters parameters = new JobParametersBuilder()
                .addString("input.file", "resources/billingdata/billing-2023-01.csv")
                .addString("instance_id", UUID.randomUUID().toString())
                .toJobParameters();

        // when
        JobExecution execution = jobLauncher.run(job, parameters);

        // then
        String expectedOutput = "Processing billing information from file resources/billingdata/billing-2023-01.csv";

        System.out.println(execution.getJobId());

        Assertions.assertTrue(
                output.getOut().contains(expectedOutput),
                "The expected output was not found. Expected: " + expectedOutput + " Actual: " + output.getOut()
        );
    }

    @Test
    void repeatedlyJobExceptionTest() throws Exception {
        // given
        String uuid = UUID.randomUUID().toString();
        JobParameters parameters = new JobParametersBuilder()
                .addString("input.file", "resources/billingdata/billing-2023-01.csv")
                .addString("instance_id", uuid)
                .toJobParameters();

        // when
        jobLauncher.run(job, parameters);

        JobParameters params = new JobParametersBuilder()
                .addString("input.file", "resources/billingdata/billing-2023-01.csv")
                .addString("instance_id", uuid)
                .toJobParameters();

        Assertions.assertThrows(
                org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException.class,
                () -> jobLauncher.run(job, params)
        );
    }
}
