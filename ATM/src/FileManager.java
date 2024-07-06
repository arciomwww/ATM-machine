import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileManager {
    public static Map<String, Account> loadFromFile(String fileName) {
        Map<String, Account> accounts = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 3) {
                    String cardNumber = parts[0];
                    String pinCode = parts[1];
                    double balance = Double.parseDouble(parts[2]);
                    accounts.put(cardNumber, new Account(cardNumber, pinCode, balance));
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
        return accounts;
    }

    public static void saveToFile(Map<String, Account> accounts, String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Account account : accounts.values()) {
                bw.write(account.getCardNumber() + " " + account.getPinCode() + " " + account.getBalance());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }
}
