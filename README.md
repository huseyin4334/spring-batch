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
- Step
  - Step is an interface.
  - Step is a sequence of tasks or a sequence of chunks.
  - We can create a step by implementing the interface.
- Tasklet
  - Tasklet is an interface.
  - Tasklet is a single task.
  - We can create a tasklet by implementing the interface.
  - Tasklet is used to perform a single task.
- Chunk
  - Chunk is a part of a step.
  - Chunk is a sequence of items.
  - Items are read, processed and written. This means that a chunk is a sequence of read, process and write operations.
  - Chunk is a part of a step.
  - ItemReader, ItemProcessor and ItemWriter are used to read, process and write items respectively.
- ItemReader
  - ItemReader is an interface.
  - ItemReader is used to read items from a file or a database.
- ItemWriter
  - ItemWriter is an interface.
  - ItemWriter is used to write items to a file or a database.
- ItemProcessor
  - ItemProcessor is an interface.
  - ItemProcessor is used to process items.
  - ItemProcessor is optional in a chunk.
  - ItemProcessor is used to process items before writing them to a file or a database.
- JobLauncher
  - JobLauncher is used to launch a job.
- JobRepository
  - JobRepository is an interface.
  - JobRepository is used to store the state of a job.
  - We should jobRepository because of restartability.