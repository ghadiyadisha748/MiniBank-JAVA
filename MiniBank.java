import java.util.Scanner;

public class MiniBank {

    record bankinfo(String name, String branch) {
    }

    enum menuoption {
        OPEN_ACCOUNT, DEPOSIT, WITHDRAW, TRANSFER, EXIT
    }

    // Custom AutoCloseable resource for try-with-resources demonstration (Practical 7)
    static class BankingSession implements AutoCloseable {
        private String sessionName;

        public BankingSession(String sessionName) {
            this.sessionName = sessionName;
            System.out.println("  [Session] Started: " + sessionName);
        }

        public void performAudit() {
            System.out.println("  [Session] Performing secure transaction audit...");
        }

        @Override
        public void close() {
            System.out.println("  [Session] Closed automatically: " + sessionName + " (Resources cleared)");
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("================================================================");
        System.out.println("        MINIBANK — OBJECT ORIENTED PROGRAMMING PRACTICALS       ");
        System.out.println("================================================================");

        // ==========================================================
        // PRACTICAL 3: Validation, Command Parsing & Statement Formatting
        // ==========================================================
        System.out.println("\n>>> PRACTICAL 3: Validation, Command Parsing & Statement <<<");

        System.out.println("\n[1] Validator Tests:");
        System.out.println("  Mobile Valid (9876543210)  : " + Validator.isValidMobile("9876543210"));
        System.out.println("  Mobile Invalid (12345)     : " + Validator.isValidMobile("12345"));
        System.out.println("  Email Valid (disha@aiml.com): " + Validator.isValidEmail("disha@aiml.com"));
        System.out.println("  Email Invalid (disha@)     : " + Validator.isValidEmail("disha@"));
        System.out.println("  PAN Valid (ABCDE1234F)     : " + Validator.isValidPan("ABCDE1234F"));
        System.out.println("  PAN Invalid (12345XYZ)     : " + Validator.isValidPan("12345XYZ"));
        System.out.println("  IFSC Valid (SBIN0123456)   : " + Validator.isValidIfsc("SBIN0123456"));
        System.out.println("  IFSC Invalid (SBIN123)     : " + Validator.isValidIfsc("SBIN123"));
        System.out.println("  Amount Valid (500)         : " + Validator.isValidAmount("500"));
        System.out.println("  Amount Invalid (-50)       : " + Validator.isValidAmount("-50"));

        System.out.println("\n[2] Command Parsing Test:");
        String commandInput = "DEPOSIT AC0001 500";
        System.out.println("  Input Line: \"" + commandInput + "\"");
        Command parsedCmd = CommandParser.parse(commandInput);
        if (parsedCmd != null) {
            System.out.println("  Command Type   : " + parsedCmd.type());
            System.out.println("  Account Number : " + parsedCmd.accountNumber());
            System.out.println("  Amount         : " + parsedCmd.amount());
        }

        System.out.println("\n[3] Command Parsing Error Handling Tests:");
        System.out.print("  Wrong parts  : ");
        CommandParser.parse("DEPOSIT 500");
        System.out.print("  Invalid type : ");
        CommandParser.parse("BUY AC0001 500");
        System.out.print("  Invalid amt  : ");
        CommandParser.parse("WITHDRAW AC0001 -100");

        // ==========================================================
        // PRACTICAL 4: Abstract Classes, Inheritance & Polymorphism
        // ==========================================================
        System.out.println("\n>>> PRACTICAL 4: Abstract Classes, Inheritance & Polymorphism <<<");

        Account[] accounts = new Account[3];
        accounts[0] = new SavingsAccount("Disha", 5000, 1000);
        accounts[1] = new CurrentAccount("Yashashvi", 10000, 5000);
        accounts[2] = new FixedDepositAccount("Bhavyata", 50000);

        System.out.println("\n[1] Polymorphic Account Array & Interest Calculation:");
        for (Account acc : accounts) {
            System.out.println("  " + acc);
            System.out.println("    Type             : " + acc.getClass().getSimpleName());
            System.out.println("    Interest Rate    : " + acc.interestRate() + "%");
            System.out.println("    Monthly Interest : Rs. " + String.format("%.2f", acc.monthlyInterest()));
        }

        System.out.println("\n[2] Pattern Matching with instanceof:");
        for (Account acc : accounts) {
            if (acc instanceof SavingsAccount sa) {
                System.out.println("  SavingsAccount detected for " + sa.getOwnerName()
                        + " | Min Balance: Rs. " + sa.getMinBalance());
            } else if (acc instanceof CurrentAccount ca) {
                System.out.println("  CurrentAccount detected for " + ca.getOwnerName()
                        + " | Overdraft Limit: Rs. " + ca.getOverdraftLimit());
            } else if (acc instanceof FixedDepositAccount fda) {
                System.out.println("  FixedDepositAccount detected for " + fda.getOwnerName()
                        + " | Locked Deposit");
            }
        }

        // StatementFormatter from Practical 3 tested with Account from Practical 4
        System.out.println("\n[3] StatementFormatter buildStatement():");
        System.out.println(StatementFormatter.buildStatement(accounts[0]));

        // ==========================================================
        // PRACTICAL 5: Interfaces, Functional Interface & Marker Interface
        // ==========================================================
        System.out.println("\n>>> PRACTICAL 5: Interfaces, Functional Interface & Marker Interface <<<");

        System.out.println("\n[1] InterestBearing Default Method (yearlyInterest):");
        for (Account acc : accounts) {
            if (acc instanceof InterestBearing ib) {
                System.out.println("  " + acc.getOwnerName() + " (" + acc.getClass().getSimpleName() + "): "
                        + "Yearly Interest = Rs. " + String.format("%.2f", ib.yearlyInterest()));
            }
        }

        System.out.println("\n[2] Functional Interface WithdrawRule (Anonymous Class vs Lambda):");
        WithdrawRule maxLimitRule = new WithdrawRule() {
            @Override
            public boolean allow(Account account, long amount) {
                return amount <= 5000;
            }
        };

        WithdrawRule activeAndPositiveRule = (account, amount) -> account.isActive() && amount > 0;

        System.out.println("  Anonymous Class (max limit <= 5000) for Rs. 4000: "
                + maxLimitRule.allow(accounts[0], 4000));
        System.out.println("  Anonymous Class (max limit <= 5000) for Rs. 8000: "
                + maxLimitRule.allow(accounts[0], 8000));
        System.out.println("  Lambda Expression (active & positive) for Rs. 1000: "
                + activeAndPositiveRule.allow(accounts[0], 1000));

        System.out.println("\n[3] Marker Interface Premium Check:");
        for (Account acc : accounts) {
            if (acc instanceof Premium) {
                System.out.println("  [PREMIUM ACCOUNT] " + acc.getAccountNumber()
                        + " (" + acc.getOwnerName() + " - " + acc.getClass().getSimpleName() + ")");
            } else {
                System.out.println("  [STANDARD ACCOUNT] " + acc.getAccountNumber()
                        + " (" + acc.getOwnerName() + " - " + acc.getClass().getSimpleName() + ")");
            }
        }

        // ==========================================================
        // PRACTICAL 6: Custom Annotations & Reflection
        // ==========================================================
        System.out.println("\n>>> PRACTICAL 6: Custom Annotations & Reflection <<<");

        Account invalidAccount = new SavingsAccount("DishaVeryLongOwnerName", -1500, 1000);
        System.out.println("\nValidating object with custom annotations via reflection:");
        System.out.println("Object: " + invalidAccount);

        String[] validationErrors = AnnotationValidator.validate(invalidAccount);
        if (validationErrors.length == 0) {
            System.out.println("  No validation errors found.");
        } else {
            System.out.println("  Validation Errors Found (" + validationErrors.length + "):");
            for (String err : validationErrors) {
                System.out.println("    - " + err);
            }
        }

        Account validAccount = new SavingsAccount("Disha", 5000, 1000);
        String[] validErrors = AnnotationValidator.validate(validAccount);
        System.out.println("Validating correctly configured account (" + validAccount.getOwnerName() + "):");
        System.out.println("  Errors count: " + validErrors.length + " (Passed all checks)");

        // ==========================================================
        // PRACTICAL 7: Custom Exceptions & Fault-Tolerant MiniBank
        // ==========================================================
        System.out.println("\n>>> PRACTICAL 7: Custom Exceptions & Fault Tolerance <<<");

        Account testSavings = new SavingsAccount("Disha", 5000, 1000);
        Account testCurrent = new CurrentAccount("Yashashvi", 2000, 3000);

        System.out.println("\n[1] Testing Successful Deposit:");
        try {
            testSavings.deposit(1500);
            System.out.println("  Deposit Rs. 1500 successful! New Balance: Rs. " + testSavings.getBalance());
        } catch (InvalidAmountException e) {
            System.out.println("  Deposit failed: " + e.getMessage());
        }

        System.out.println("\n[2] Testing Invalid Deposit Amount (<= 0):");
        try {
            testSavings.deposit(-200);
        } catch (InvalidAmountException e) {
            System.out.println("  Caught expected exception: " + e.getClass().getSimpleName() + " -> " + e.getMessage());
        }

        System.out.println("\n[3] Testing Successful Withdrawal:");
        try {
            testSavings.withdraw(2000);
            System.out.println("  Withdraw Rs. 2000 successful! New Balance: Rs. " + testSavings.getBalance());
        } catch (BankException e) {
            System.out.println("  Withdrawal failed: " + e.getMessage());
        }

        System.out.println("\n[4] Testing Insufficient Funds Withdrawal (Violating Min Balance):");
        try {
            testSavings.withdraw(4000);
        } catch (InsufficientFundsException e) {
            System.out.println("  Caught expected exception: " + e.getClass().getSimpleName());
            System.out.println("  Message   : " + e.getMessage());
            System.out.println("  Shortfall : Rs. " + e.getShortfall());
            System.out.println("  Balance preserved: Rs. " + testSavings.getBalance());
        } catch (InvalidAmountException e) {
            System.out.println("  Invalid amount: " + e.getMessage());
        }

        System.out.println("\n[5] Testing Invalid Withdrawal Amount (<= 0):");
        try {
            testSavings.withdraw(0);
        } catch (InvalidAmountException e) {
            System.out.println("  Caught expected exception: " + e.getClass().getSimpleName() + " -> " + e.getMessage());
        } catch (InsufficientFundsException e) {
            System.out.println("  Insufficient funds: " + e.getMessage());
        }

        System.out.println("\n[6] Testing Successful Fund Transfer:");
        try {
            System.out.println("  Before Transfer:");
            System.out.println("    Source (" + testSavings.getOwnerName() + ") Balance: Rs. " + testSavings.getBalance());
            System.out.println("    Dest   (" + testCurrent.getOwnerName() + ") Balance: Rs. " + testCurrent.getBalance());

            testSavings.transfer(testCurrent, 1000);

            System.out.println("  Transfer Rs. 1000 Completed Successfully!");
            System.out.println("  After Transfer:");
            System.out.println("    Source (" + testSavings.getOwnerName() + ") Balance: Rs. " + testSavings.getBalance());
            System.out.println("    Dest   (" + testCurrent.getOwnerName() + ") Balance: Rs. " + testCurrent.getBalance());
        } catch (BankException e) {
            System.out.println("  Transfer error: " + e.getMessage());
        }

        System.out.println("\n[7] Testing Failed Transfer (Insufficient Funds):");
        try {
            testSavings.transfer(testCurrent, 5000);
        } catch (BankException e) {
            System.out.println("  Caught expected exception: " + e.getClass().getSimpleName() + " -> " + e.getMessage());
            System.out.println("  Source balance safely intact: Rs. " + testSavings.getBalance());
            System.out.println("  Destination balance safely intact: Rs. " + testCurrent.getBalance());
        } finally {
            System.out.println("  [finally block] Transfer attempt transaction audit recorded.");
        }

        System.out.println("\n[8] Try-With-Resources Demonstration with AutoCloseable:");
        try (BankingSession session = new BankingSession("Audit-Session-2026")) {
            session.performAudit();
        } catch (Exception e) {
            System.out.println("  Session error: " + e.getMessage());
        }

        // ==========================================================
        // PRACTICAL 8: Multithreading and Synchronization
        // ==========================================================
        System.out.println("\n>>> PRACTICAL 8: Multithreading & Synchronization <<<");

        // 1. Unsafe deposit demonstration (Illustrating Race Condition)
        System.out.println("\n[1] Unsafe Deposit Demonstration (Demonstrating Race Condition):");
        Account raceAccount = new SavingsAccount("RaceDemo", 0, 0);
        int raceThreadsCount = 10;
        int raceDepositsPerThread = 50;
        long raceAmountPerDeposit = 10;
        long raceExpectedBalance = raceThreadsCount * raceDepositsPerThread * raceAmountPerDeposit;

        Thread[] raceThreads = new Thread[raceThreadsCount];
        for (int i = 0; i < raceThreadsCount; i++) {
            raceThreads[i] = new Thread(
                    new AccountWorker(raceAccount, raceDepositsPerThread, raceAmountPerDeposit, true),
                    "UnsafeWorker-" + (i + 1));
            raceThreads[i].start();
        }

        for (Thread t : raceThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + e.getMessage());
            }
        }

