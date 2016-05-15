#!/usr/bin/env bash
#docker run -it --rm --env-file=./hadoop.env --net hadoop awesomedata/hadoop hadoop fs -mkdir -p /user/root

source ./randomstring.sh
ID=`random_str`

docker run -it --rm --env-file=./hadoop.env --net hadoop --name temp-hadoop-${ID} --volume $(pwd):/data/ awesomedata/hadoop bash