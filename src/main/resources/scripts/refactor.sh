#!/bin/bash
set -e

# This script is used to drop and recreate the meta-data tables

docker exec postgres psql -f ./../sql/drop-batch-table.sql -U postgres
docker exec postgres psql -f ./../sql/batch-table.sql -U postgres