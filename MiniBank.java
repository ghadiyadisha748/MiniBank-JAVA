import java.util.Scanner;

public class MiniBank {

    record bankinfo(String name, String branch) {
    }

    enum menuoption {
        OPEN_ACCOUNT, DEPOSIT, WITHDRAW, TRANSFER, EXIT
    }

    static class customer implements Cloneable {

        private String name;
        private String email;
        private String mobile;
        private final String customerid;

        private static long customercnt = 100;
        private Address address;

        @Override
        public customer clone() {

            try {
                return (customer) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }

        public static class Address {

            private String line;
            private String city;
            private String pincode;

            public Address(String line, String city, String pincode) {
                this.line = line;
                this.city = city;
                this.pincode = pincode;
            }
        }

        private static String generateCustomerId() {
            customercnt++;
            return "CUST" + customercnt;
        }

        public customer(String name, String email, String mobile) {
            this.name = name;
            this.email = email;
            this.mobile = mobile;
            this.customerid = generateCustomerId();
        }

        public String getname() {
            return name;
        }

        public String getemail() {
            return email;
        }

        public String getmobile() {
            return mobile;
        }

        public String getCustomerId() {
            return customerid;
        }

        public Address getAddress() {
            return address;
        }
    }

    static class Account {

        private final String accountno;
        private String ownername;
        private long balance;
        private boolean active;

        private static long accountcnt = 0;

        @Override
        public String toString() {
            return accountno + " - " + ownername + " - Balance: " + balance;
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj)
                return true;

            if (!(obj instanceof Account))
                return false;

            Account other = (Account) obj;

            return accountno.equals(other.accountno);
        }

        @Override
        public int hashCode() {
            return accountno.hashCode();
        }

        private static String generateAccountNumber() {
            accountcnt++;
            return String.format("AC%04d", accountcnt);
        }

        public Account(String ownername, long balance) {
            this.accountno = generateAccountNumber();
            this.ownername = ownername;
            this.balance = balance;
            this.active = true;
        }

        public Account(String ownername) {
            this(ownername, 0);
        }

        public void deposit(long amount) {
            balance = balance + amount;
        }

        public boolean withdraw(long amount) {
            if (balance >= amount) {
                balance = balance - amount;
                return true;
            }

            return false;
        }

        public String getAccountNumber() {
            return accountno;
        }

        public String getOwnerName() {
            return ownername;
        }

        public long getBalance() {
            return balance;
        }

        public boolean isActive() {
            return active;
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Account[] account = new Account[3];

        account[0] = new Account("Disha", 5000);
        account[1] = new Account("Yashashvi", 10000);
        account[2] = new Account("Bhavyata", 2000);

        bankinfo bank = new bankinfo("MiniBank", "Sarthana Branch");

        boolean running = true;

        account[0].deposit(1000);

        account[1].withdraw(2000);

        account[2].deposit(500);

        System.out.println("\nAccount Details:");

        for (Account accountt : account) {
            System.out.println(
                    accountt.getAccountNumber() + " - "
                            + accountt.getOwnerName() + " - Balance: "
                            + accountt.getBalance());
        }
        System.out.println(account[0].toString());
        System.out.println(account[1].toString());
        System.out.println(account[2].toString());
        System.out.println(account[0].equals(account[1]));

        Object obj = account[0];

        if (obj instanceof Account) {
            System.out.println("Object is an Account");
        }

        while (running) {

            System.out.println("\n==============================");
            System.out.println(bank.name() + " - " + bank.branch());
            System.out.println("==============================");

            System.out.println("1.Open Account");
            System.out.println("2.Deposit");
            System.out.println("3.Withdraw");
            System.out.println("4.Transfer");
            System.out.println("5.Exit");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> System.out.println("open account");
                case 2 -> System.out.println("deposit");
                case 3 -> System.out.println("withdraw");
                case 4 -> System.out.println("transfer");
                case 5 -> {
                    System.out.println("Exit");
                    running = false;
                }
                default -> System.out.println("Invalid choice");
            }

        }

        System.out.println("\nthank you");

        sc.close();
    }

}
