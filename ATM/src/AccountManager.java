import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class AccountManager {
    private static final String DATA_FILE = "C:\\Users\\arcem\\IdeaProjects\\ATM machine\\accounts.txt";
    private Map<String, Account> accounts;

    public AccountManager() {
        accounts = new HashMap<>();
        loadAccounts();
    }

    public Account getAccount(String cardNumber) {
        return accounts.get(cardNumber);
    }

    public boolean isFormatCardNumber(String cardNumber) {
        return Pattern.matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}", cardNumber);
    }

    public boolean isValidCardNumber(String cardNumber) {
        return isLunaValid(cardNumber);
    }

    private boolean isLunaValid(String cardNumber) {
        cardNumber = cardNumber.replace("-", "");
        int n = cardNumber.length();
        int total = 0;
        boolean even = false;

        for (int i = n - 1; i >= 0; i--) {
            int digit = cardNumber.charAt(i) - '0';
            if (digit < 0 || digit > 9) {
                return false;
            }
            if (even) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            total += digit;
            even = !even;
        }

        return total % 10 == 0;
    }


    public void saveAccounts() {
        FileManager.saveToFile(accounts, DATA_FILE);
    }

    private void loadAccounts() {
        accounts = FileManager.loadFromFile(DATA_FILE);
    }
}
