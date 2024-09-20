import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TicketApplier {
  public String queueName;
  public String hostname;
  public String logFilename;
  public ConnectionFactory factory;

  public TicketApplier(String queueName, String hostname, String logFilename, ConnectionFactory factory)
      throws Exception {
    this.hostname = hostname;
    this.queueName = queueName;
    this.logFilename = logFilename;
    this.factory = factory;

    Connection connection = this.factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(this.queueName, false, false, false, null);
    Logger.log(this.logFilename, "Queue " + this.queueName + " waiting for messages.");

    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
          throws IOException {
        String msg = new String(body, "UTF-8");
        Logger.log(TicketApplier.this.logFilename, "Queue " + TicketApplier.this.queueName + " received car " + msg);
        Logger.log(TicketApplier.this.logFilename, "Car " + msg + " eligible for a ticket");
      }
    };

    channel.basicConsume(this.queueName, true, consumer);
  }

  public void applyTicket(String car) throws Exception {
    Connection connection = this.factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(this.queueName, false, false, false, null);
    channel.basicPublish("", this.queueName, null, car.getBytes("UTF-8"));

    channel.close();
    connection.close();
  }
}
