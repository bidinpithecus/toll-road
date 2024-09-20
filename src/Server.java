import java.util.concurrent.TimeUnit;
import com.rabbitmq.client.ConnectionFactory;

public class Server {
  private Booth booth;

  public Server() throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");

    this.booth = new Booth(factory);

    Service semParar = new Service("SemParar", "localhost");
    Service vloe = new Service("VLOE", "localhost");
  }

  public void receiveCar(String car) throws Exception {
    System.out.printf("   Car %s stopped at the booth\n", car);
    booth.processCar(car);
  }

  public static void main(String[] args) throws Exception {
    int totalNumOfCars = Integer.valueOf(args[0]);
    int numOfCarsPerInterval = Integer.valueOf(args[1]);
    int interval = Integer.valueOf(args[2]);

    if (args.length != 3) {
      System.out.println("Usage: java Server <totalNumOfCars> <numOfCarsPerInterval> <interval>");
      System.out.println("<totalNumOfCars>: Total number of cars to process.");
      System.out.println("<numOfCarsPerInterval>: Number of cars processed per interval.");
      System.out.println("<interval>: Time interval between each tick (in milliseconds).");

      return;
    }

    System.out.println("To stop the server, press CTRL + C");

    Server server = new Server();

    for (int i = 0; i < totalNumOfCars; i += numOfCarsPerInterval) {
      for (int j = 0; j < numOfCarsPerInterval; j += 1) {
        server.receiveCar(MessageFactory.createMessage());
      }
      TimeUnit.MILLISECONDS.sleep(interval);
    }
  }
}
