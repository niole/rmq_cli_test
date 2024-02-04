import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Connection

import scala.util.Using
import scala.util.control.NonFatal

object Send {
  def apply(message: String): Unit = {
    val factory = new ConnectionFactory()
    factory.setHost("localhost")

    try {
      Using.Manager { use =>
        val connection = use(factory.newConnection())
        val channelHolder = ChannelFactory(connection, ChannelFactory.DURABLE_Q_NAME, true)
        use(channelHolder.channel)

        channelHolder.sendMessage(message)

        println(" [x] Sent '" + message + "'")
      }
    } catch {
      case NonFatal(exn) =>
        println(s"Failed to connect to rmq server: ${exn.getMessage}")
    }
  }
}
