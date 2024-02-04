import com.rabbitmq.client.{Connection, DeliverCallback, ConnectionFactory, Delivery}

object Recv {

  private val QUEUE_NAME: String = "hello"

  def apply(workerName: String = "worker1"): Unit = {
    val factory = new ConnectionFactory();
    factory.setHost("localhost");
    val connection = factory.newConnection();
    val channel = ChannelFactory(connection)

    val deliverCallback: DeliverCallback = { (consumerTag: String, delivery: Delivery) =>
      val message = new String(delivery.getBody(), "UTF-8")
      System.out.println(s" [x] $workerName Received '" + message + "'");

      doWork(message)
      channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false)
    };

    channel.basicConsume(QUEUE_NAME, false, deliverCallback, { consumerTag => })

    System.out.println(s" [*] $workerName Waiting for messages. To exit press CTRL+C")
  }

  def doWork(task: String): Unit = {
     val seconds = task.toCharArray().count(_ == '.')
     println(s"Start sleeping for $seconds seconds")
     Thread.sleep(seconds * 1000)
     println("Done")
  }
}
