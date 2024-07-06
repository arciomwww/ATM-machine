import java.util.Date;

public class Account {
    private static final long LOCK_DURATION = 24 * 60 * 60 * 1000; //milliseconds

    private final String cardNumber;
    private final String pinCode;
    private double balance;
    private int pinAttempts;
    private long lockTime;

    public Account(String cardNumber, String pinCode, double balance) {
        this.cardNumber = cardNumber;
        this.pinCode = pinCode;
        this.balance = balance;
        this.pinAttempts = 0;
        this.lockTime = 0;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPinCode() {
        return pinCode;
    }

    public double getBalance() {
        return balance;
    }

    public boolean authenticate(String pin) {
        if (isLocked()) {
            return false;
        }

        if (pinCode.equals(pin)) {
            pinAttempts = 0;
            return true;
        } else {
            pinAttempts++;
            if (pinAttempts >= 3) {
                lock();
            }
            return false;
        }
    }

    public boolean withdraw(double amount) {
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean isLocked() {
        return lockTime > 0 && (new Date().getTime() - lockTime) < LOCK_DURATION;
    }


    public void lock() {
        lockTime = new Date().getTime();
    }
}
