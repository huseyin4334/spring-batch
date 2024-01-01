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
Ingesting a file into a database table seems like a simple and easy task at first glance, but this simple task is actually quite challenging! 
What if the input file is big enough that it doesn't fit in memory? What if the process that ingests the file is terminated abruptly half way through? 
How do we deal with these situations in an efficient and fault-tolerant way?

Spring Batch comes with a processing model that is designed and implemented to address those challenges. It is called the chunk-oriented processing model. 
The idea of this model is to process the datasource in chunks of a configurable size.

A chunk is a collection of "items" from the datasource. An item can be a line in a flat file, a record in a database table, etc.

Each chunk of items is read and written within the scope of a transaction. This way, chunks are either committed together or rolled back together, 
meaning they either all succeed or fail together. You can think of this as an "all-or-nothing" approach, but only for the specific chunk that's being processed.