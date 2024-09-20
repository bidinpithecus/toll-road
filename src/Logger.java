import java.io.FileWriter;

public class Logger {
  public static void log(String filename, String message) {
    try {
      FileWriter fw = new FileWriter(filename, true);

      for (int i = 0; i < message.length(); i++) {
        fw.write(message.charAt(i));
      }
      fw.write("\n");

      fw.close();
    } catch (Exception e) {
      System.out.printf("Deu um pepino ao escrever no arquivo %s\n", filename);
      e.getStackTrace();
    }
  }
}
