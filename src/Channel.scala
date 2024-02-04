import com.rabbitmq.client.{Channel, Connection, DeliverCallback, ConnectionFactory, Delivery}

object ChannelFactory {
  private val QUEUE_NAME: String = "hello"

  def apply(connection: Connection): Channel = {
    val channel = connection.createChannel();
    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    channel.basicQos(1)
    channel
  }
}
