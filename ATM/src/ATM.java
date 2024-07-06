import java.util.Scanner;

public class ATM {
    private static final int MAX_PIN_ATTEMPTS = 3;

    private final AccountManager accountManager;
    private final Scanner scanner;
    private double atmBalance;

    public ATM(double initialBalance) {
        accountManager = new AccountManager();
        scanner = new Scanner(System.in);
        atmBalance = initialBalance;
    }

    public void start() {
        while (true) {
            System.out.println("Введите номер карты (формат XXXX-XXXX-XXXX-XXXX) или 'exit' для выхода:");
            String cardNumber = scanner.nextLine();

            if (cardNumber.equalsIgnoreCase("exit")) {
                System.out.println("Завершение работы.");
                break;
            }

            if (!accountManager.isFormatCardNumber(cardNumber)) {
                System.out.println("Неверный формат номера карты.");
                continue;
            }

            if (!accountManager.isValidCardNumber(cardNumber)) {
                System.out.println("Неверный номер карты.");
                continue;
            }

            Account account = accountManager.getAccount(cardNumber);
            if (account == null) {
                System.out.println("Карта не найдена.");
                continue;
            }

            if (account.isLocked()) {
                System.out.println("Карта заблокирована на 24 часа. Повторите попытку позже.");
                continue;
            }

            System.out.println("Введите ПИН-код:");
            boolean authenticated = false;
            for (int i = 0; i < MAX_PIN_ATTEMPTS; i++) {
                String pin = scanner.nextLine();
                if (account.authenticate(pin)) {
                    authenticated = true;
                    break;
                } else {
                    System.out.println("Неверный ПИН-код.");
                }
            }

            if (!authenticated) {
                account.lock();
                System.out.println("Карта заблокирована на 24 часа.");
                continue;
            }

            showMenu(account);
        }
    }

    private void showMenu(Account account) {
        while (true) {
            System.out.println("Выберите действие:");
            System.out.println("1. Проверить баланс");
            System.out.println("2. Снять средства");
            System.out.println("3. Пополнить баланс");
            System.out.println("4. Выход");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.println("Текущий баланс: " + account.getBalance());
                    break;
                case "2":
                    withdraw(account);
                    break;
                case "3":
                    deposit(account);
                    break;
                case "4":
                    accountManager.saveAccounts();
                    return;
                default:
                    System.out.println("Неверный выбор. Повторите попытку.");
            }
        }
    }

    private void withdraw(Account account) {
        System.out.println("Введите сумму для снятия:");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (amount > account.getBalance()) {
                System.out.println("Недостаточно средств на счете.");
            } else if (amount > atmBalance) {
                System.out.println("Недостаточно средств в банкомате.");
            } else {
                if (account.withdraw(amount)) {
                    atmBalance -= amount;
                    System.out.println("Снятие успешно. Текущий баланс: " + account.getBalance());
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат суммы.");
        }
    }

    private void deposit(Account account) {
        System.out.println("Введите сумму для пополнения (не более 1 000 000):");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (amount > 1000000) {
                System.out.println("Сумма превышает допустимый лимит.");
            } else {
                account.deposit(amount);
                System.out.println("Пополнение успешно. Текущий баланс: " + account.getBalance());
            }
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат суммы.");
        }
    }
}
