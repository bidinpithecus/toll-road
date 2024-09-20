import com.rabbitmq.client.*;
import java.io.IOException;

public class Booth {
  private ConnectionFactory factory;

  public Booth(ConnectionFactory factory) {
    this.factory = factory;
  }

  public void openGate() {
    System.out.printf("ABRI(N)DO\n");
  }

  public void processCar(String car) throws Exception {
    String[] parts = car.split(";");
    if (parts.length == 3) {
      openGate();
      String licensePlate = parts[0];
      int price = Integer.valueOf(parts[1]);
      String serviceTag = parts[2];

      sendToService(licensePlate, price, serviceTag);
    } else {
      System.out.println("Carro sem teoria dos grafos");
    }
  }

  private void sendToService(String licensePlate, int price, String serviceTag) throws Exception {
    Connection connection = this.factory.newConnection();
    Channel channel = connection.createChannel();

    String message = licensePlate + ";" + price;

    channel.queueDeclare(serviceTag, false, false, false, null);
    channel.basicPublish("", serviceTag, null, message.getBytes("UTF-8"));

    System.out.printf("Booth sent message to service %s: %s\n", serviceTag, message);

    channel.close();
    connection.close();
  }
}
