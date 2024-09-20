import com.rabbitmq.client.*;
import java.io.IOException;

public class Booth {
  private ConnectionFactory factory;
  private String logFilename;

  public Booth(ConnectionFactory factory, String logFilename) {
    this.factory = factory;
    this.logFilename = logFilename;
  }

  public void openGate(String car) {
    Logger.log(this.logFilename, "Opening gate for " + car);
  }

  public void processCar(String car) throws Exception {
    Logger.log(logFilename, "Car " + car + " stopped at the booth");
    String[] parts = car.split(";");
    if (parts.length == 3) {
      openGate(car);
      String licensePlate = parts[0];
      int price = Integer.valueOf(parts[1]);
      String serviceTag = parts[2];

      sendToService(licensePlate, price, serviceTag);
    } else {
      Logger.log(this.logFilename, "Car " + car + " without TAG");
    }
  }

  private void sendToService(String licensePlate, int price, String serviceTag) throws Exception {
    Connection connection = this.factory.newConnection();
    Channel channel = connection.createChannel();

    String message = licensePlate + ";" + price;

    channel.queueDeclare(serviceTag, false, false, false, null);
    channel.basicPublish("", serviceTag, null, message.getBytes("UTF-8"));

    Logger.log(this.logFilename, "Booth sent message to service " + serviceTag + ": " + message);

    channel.close();
    connection.close();
  }
}
