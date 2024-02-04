import com.rabbitmq.client.{Channel, Connection, DeliverCallback, ConnectionFactory, Delivery}
import com.rabbitmq.client.MessageProperties

import scala.util.Using.Releasable

trait ChannelHolder {
  val name: String
  val channel: Channel

  def sendMessage(msg: String): Unit
}
case class BasicChannelHolder(name: String, channel: Channel)  extends ChannelHolder {
  override def sendMessage(message: String): Unit = {
    channel.basicPublish("", name, null, message.getBytes())
  }
}

case class DurableChannelHolder(name: String, channel: Channel) extends ChannelHolder {
  override def sendMessage(message: String): Unit = {
    channel.basicPublish("", name, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes())
  }
}

object ChannelFactory {
  val DEFAULT_QUEUE_NAME: String = "hello"
  val DURABLE_Q_NAME: String = "durable"

  def apply(
    connection: Connection,
    queueName: String = DEFAULT_QUEUE_NAME,
    durable: Boolean = false,
  ): ChannelHolder = {
    val channel = connection.createChannel();

    channel.basicQos(1)
    channel.queueDeclare(queueName, durable, false, false, null);

    if (durable) {
      DurableChannelHolder(queueName, channel)
    } else {
      BasicChannelHolder(queueName, channel)
    }
  }
}
