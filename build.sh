#!/bin/bash

if [ $# -lt 2 ]
then
  echo "Usage: build.sh <GCP_project_ID> <build_number>"
  exit
fi

project_id=$1
deploy_number=$2

./mvnw clean package
docker build -t gcr.io/${project_id}/happy-queue:v${deploy_number} .
docker push gcr.io/${project_id}/happy-queue:v${deploy_number}

