import com.rabbitmq.client.*;

import java.io.IOException;

public class Service {
  public String queueName;
  public String hostname;
  public String logFilename;

  public Service(String queueName, String hostname, String logFilename) throws Exception {
    this.hostname = hostname;
    this.queueName = queueName;
    this.logFilename = logFilename;

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(this.hostname);

    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(this.queueName, false, false, false, null);
    Logger.log(this.logFilename, "Queue " + this.queueName + " waiting for messages.");

    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
          throws IOException {
        String msg = new String(body, "UTF-8");
        Logger.log(Service.this.logFilename, "Queue " + Service.this.queueName + " received message " + msg);
      }
    };

    channel.basicConsume(this.queueName, true, consumer);
  }
}
