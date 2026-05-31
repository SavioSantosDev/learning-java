package aulas.thread;

public class DeadLockExample {

    // Dois recursos que serão usados como locks
    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    static void main() {
        // Thread 1: tenta adquirir lock1 primeiro, depois lock2
        Thread thread1 = new Thread(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + ": Tentando adquirir lock1...");
            
            synchronized (lock1) {
                System.out.println(threadName + ": Adquiriu lock1. Tentando adquirir lock2...");
                
                // Pequena pausa para aumentar a chance de deadlock
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                synchronized (lock2) {
                    System.out.println(threadName + ": Adquiriu lock2. Executando operação crítica.");
                }
            }
            
            System.out.println(threadName + ": Liberou todos os locks.");
        }, "Thread-1");

        // Thread 2: tenta adquirir lock2 primeiro, depois lock1
        Thread thread2 = new Thread(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + ": Tentando adquirir lock2...");
            
            synchronized (lock2) {
                System.out.println(threadName + ": Adquiriu lock2. Tentando adquirir lock1...");
                
                // Pequena pausa para aumentar a chance de deadlock
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                synchronized (lock1) {
                    System.out.println(threadName + ": Adquiriu lock1. Executando operação crítica.");
                }
            }
            
            System.out.println(threadName + ": Liberou todos os locks.");
        }, "Thread-2");

        // Iniciar as threads
        thread1.start();
        thread2.start();

        // Timeout para detectar deadlock (após 5 segundos)
        try {
            Thread.sleep(5000);
            
            if (thread1.isAlive() || thread2.isAlive()) {
                System.out.println("\n=== DEADLOCK DETECTADO ===");
                System.out.println("As threads estão bloqueadas esperando uma pela outra.");
                System.out.println("Thread-1 está aguardando: lock2");
                System.out.println("Thread-2 está aguardando: lock1");
                System.out.println("Para encerrar, pressione Ctrl+C");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
