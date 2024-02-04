import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Connection
import com.rabbitmq.client.Channel

import scala.util.Using
import scala.util.control.NonFatal

object Send {
  private val QUEUE_NAME: String = "hello"

  def apply(message: String): Unit = {
    val factory = new ConnectionFactory()
    factory.setHost("localhost")

    try {
      Using.Manager { use =>
        val connection = use(factory.newConnection())
        val channel = use(connection.createChannel())

        channel.queueDeclare(QUEUE_NAME, false, false, false, null)

        channel.basicPublish("", QUEUE_NAME, null, message.getBytes())

        println(" [x] Sent '" + message + "'")
      }
    } catch {
      case NonFatal(exn) =>
        println(s"Failed to connect to rmq server: ${exn.getMessage}")
    }
  }
}
