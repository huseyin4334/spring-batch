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
- JobInstance
  - JobInstance is an instance of a job.
  - JobInstance is identified by a job name and a job key.
  - We are distinguish same job instances by job parameters.
  - Job key is equal to job parameters hash code.
  - If job parameters hash code is same, then jobLauncher throws an exception.
    - org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException: A job instance already exists and is complete for parameters={}.
  - If job parameters hash code is different, then jobLauncher executes a new job instance.
  - We are using jobInstance to restart a job if it fails.