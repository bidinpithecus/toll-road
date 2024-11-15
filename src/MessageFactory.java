import java.security.SecureRandom;
import java.io.FileWriter;
import java.io.IOException;

public class MessageFactory {
    private static final String plateCharacters = "1234567890QWERTYUIOPASDFGHJKLZXCVBNM";
    private static final SecureRandom random = new SecureRandom();

    public static String generateCarPlate() {
        String plate = "";
        for (int i = 0; i < 7; i++) {
            int index = random.nextInt(plateCharacters.length());
            plate += (plateCharacters.charAt(index));
        }
        return plate;
    }

    public static int generatePaymentValue() {
        return random.nextInt(10) + 1;
    }

    public static String generateServiceIdentifier() {
        return random.nextBoolean() ? "VLOE" : "SemParar";
    }

    public static String createMessage() {
        String plate = generateCarPlate();
        int paymentValue = generatePaymentValue();
        String serviceIdentifier = random.nextBoolean() ? generateServiceIdentifier() : null;
        String message = plate + ";" + paymentValue;
        if (serviceIdentifier != null) {
            message += ";" + serviceIdentifier;

            if (random.nextBoolean()) {
                if (serviceIdentifier.equals("VLOE")) {
                    try {
                        FileWriter writer = new FileWriter("database/vloe.db", true);

                        writer.write(message + "\n");
                        writer.close();
                    } catch (IOException e) {
                        System.out.println("Deu um pepino ao escrever no arquivo vloe.db");
                        e.printStackTrace();
                    }
                } else {
                    try {
                        FileWriter writer = new FileWriter("database/semParar.db", true);
                        writer.write(message + "\n");
                        writer.close();
                    } catch (IOException e) {
                        System.out.println("Deu um pepino ao escrever no arquivo semParar.db");
                        e.printStackTrace();
                    }
                }
            }
        }
        return message;
    }
}