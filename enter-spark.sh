#!/usr/bin/env bash

ID=`date +%Y%m%d-%H%M%S`
docker run -it --rm --env-file=./hadoop.env --net hadoop --name spark-hadoop-${ID} --volume $(pwd):/data/ awesomedata/hadoop-spark bash