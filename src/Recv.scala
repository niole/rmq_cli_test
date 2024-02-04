import com.rabbitmq.client.{Channel, Connection, DeliverCallback, ConnectionFactory, Delivery}

object Recv {

  private val QUEUE_NAME: String = "hello"

  def apply(): Unit = {
    val factory = new ConnectionFactory();
    factory.setHost("localhost");
    val connection = factory.newConnection();
    val channel = connection.createChannel();

    channel.queueDeclare(QUEUE_NAME, false, false, false, null);

    val deliverCallback: DeliverCallback = { (consumerTag: String, delivery: Delivery) =>
      val message = new String(delivery.getBody(), "UTF-8")
      System.out.println(" [x] Received '" + message + "'");
    };

    channel.basicConsume(QUEUE_NAME, true, deliverCallback, { consumerTag => })

    System.out.println(" [*] Waiting for messages. To exit press CTRL+C")
  }
}
