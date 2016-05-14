package com.wiki.test

import com.wiki.utils.WikiStreamUtils
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by shona on 5/14/16.
  */
object TestStream {

  def main(args: Array[String]) = {
    val conf = new SparkConf()
      .setAppName("IRC Wikipedia Page Edit Stream").setMaster("local[*]")
    val sparkContext = new SparkContext(conf)
    val ssc = new StreamingContext(sparkContext, Seconds(10))
    ssc.checkpoint("/tmp/wiki/produce/spark-checkpoint2")

    val channels = if (args.length > 0) sparkContext.textFile(args(0)).collect().toList
    else List("#en.wikisource", "#en.wikibooks", "#en.wikinews", "#en.wikiquote", "#en.wikipedia", "#wikidata.wikipedia")

    val temp = WikiStreamUtils.createStream(ssc, StorageLevel.MEMORY_ONLY, "irc.wikimedia.org", 6667, channels)

    temp.foreachRDD(rdd => println(rdd.name))

    ssc.start() // Start the computation
    ssc.awaitTermination() // Wait for the computation to terminate
  }

}
