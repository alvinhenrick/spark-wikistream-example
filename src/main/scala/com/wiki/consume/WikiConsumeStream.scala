package com.wiki.consume

import com.wiki.common.WikiEdit
import kafka.serializer.StringDecoder
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import spray.json._
import com.wiki.common.WikiJsonProtocol._

/**
  * Created by Alvin on 5/9/16.
  */
class WikiConsumeStream {


  def main(args: Array[String]) {
    /** Spark initialization **/
    val sc = new SparkConf().setAppName("WikiData")
    val ssc = new StreamingContext(sc, Seconds(10)) // this sets the micro batch size

    /** Enable checkpoinging **/
    ssc.checkpoint("/tmp/wiki/consume/spark-checkpoint")

    /** Kafka initialisation **/
    val topicsSet = Set("wikitopic")
    val kafkaParams = Map[String, String]("metadata.broker.list" -> "kafka1.hadoop:9092")

    val strMessages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicsSet)

    val messages = strMessages.mapPartitions(records => records.map(x => x._2.parseJson.convertTo[WikiEdit]))

    messages.map(_.channel).countByValueAndWindow(Minutes(60), Seconds(10)).foreachRDD { rdd =>

      val data = rdd.collect()
        .sorted(Ordering.by[(String, Long), Long](_._2).reverse)
        .map { case (channel, count) => ("channel" -> channel, "count" -> count + "") }
        .toList

      println("******************************************")
      println("\n\n\n\n")
      println(data.take(3).toJson)
      println("\n\n\n\n")
      println("******************************************")
    }
  }

}

