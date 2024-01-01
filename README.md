# SPRING BATCH

## Database Creation
- docker pull postgres
- docker run --name postgres -e POSTGRES_PASSWORD=postgres -d -p 5432:5432 postgres
- docker exec -it postgres psql -U postgres
- Execute resources/batch.sql file in the psql console.
- \d to list tables.
- Batch Job Tables
  - BATCH_JOB_EXECUTION
  - BATCH_JOB_EXECUTION_CONTEXT
  - BATCH_JOB_EXECUTION_PARAMS
  - BATCH_JOB_EXECUTION_SEQ
  - BATCH_JOB_INSTANCE
  - BATCH_JOB_SEQ
  - BATCH_STEP_EXECUTION
  - BATCH_STEP_EXECUTION_CONTEXT
  - BATCH_STEP_EXECUTION_SEQ
- Batch Job Repository will connect to the database automatically with datasource properties.

- docker exec postgres psql -U postgres -c 'select * from batch_job_instance;'


## Spring Batch Concepts
- Job
  - Job is a sequence of steps.
  - Job is an interface.
  - We can create a job by implementing the interface.
  - Job Interface
  - SimpleJob
  - FlowJob
  - We can use JobBuilder to create a job.
- JobInstance
  - JobInstance is an instance of a job.
  - JobInstance is identified by a job name and a job key.
  - We are distinguish same job instances by job parameters.
  - Job key is equal to job parameters hash code.
  - If job parameters hash code is same, then jobLauncher throws an exception.
    - org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException: A job instance already exists and is complete for parameters={}.
  - If job parameters hash code is different, then jobLauncher executes a new job instance.
  - We are using jobInstance to restart a job if it fails.
- Step
  - Step Execution is an instance of a step.
    - It is identified by a step name and a job execution id.
  - Step is an instance for jobs.
  - Step types:
    - Normal Step Interface
      - We can create a step by implementing the interface.
      - We can use normal step interface to create a simple step.
    - TaskletStep
      - Designed for simple tasks (like copying a file or creating an archive), or item-oriented tasks (like reading a file or a database table).
      - Designed for work single thread.
      - For example, we can use tasklet step to copy a file.
      - Chunk Oriented Tasklet
      - System Command Tasklet
    - PartitionStep
      - Designed to process the input data set in partitions.
      - Each partition is processed in a separate thread.
      - For example, we can use partition step to process a large file.
    - JobStep
      - Similar to a FlowStep but actually creates and launches a separate job execution for the steps in the specified flow. This is useful for creating a complex flow of jobs and sub-jobs.
    - FlowStep
      - Useful for logically grouping steps into flows.
      - For example, we can use flow step to group steps.
      - We can use flow step to create a conditional flow.
  - We can use step builder to create a step.
  - Or We can implement Step interface to create a step.