        System.out.println("  Unsafe Deposit Test Results:");
        System.out.println("    Expected Balance : Rs. " + raceExpectedBalance);
        System.out.println("    Actual Balance   : Rs. " + raceAccount.getBalance());
        if (raceAccount.getBalance() != raceExpectedBalance) {
            System.out.println("    Observation      : RACE CONDITION OCCURRED! Lost updates due to unsynchronized access.");
        } else {
            System.out.println("    Observation      : Race condition did not manifest in this run (timing dependent).");
        }

        // 2. Thread-safe synchronized deposit verification
        System.out.println("\n[2] Thread-Safe Synchronized Deposit Test (Verification):");
        Account syncAccount = new SavingsAccount("SyncDemo", 0, 0);
        int syncThreadsCount = 10;
        int syncDepositsPerThread = 1000;
        long syncAmountPerDeposit = 10;
        long syncExpectedBalance = (long) syncThreadsCount * syncDepositsPerThread * syncAmountPerDeposit; // 100,000

        Thread[] syncThreads = new Thread[syncThreadsCount];
        for (int i = 0; i < syncThreadsCount; i++) {
            syncThreads[i] = new Thread(
                    new AccountWorker(syncAccount, syncDepositsPerThread, syncAmountPerDeposit, false),
                    "SyncWorker-" + (i + 1));
            syncThreads[i].start();
        }

