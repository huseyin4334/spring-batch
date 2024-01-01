package com.batch.process.demo.steps;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FilePreparationTasklet implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        JobParameters parameters = stepContribution.getStepExecution().getJobParameters();

        String file = parameters.getString("input.file");

        assert file != null;

        Path source = new ClassPathResource(file).getFile().toPath();
        Path target = Paths.get("staging", source.getFileName().toString());


        Files.copy(source,
                target,
                StandardCopyOption.REPLACE_EXISTING);

        return RepeatStatus.FINISHED;
    }

    /*
        StepContribution is an interface that defines the methods that a step contribution must implement.
        The StepContribution is used to add information to the StepExecution.
        The StepExecution is the context in which the step is executed.

        ChunkContext is an interface that defines the methods that a chunk context must implement.
        The ChunkContext is used to add information to the ChunkExecution.
        The ChunkExecution is the context in which the chunk is executed.
     */
}
