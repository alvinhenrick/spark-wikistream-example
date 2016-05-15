#!/usr/bin/env bash

ID=$1
#`date +%Y%m%d%H%M%S`
docker run -it --rm --env-file=./hadoop.env --net hadoop --name spark-hadoop-${ID} --volume $(pwd):/data/ awesomedata/hadoop-spark bash