#!/usr/bin/env bash

/opt/spark-1.6.1/bin/spark-submit --master spark://spark-master.hadoop:7077 --class com.wiki.produce.WikiProduceStream --deploy-mode client --driver-memory 1g --executor-memory 1g --executor-cores 1 target/spark-wikistream-example.jar