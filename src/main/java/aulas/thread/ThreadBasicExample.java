package aulas.thread;

public class ThreadBasicExample {

    static void main() throws InterruptedException {
        // Cria e instancia 5 threads
        Thread thread1 = new Thread(new MyRunnable(), "Thread-1");
        Thread thread2 = new Thread(new MyRunnable(), "Thread-2");
        Thread thread3 = new Thread(new MyRunnable(), "Thread-3");
        Thread thread4 = new Thread(new MyRunnable(), "Thread-4");
        Thread thread5 = new Thread(new MyRunnable(), "Thread-5");

        // Chama start() de todas as threads
        thread1.start();
        thread1.join(); // espera a thread 1 finalizar

        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
    }

    // Classe interna que implementa a interface Runnable
    static class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " started");
            for (int i = 1; i <= 5; i++) {
                System.out.println(Thread.currentThread().getName() + " running: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Thread interrupted: " + e.getMessage());
                }
            }
            System.out.println(Thread.currentThread().getName() + " finished!");
        }
    }
}
