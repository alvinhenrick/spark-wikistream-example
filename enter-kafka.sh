#!/usr/bin/env bash

ID=`date +%Y%m%d-%H%M%S`
docker run -it --rm --env-file=./kafka.env --net hadoop --name temp-kafka-${ID} awesomedata/kafka bash