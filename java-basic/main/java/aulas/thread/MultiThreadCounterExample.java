package aulas.thread;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class MultiThreadCounterExample {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Unsafe Counter (sem sincronismo) ===");
        runTest(new UnsafeCounter());

        System.out.println("\n=== Synchronized Counter ===");
        runTest(new SynchronizedCounter());

        System.out.println("\n=== AtomicInteger Counter ===");
        runTest(new AtomicCounter());

        System.out.println("\n=== ReentrantLock Counter ===");
        runTest(new ReentrantLockCounter());
    }

    static void runTest(Counter counter) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) counter.increment();
        }, "Thread-1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) counter.increment();
        }, "Thread-2");

        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) counter.increment();
        }, "Thread-3");

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println("Resultado final: " + counter.getCount() + " (esperado: 30000)");
    }

    interface Counter {
        void increment();

        int getCount();
    }

    // Sem sincronismo - race condition esperada
    static class UnsafeCounter implements Counter {
        private int count = 0;

        @Override
        public void increment() {
            count++;
        }

        @Override
        public int getCount() {
            return count;
        }
    }

    // Sincronizado com synchronized
    static class SynchronizedCounter implements Counter {
        private int count = 0;

        @Override
        public synchronized void increment() {
            count++;
        }

        @Override
        public synchronized int getCount() {
            return count;
        }
    }


    // Usando AtomicInteger
    static class AtomicCounter implements Counter {
        private final AtomicInteger count = new AtomicInteger(0);

        @Override
        public void increment() {
            count.incrementAndGet();
        }

        @Override
        public int getCount() {
            return count.get();
        }
    }

    // Usando ReentrantLock
    static class ReentrantLockCounter implements Counter {
        private final ReentrantLock lock = new ReentrantLock();
        private int count = 0;

        @Override
        public void increment() {
            lock.lock();
            try {
                count++;
            } finally { // garantir que o lock seja liberado
                lock.unlock();
            }
        }

        @Override
        public int getCount() {
            lock.lock();
            try {
                return count;
            } finally {
                lock.unlock();
            }
        }
    }
}