        for (Thread t : syncThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + e.getMessage());
            }
        }

        System.out.println("\n  Synchronized Test Results:");
        System.out.println("    Initial Balance  : Rs. 0");
        System.out.println("    Threads Count    : " + syncThreadsCount);
        System.out.println("    Deposits/Thread  : " + syncDepositsPerThread);
        System.out.println("    Deposit Amount   : Rs. " + syncAmountPerDeposit);
        System.out.println("    Expected Balance : Rs. " + syncExpectedBalance);
        System.out.println("    Actual Balance   : Rs. " + syncAccount.getBalance());
        System.out.println("    Test Status      : "
                + (syncAccount.getBalance() == syncExpectedBalance ? "PASSED (Thread-Safe)" : "FAILED"));

        // ==========================================================
        // PRACTICAL 9: Thread Pools, Producer-Consumer & Deadlock
        // ==========================================================
        System.out.println("\n>>> PRACTICAL 9: Thread Pools, Producer-Consumer & Deadlock <<<");

        // 1. Thread Pool using TransactionProcessor
        System.out.println("\n[1] Thread Pool Execution with TransactionProcessor:");
        Account poolAccount = new SavingsAccount("PoolAcc", 5000, 500);
        TransactionProcessor processor = new TransactionProcessor();

        // Submit multiple deposit and withdrawal tasks
        processor.submit(() -> {
            try {
                poolAccount.deposit(1000);
                System.out.println("  [PoolTask] Deposited Rs. 1000. Balance: Rs. " + poolAccount.getBalance());
            } catch (InvalidAmountException e) {
                System.out.println("  [PoolTask] Deposit failed: " + e.getMessage());
            }
        });

        processor.submit(() -> {
            try {
                poolAccount.withdraw(1500);
                System.out.println("  [PoolTask] Withdrawn Rs. 1500. Balance: Rs. " + poolAccount.getBalance());
            } catch (BankException e) {
                System.out.println("  [PoolTask] Withdrawal failed: " + e.getMessage());
            }
        });

        processor.submit(() -> {
            try {
                poolAccount.deposit(2500);
                System.out.println("  [PoolTask] Deposited Rs. 2500. Balance: Rs. " + poolAccount.getBalance());
            } catch (InvalidAmountException e) {
                System.out.println("  [PoolTask] Deposit failed: " + e.getMessage());
            }
        });

        processor.submit(() -> {
            try {
                poolAccount.withdraw(500);
                System.out.println("  [PoolTask] Withdrawn Rs. 500. Balance: Rs. " + poolAccount.getBalance());
            } catch (BankException e) {
                System.out.println("  [PoolTask] Withdrawal failed: " + e.getMessage());
            }
        });

        processor.stop();
        System.out.println("  ExecutorService shutdown cleanly. Final Pool Account Balance: Rs. " + poolAccount.getBalance());

        // 2. Producer-Consumer Pattern using wait() & notify()
        System.out.println("\n[2] Producer-Consumer Pattern using wait() and notify():");
        TransactionBuffer buffer = new TransactionBuffer(3);
        Account bufferAccount = new SavingsAccount("BufferAcc", 2000, 100);

        Thread producerThread = new Thread(() -> {
            Command[] transactions = {
                new Command(TransactionType.DEPOSIT, "AC0001", 500),
                new Command(TransactionType.DEPOSIT, "AC0001", 300),
                new Command(TransactionType.WITHDRAW, "AC0001", 400),
                new Command(TransactionType.DEPOSIT, "AC0001", 1000),
                new Command(TransactionType.WITHDRAW, "AC0001", 200)
            };

            for (Command cmd : transactions) {
                try {
                    buffer.produce(cmd);
                    System.out.println("  [Producer] Produced: " + cmd.type() + " Rs. " + cmd.amount());
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            buffer.finish();
            System.out.println("  [Producer] Finished producing all transactions.");
        }, "Producer-Thread");

        Thread consumerThread = new Thread(() -> {
            while (true) {
                try {
                    Command cmd = buffer.consume();
                    if (cmd == null) {
                        break; // Buffer is empty and producer finished
                    }

                    if (cmd.type() == TransactionType.DEPOSIT) {
                        bufferAccount.deposit(cmd.amount());
                    } else if (cmd.type() == TransactionType.WITHDRAW) {
                        bufferAccount.withdraw(cmd.amount());
                    }

                    System.out.println("  [Consumer] Processed: " + cmd.type() + " Rs. " + cmd.amount()
                            + " | New Balance: Rs. " + bufferAccount.getBalance());
                    Thread.sleep(80);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (BankException e) {
                    System.out.println("  [Consumer] Transaction error: " + e.getMessage());
                }
            }
            System.out.println("  [Consumer] Processed all transactions and terminated.");
        }, "Consumer-Thread");

        producerThread.start();
        consumerThread.start();

        try {
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted: " + e.getMessage());
        }

        System.out.println("  Final Buffer Account Balance: Rs. " + bufferAccount.getBalance());

        // 3. Deadlock Demonstration & Safe Ordered Lock Transfer
        System.out.println("\n[3] Deadlock Demonstration & Safe Ordered-Lock Transfer:");
        Account deadA = new SavingsAccount("AC9001", "DemoA", 1000, 0);
        Account deadB = new SavingsAccount("AC9002", "DemoB", 1000, 0);

        System.out.println("  Demonstrating Potential Deadlock Scenario (Locking in Opposite Order):");
        Thread deadlockThread1 = new Thread(() -> {
            synchronized (deadA) {
                System.out.println("    [Deadlock-T1] Acquired lock on Account A (" + deadA.getAccountNumber() + "). Waiting for B...");
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {}
                synchronized (deadB) {
                    System.out.println("    [Deadlock-T1] Acquired lock on Account B.");
                }
            }
        }, "DeadlockThread-1");

        Thread deadlockThread2 = new Thread(() -> {
            synchronized (deadB) {
                System.out.println("    [Deadlock-T2] Acquired lock on Account B (" + deadB.getAccountNumber() + "). Waiting for A...");
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {}
                synchronized (deadA) {
                    System.out.println("    [Deadlock-T2] Acquired lock on Account A.");
                }
            }
        }, "DeadlockThread-2");

        deadlockThread1.setDaemon(true);
        deadlockThread2.setDaemon(true);
        deadlockThread1.start();
        deadlockThread2.start();

        try {
            deadlockThread1.join(300);
            deadlockThread2.join(300);
        } catch (InterruptedException ignored) {}

        if (deadlockThread1.isAlive() && deadlockThread2.isAlive()) {
            System.out.println("    -> DEADLOCK DETECTED! Thread-1 holds A waiting for B; Thread-2 holds B waiting for A.");
            System.out.println("    -> Circular wait condition confirmed (Threads are daemon and safely contained).");
        }

        System.out.println("\n  Applying Consistent Lock-Ordering Solution (Safe Transfer):");
        Account accountA = new SavingsAccount("AC0001", "Alice", 5000, 0);
        Account accountB = new SavingsAccount("AC0002", "Bob", 5000, 0);

        System.out.println("  Initial Balances: Alice (AC0001) = Rs. " + accountA.getBalance()
                + " | Bob (AC0002) = Rs. " + accountB.getBalance());
        System.out.println("  Total System Money: Rs. " + (accountA.getBalance() + accountB.getBalance()));

        Thread safeThread1 = new Thread(() -> {
            try {
                accountA.safeTransfer(accountB, 1000);
                System.out.println("    [SafeTransfer-1] Transferred Rs. 1000 from Alice to Bob.");
            } catch (BankException e) {
                System.out.println("    [SafeTransfer-1] Error: " + e.getMessage());
            }
        }, "SafeTransfer-1");

        Thread safeThread2 = new Thread(() -> {
            try {
                accountB.safeTransfer(accountA, 1000);
                System.out.println("    [SafeTransfer-2] Transferred Rs. 1000 from Bob to Alice.");
            } catch (BankException e) {
                System.out.println("    [SafeTransfer-2] Error: " + e.getMessage());
            }
        }, "SafeTransfer-2");

        safeThread1.start();
        safeThread2.start();

        try {
            safeThread1.join();
            safeThread2.join();
        } catch (InterruptedException e) {
            System.out.println("Safe transfer interrupted: " + e.getMessage());
        }

        System.out.println("  Final Balances: Alice = Rs. " + accountA.getBalance()
                + " | Bob = Rs. " + accountB.getBalance());
        System.out.println("  Total System Money Preserved: Rs. " + (accountA.getBalance() + accountB.getBalance())
                + " (No deadlock occurred, locks acquired in consistent order)");

        // ==========================================================
        // Interactive Banking Menu (Preserved from Practical 1 & 2)
        // ==========================================================
        bankinfo bank = new bankinfo("MiniBank", "Sarthana Branch");
        boolean running = true;

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

            if (!sc.hasNextInt()) {
                break;
            }
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

        System.out.println("\nThank you for using MiniBank!");
        sc.close();
    }
}
