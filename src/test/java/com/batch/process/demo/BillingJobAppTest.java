package com.batch.process.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest
@SpringBatchTest
class BillingJobAppTest {

    /*
        The following two beans are used to reset the job repository before each test.
        These beans coming from the Spring Batch Test framework.
     */

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /*
        docker exec postgres psql -U postgres -c 'select * from batch_job_instance;'
        You will see just last job instance in the table. Because beforeEach() method clears the table.
     */
    @BeforeEach
    void beforeEach() {
        jobRepositoryTestUtils.removeJobExecutions();
        JdbcTestUtils.deleteFromTables(this.jdbcTemplate, "BILLING_DATA");
    }

    @Test
    void copyFileStage() throws Exception {
        // given
        JobParameters parameters = new JobParametersBuilder()
                .addString("input.file",
                        "billingdata/billing-2023-01.csv")
                .toJobParameters();

        // when
        JobExecution execution = jobLauncherTestUtils.launchJob(parameters);

        System.out.println(execution.getJobId());

        // then
        Assertions.assertTrue(Files.exists(Paths.get("staging", "billing-2023-01.csv")));

        Assertions.assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
    }

    @Test
    void readAndWrite() throws Exception {
        // given
        JobParameters parameters = new JobParametersBuilder()
                .addString("input.file",
                        "billingdata/billing-2023-01.csv")
                .toJobParameters();

        // when
        JobExecution execution = jobLauncherTestUtils.launchJob(parameters);

        System.out.println(execution.getJobId());

        // then
        Assertions.assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
        Assertions.assertEquals(1000, JdbcTestUtils.countRowsInTable(jdbcTemplate, "BILLING_DATA"));
    }
}
