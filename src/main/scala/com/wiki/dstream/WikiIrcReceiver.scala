package com.wiki.dstream

import java.nio.charset.Charset

import com.wiki.common.WikiEdit
import org.apache.spark.Logging
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.receiver.Receiver
import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.events.MessageEvent
import org.pircbotx.{Configuration, PircBotX}

import scala.util.Random

/**
  * Created by Alvin on 5/14/16.
  */
class WikiIrcReceiver(storageLevel: StorageLevel,
                      server: String, port: Int, channels: List[String]
                     ) extends Receiver[WikiEdit](storageLevel) with Logging {

  val Regex = """^.*\[\[(.+?)\]\].\s(.*)\s.*(https?://[^\s]+).*[*]\s(.*?)\s.*[*]\s\((.*?)\)(.*)$""".r

  private def create: PircBotX = {
    val nick = s"sparkstream-${Random.nextLong()}"

    val charCode = Charset.forName("UTF-8")
    val config = new Configuration.Builder
    config.setServer(server, port)
    config.setName(nick)
    config.setLogin(nick)
    config.setRealName(nick)
    config.setAutoNickChange(true)
    config.getListenerManager.addListener(new ListenerAdapter[PircBotX] {
      override def onMessage(event: MessageEvent[PircBotX]): Unit = {
        processMessage(event.getMessage, event.getChannel.getName, System.currentTimeMillis()) foreach store
      }
    })
    config.setEncoding(charCode)
    channels foreach config.addAutoJoinChannel
    config.setAutoReconnect(true)
    new PircBotX(config.buildConfiguration)
  }

  private def processMessage(line: String, channel: String, timestamp: Long): Option[WikiEdit] = try {
    val input = line.replaceAll("[^\\x20-\\x7E]", "")
    input match {
      case Regex(title, flags, diffUrl, user, byteDiff, summary) => Some(WikiEdit(channel, timestamp,
        title, flags, diffUrl, user, byteDiff, summary))
      case _ => None
    }
  } catch {
    case ex: Throwable => {
      logWarning(s"could not parse input line: $line, error $ex")
      None
    }
  }

  lazy val botX: PircBotX = create

  def onStop(): Unit = {
    botX.stopBotReconnect()
  }

  def onStart(): Unit = {
    logInfo("starting IRC bot")
    botX.startBot()
    logInfo("Disconnected from WikiServer Server")
  }

}
