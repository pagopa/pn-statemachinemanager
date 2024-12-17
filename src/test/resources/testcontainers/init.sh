#!/bin/bash

set -e

start_time=$(date +%s)

## CONFIGURATION ##
VERBOSE=true
AWS_REGION="eu-south-1"
LOCALSTACK_ENDPOINT="http://localhost:4566"
DYNAMODB_ENDPOINT=$LOCALSTACK_ENDPOINT

## DEFINITIONS ##
DYNAMODB_TABLES=(
  "pn-SmStates:processClientId,currStatus"
)

## LOGGING FUNCTIONS ##
log() { echo "[$(date +'%Y-%m-%d %H:%M:%S')] $*"; }

silent() {
  if [ "$VERBOSE" = false ]; then
    "$@" > /dev/null 2>&1
  else
    "$@"
  fi
}

## HELPER FUNCTIONS ##
wait_for_pids() {
    local -n pid_array=$1
    local error_message=$2
    local exit_code=0

    for pid in "${pid_array[@]}"; do
        wait "$pid" || { log "$error_message"; exit_code=1; }
    done

    return "$exit_code"
}

## FUNCTIONS ##
create_table() {
    local table_name=$1
    local pk=$2
    local sk=$3

    log "Creating table: $table_name (PK: $pk, SK: $sk)"

    if ! aws dynamodb describe-table --table-name "$table_name" \
                                   --endpoint-url "$DYNAMODB_ENDPOINT" \
                                   --region $AWS_REGION > /dev/null 2>&1; then
        aws dynamodb create-table --table-name "$table_name" \
                               --attribute-definitions \
                                   AttributeName="$pk",AttributeType=S \
                                   AttributeName="$sk",AttributeType=S \
                               --key-schema \
                                   AttributeName="$pk",KeyType=HASH \
                                   AttributeName="$sk",KeyType=RANGE \
                               --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
                               --region $AWS_REGION \
                               --endpoint-url "$DYNAMODB_ENDPOINT" && \
        log "Created DynamoDB table: $table_name" || \
        { log "Failed to create DynamoDB table: $table_name"; return 1; }
    else
        log "Table already exists: $table_name"
    fi
}

initialize_dynamo() {
    log "### Initializing DynamoDB tables ###"
    local pids=()

    for table_def in "${DYNAMODB_TABLES[@]}"; do
        IFS=: read -r table_name keys <<< "$table_def"
        IFS=, read -r pk sk <<< "$keys"
        silent create_table "$table_name" "$pk" "$sk" &
        pids+=($!)
    done

    wait_for_pids pids "Failed to initialize DynamoDB tables"
    return "$?"
}



## MAIN ##
main() {
    log "Starting LocalStack initialization..."

    initialize_dynamo || { log "Failed to initialize DynamoDB"; exit 1; }

    local end_time=$(date +%s)
    log "LocalStack initialization completed in $((end_time - start_time)) seconds"
    log "*Initialization terminated.*"
}

main