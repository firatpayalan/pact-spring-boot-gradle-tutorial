#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username admin <<-EOSQL
  CREATE USER pactbrokeruser WITH PASSWORD '$PACTBROKER_USER_PASSWORD';
  CREATE DATABASE pactbroker WITH OWNER pactbrokeruser;
  GRANT ALL PRIVILEGES ON DATABASE pactbroker TO pactbrokeruser;
EOSQL