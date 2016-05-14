#!/usr/bin/env bash
#docker run -it --rm --env-file=./hadoop.env --net hadoop awesomedata/hadoop hadoop fs -mkdir -p /user/root

ID=`date +%Y%m%d-%H%M%S`
docker run -it --rm --env-file=./hadoop.env --net hadoop --name temp-hadoop-${ID} --volume $(pwd):/data/ awesomedata/hadoop bash