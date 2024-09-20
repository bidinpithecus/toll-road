import com.rabbitmq.client.*;
import java.io.IOException;

public class Service {
  public String queueName;
  public String hostname;

  public Service(String queueName, String hostname) throws Exception {
    this.hostname = hostname;
    this.queueName = queueName;

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(this.hostname);

    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(this.queueName, false, false, false, null);
    System.out.printf("Queue %s waiting for messages.\n", this.queueName);

    Consumer consumer = new DefaultConsumer(channel) {
      public void handleMessage(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
          throws IOException {
        String msg = new String(body, "UTF-8");
        System.out.printf("Queue %s receieved msg \"%s\".", Service.this.queueName, msg);
      }
    };

    channel.basicConsume(this.queueName, true, consumer);
  }
}
