package aulas.thread;

class Account {
    private double balance;

    public Account(double initialBalance) {
        this.balance = initialBalance;
    }

    public synchronized void withdraw(double amount) {
        System.out.println(Thread.currentThread().getName() + " tentando sacar: " + amount);
        
        if (balance >= amount) {
            System.out.println(Thread.currentThread().getName() + " - Saldo antes do saque: " + balance);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            balance -= amount;
            System.out.println(Thread.currentThread().getName() + " - Sacou: " + amount + " - Novo saldo: " + balance);
            System.out.println("=================================");
        } else {
            System.out.println(Thread.currentThread().getName() + " - Saldo insuficiente! Saldo atual: " + balance);
        }
    }

    public double getBalance() {
        return balance;
    }
}

public class ThreadSincronismExample {
    static void main() throws InterruptedException {
        Account account = new Account(1000.0);
        
        System.out.println("Saldo inicial da conta: " + account.getBalance());
        System.out.println("Iniciando threads de saque...\n");

        Thread thread1 = new Thread(() -> account.withdraw(500.0), "Thread-1");
        Thread thread2 = new Thread(() -> account.withdraw(500.0), "Thread-2");
        Thread thread3 = new Thread(() -> account.withdraw(500.0), "Thread-3");

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        System.out.println("\nSaldo final da conta: " + account.getBalance());
    }
}
