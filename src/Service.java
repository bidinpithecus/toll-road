import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Service {
  public String queueName;
  public String hostname;
  public String logFilename;
  public String databaseFilename;
  public ConnectionFactory factory;
  public TicketApplier ticketApplier;

  public Service(String queueName, String hostname, String logFilename, String databaseFilename,
      ConnectionFactory factory,
      TicketApplier ticketApplier) throws Exception {
    this.hostname = hostname;
    this.queueName = queueName;
    this.logFilename = logFilename;
    this.databaseFilename = databaseFilename;
    this.ticketApplier = ticketApplier;
    this.factory = factory;

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
        Logger.log(Service.this.logFilename, "Queue " + Service.this.queueName + " received car " + msg);
        msg += ";" + Service.this.queueName;

        if (isCarInService(databaseFilename, msg)) {
          Logger.log(Service.this.logFilename, "Car " + msg + " has a valid TAG");
        } else {
          Logger.log(Service.this.logFilename, "Car " + msg + " has an invalid TAG");

          try {
            Service.this.ticketApplier.applyTicket(msg);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    };

    channel.basicConsume(this.queueName, true, consumer);
  }

  public static boolean isCarInService(String database, String car) {
    try (BufferedReader br = new BufferedReader(new FileReader(database))) {
      String line;
      while ((line = br.readLine()) != null) {
        if (line.contains(car)) {
          return true;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }
}
