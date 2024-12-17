#!/bin/bash

curr_dir=$(pwd)
echo "### Starting pn-statemachinemanager ###"

if ! ../src/test/resources/testcontainers/init.sh; then
  echo "### Failed to run init.sh ###"
  exit 1
fi

cd ..

if ! mvn spring-boot:run; then
  echo "### Initialization failed ###"
  exit 1
fi

# Return to the original directory
cd "$curr_dir"
