#!/usr/bin/env bash

source ./randomstring.sh
ID=$(random_str)

docker run -it --rm --env-file=./kafka.env --net hadoop --name temp-kafka-${ID} awesomedata/kafka bash