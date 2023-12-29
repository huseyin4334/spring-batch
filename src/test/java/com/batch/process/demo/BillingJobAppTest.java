package com.batch.process.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest
public class BillingJobAppTest {

    @Autowired
    private Job job;

    @Autowired
    private JobLauncher jobLauncher;

    @Test
    void JobExecutionTest(CapturedOutput output) throws Exception {
        // given
        JobParameters parameters = new JobParameters();

        // when
        JobExecution execution = jobLauncher.run(job, parameters);

        // then
        String expectedOutput = "BillingJob was executed";

        System.out.println(execution.getJobId());

        Assertions.assertTrue(
                output.getOut().contains(expectedOutput),
                "The expected output was not found. Expected: " + expectedOutput + " Actual: " + output.getOut()
        );
    }
}
