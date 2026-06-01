package aulas.thread;

import javax.swing.JOptionPane;
import java.util.LinkedList;
import java.util.Queue;

public class WaitNotifyEmailExample {

    static class EmailQueue {

        private final Queue<String> emails = new LinkedList<>();
        private boolean open = true;

        // wait() e notify() devem ser sempre chamados no mesmo objeto — que é o objeto cujo synchronized está sendo usado como lock.
        // Neste caso, o objeto é 'this' (a instância de EmailQueue).
        // Poderia ser na própria fila de emails, mas aí teria que usar synchonized(this.emails) { ... } o que não é recomendado

        synchronized String receiveEmail() {
            String threadName = Thread.currentThread().getName();

            while (open && emails.isEmpty()) {
                System.out.println(threadName + ": Nenhum email disponível. Aguardando...");
                try {
                    wait(); // mesma coisa que this.wait()
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (!open && emails.isEmpty()) {
                System.out.println(threadName + ": Fila fechada e sem emails. Encerrando.");
                return null;
            }

            String email = emails.poll();
            System.out.println(threadName + ": Email recebido: " + email);
            return email;
        }

        synchronized void addEmail(String email) {
            System.out.println("Main: Adicionando email: " + email);
            emails.add(email);
            notifyAll();
        }

        synchronized void close() {
            System.out.println("Main: Fechando a fila de emails.");
            open = false;
            notifyAll();
        }
    }

    static class EmailReceiver implements Runnable {

        private final EmailQueue emailQueue;

        EmailReceiver(EmailQueue emailQueue) {
            this.emailQueue = emailQueue;
        }

        @SuppressWarnings("BusyWait")
        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + ": Iniciando receptor de emails.");

            String email;
            while ((email = emailQueue.receiveEmail()) != null) {
                System.out.println(threadName + ": Processando email: " + email);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println(threadName + ": Email processado: " + email);
            }

            System.out.println(threadName + ": Receptor encerrado.");
        }
    }

    static void main() {
        EmailQueue emailQueue = new EmailQueue();

        Thread t1 = new Thread(new EmailReceiver(emailQueue), "Thread-1");
        Thread t2 = new Thread(new EmailReceiver(emailQueue), "Thread-2");
        Thread t3 = new Thread(new EmailReceiver(emailQueue), "Thread-3");

        t1.start();
        t2.start();
        t3.start();

        while (true) {
            String input = JOptionPane.showInputDialog("Digite o email (cancele ou deixe vazio para encerrar):");

            if (input == null || input.isEmpty()) {
                emailQueue.close();
                break;
            }

            emailQueue.addEmail(input);
        }
    }
}
