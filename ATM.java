import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ATM {
    private List<User> users;
    private User currentUser;
    private Scanner scanner;

    public ATM() {
        users = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to the ATM!");

        boolean authenticated = false;
        while (!authenticated) {
            System.out.print("Enter User ID: ");
            String userId = scanner.nextLine();
            System.out.print("Enter User PIN: ");
            String pin = scanner.nextLine();

            authenticated = authenticateUser(userId, pin);

            if (!authenticated) {
                System.out.println("Invalid User ID or PIN. Please try again.");
            }
        }

        System.out.println("Authentication successful. ATM functionalities unlocked.");
        showMainMenu();
    }

    private boolean authenticateUser(String userId, String pin) {
        for (User user : users) {
            if (user.getUserId().equals(userId) && user.getPin().equals(pin)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    private void showMainMenu() {
        boolean quit = false;
        while (!quit) {
            System.out.println("---------- Main Menu ----------");
            System.out.println("1. Transactions History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        showTransactionsHistory();
                        break;
                    case "2":
                        performWithdrawal();
                        break;
                    case "3":
                        performDeposit();
                        break;
                    case "4":
                        performTransfer();
                        break;
                    case "5":
                        quit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input format. Please enter a valid choice.");
            }
        }

        System.out.println("Thank you for using the ATM. Goodbye!");
    }

    private void showTransactionsHistory() {
        System.out.println("---------- Transactions History ----------");
        List<Transaction> transactions = currentUser.getTransactions();
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (Transaction transaction : transactions) {
                System.out.println(transaction.toString());
            }
        }
        System.out.println("----------------------------------------");
    }

    private void performWithdrawal() {
        try {
            System.out.print("Enter the withdrawal amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            if (amount <= 0) {
                System.out.println("Invalid withdrawal amount. Please enter a positive value.");
                return;
            }

            if (currentUser.getBalance() >= amount) {
                currentUser.setBalance(currentUser.getBalance() - amount);
                Transaction transaction = new Transaction("Withdrawal", -amount);
                currentUser.addTransaction(transaction);
                System.out.println("Withdrawal successful. Your new balance is: " + currentUser.getBalance());
            } else {
                System.out.println("Insufficient funds. Withdrawal canceled.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format. Please enter a valid amount.");
        }
    }

    private void performDeposit() {
        try {
            System.out.print("Enter the deposit amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            if (amount <= 0) {
                System.out.println("Invalid deposit amount. Please enter a positive value.");
                return;
            }

            currentUser.setBalance(currentUser.getBalance() + amount);
            Transaction transaction = new Transaction("Deposit", amount);
            currentUser.addTransaction(transaction);
            System.out.println("Deposit successful. Your new balance is: " + currentUser.getBalance());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format. Please enter a valid amount.");
        }
    }

    private void performTransfer() {
        System.out.print("Enter the recipient's User ID: ");
        String recipientId = scanner.nextLine();

        User recipient = null;
        for (User user : users) {
            if (user.getUserId().equals(recipientId)) {
                recipient = user;
                break;
            }
        }

        if (recipient == null) {
            System.out.println("Recipient User ID not found. Transfer canceled.");
            return;
        }

        try {
            System.out.print("Enter the transfer amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            if (amount <= 0) {
                System.out.println("Invalid transfer amount. Please enter a positive value.");
                return;
            }

            if (currentUser.getBalance() >= amount) {
                currentUser.setBalance(currentUser.getBalance() - amount);
                recipient.setBalance(recipient.getBalance() + amount);
                Transaction transaction = new Transaction("Transfer to " + recipient.getUserId(), -amount);
                currentUser.addTransaction(transaction);
                transaction = new Transaction("Transfer from " + currentUser.getUserId(), amount);
                recipient.addTransaction(transaction);
                System.out.println("Transfer successful. Your new balance is: " + currentUser.getBalance());
            } else {
                System.out.println("Insufficient funds. Transfer canceled.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format. Please enter a valid amount.");
        }
    }

    public static void main(String[] args) {
        ATM atm = new ATM();
        atm.initializeUsers();
        atm.start();
    }

    private void initializeUsers() {
        users.add(new User("saiganesh", "1234", 100000));
        users.add(new User("tonystark", "5678", 5000));
        users.add(new User("brucebanner", "5678", 8000));
        // Add more users as needed
    }

    class User {
        private String userId;
        private String pin;
        private double balance;
        private List<Transaction> transactions;

        public User(String userId, String pin, double balance) {
            this.userId = userId;
            this.pin = pin;
            this.balance = balance;
            this.transactions = new ArrayList<>();
        }

        public String getUserId() {
            return userId;
        }

        public String getPin() {
            return pin;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public List<Transaction> getTransactions() {
            return transactions;
        }

        public void addTransaction(Transaction transaction) {
            transactions.add(transaction);
        }
    }

    class Transaction {
        private String type;
        private double amount;

        public Transaction(String type, double amount) {
            this.type = type;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return "Type: " + type + ", Amount: " + amount;
        }
    }
}
