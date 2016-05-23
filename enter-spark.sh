#!/usr/bin/env bash

source ./randomstring.sh
ID=$(random_str)

docker run -it --rm --env-file=./hadoop.env --net hadoop --name spark-hadoop-${ID} --volume $(pwd):/data/ awesomedata/hadoop-spark bash