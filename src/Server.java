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
    System.out.printf("Car %s stopped at the booth\n", car);
    booth.processCar(car);
  }

  public static void main(String[] args) throws Exception {
    Server server = new Server();

    server.receiveCar(MessageFactory.createMessage());
  }
}
