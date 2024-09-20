import java.util.concurrent.TimeUnit;
import com.rabbitmq.client.ConnectionFactory;

public class Server {
  private Booth booth;

  public Server() throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");

    TicketApplier ticketApplier = new TicketApplier("tickets", "localhost", "logs/tickets.log", factory);

    this.booth = new Booth(factory, "logs/booth.log", ticketApplier);

    Service semParar = new Service("SemParar", "localhost", "logs/semParar.log", "database/semParar.db", factory,
        ticketApplier);
    Service vloe = new Service("VLOE", "localhost", "logs/vloe.log", "database/vloe.db", factory, ticketApplier);
  }

  public void receiveCar(String car) throws Exception {
    Logger.log("logs/server.log", "Car " + car + " stopped at the booth");
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
      System.out.printf("%d cars have arrived at the booth\n", i + numOfCarsPerInterval);
      TimeUnit.MILLISECONDS.sleep(interval);
    }
    System.out.println("There are no more cars in the entire world, therefore you shall close the program");
  }
}
