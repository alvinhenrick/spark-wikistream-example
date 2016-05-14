package com.wiki.dstream

import com.wiki.common.WikiEdit
import org.apache.spark.Logging
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.receiver.Receiver

/**
  * Created by Alvin on 5/14/16.
  */

class WikiInputDStream(
                        @transient ssc_ : StreamingContext,
                        storageLevel: StorageLevel,
                        server: String, port: Int, channels: List[String]
                      ) extends ReceiverInputDStream[WikiEdit](ssc_) with Logging {

  def getReceiver(): Receiver[WikiEdit] = {
    new WikiIrcReceiver(storageLevel, server, port, channels)
  }
}