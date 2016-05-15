package com.wiki.produce

import com.wiki.common.Protocols
import com.wiki.factory.KafkaProducerFactory
import com.wiki.utils.WikiStreamUtils
import org.apache.kafka.clients.producer.{ProducerConfig, ProducerRecord}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import spray.json._


/**
  * Created by Alvin on 5/2/16.
  */
object WikiProduceStream extends Protocols {

  def main(args: Array[String]) = {

    val conf = new SparkConf()
      .setAppName("IRC Wikipedia Page Edit Stream")
    val sparkContext = new SparkContext(conf)

    val ssc = new StreamingContext(sparkContext, Seconds(10))
    ssc.checkpoint("/tmp/wiki/produce/spark-checkpoint")

    val channels = if (args.length > 0) sparkContext.textFile(args(0)).collect().toList
    else List("#en.wikisource", "#en.wikibooks", "#en.wikinews", "#en.wikiquote", "#en.wikipedia", "#wikidata.wikipedia")

    val stream = WikiStreamUtils.createStream(ssc, StorageLevel.MEMORY_ONLY, "irc.wikimedia.org", 6667, channels)

    stream.foreachRDD { rdd =>
      rdd.foreachPartition { records =>
        val props = Map(
          ProducerConfig.BOOTSTRAP_SERVERS_CONFIG -> "kafka1.hadoop:9092",
          ProducerConfig.CLIENT_ID_CONFIG -> "producer-01")
        val producer = KafkaProducerFactory.getOrCreateProducer(props)
        records.foreach { record =>
          val message = new ProducerRecord[String, String]("wikitopic", record.channel, record.toJson.compactPrint)
          producer.send(message)
        }
      }
    }

    ssc.start() // Start the computation
    ssc.awaitTermination() // Wait for the computation to terminate
  }

}
