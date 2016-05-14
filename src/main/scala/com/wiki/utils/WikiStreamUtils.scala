package com.wiki.utils

import com.wiki.common.WikiEdit
import com.wiki.dstream.WikiInputDStream
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.api.java.{JavaDStream, JavaStreamingContext}
import org.apache.spark.streaming.dstream.DStream

import scala.reflect.ClassTag

/**
  * Created by Alvin on 5/14/16.
  */
object WikiStreamUtils {
  def createStream(
                    ssc: StreamingContext,
                    storageLevel: StorageLevel = StorageLevel.MEMORY_AND_DISK,
                    server: String,
                    port: Int,
                    channels: List[String]
                  ): DStream[WikiEdit] = {
    new WikiInputDStream(ssc, storageLevel, server, port, channels)
  }

  def createStream(
                    jssc: JavaStreamingContext,
                    storageLevel: StorageLevel,
                    server: String,
                    port: Int,
                    channels: List[String]
                  ): JavaDStream[WikiEdit] = {
    implicitly[ClassTag[AnyRef]].asInstanceOf[ClassTag[WikiEdit]]
    createStream(jssc.ssc, storageLevel, server, port, channels)
  }

}
