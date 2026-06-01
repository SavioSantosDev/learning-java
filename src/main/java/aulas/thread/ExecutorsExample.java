package aulas.thread;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class ExecutorsExample {

    static void main() throws InterruptedException, ExecutionException {
        System.out.println("=== 1. FixedThreadPool ===");
        demoFixedThreadPool();

        System.out.println("\n=== 2. CachedThreadPool ===");
        demoCachedThreadPool();

        System.out.println("\n=== 3. SingleThreadExecutor ===");
        demoSingleThreadExecutor();

        System.out.println("\n=== 4. ScheduledThreadPool ===");
        demoScheduledThreadPool();

        System.out.println("\n=== 5. Callable + Future ===");
        demoCallableFuture();

        System.out.println("\n=== 6. invokeAll ===");
        demoInvokeAll();

        System.out.println("\n=== 7. invokeAny ===");
        demoInvokeAny();

        System.out.println("\n=== 8. CompletableFuture ===");
        demoCompletableFuture();
    }

    // FixedThreadPool: pool com número fixo de threads.
    // Tarefas extras ficam na fila até uma thread ficar disponível.
    static void demoFixedThreadPool() {
        // Java 19+: ExecutorService implementa AutoCloseable.
        // close() chama shutdown() e awaitTermination() automaticamente.
        try (ExecutorService executor = Executors.newFixedThreadPool(3)) {
            for (int i = 1; i <= 6; i++) {
                final int taskId = i;
                executor.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + ": Tarefa " + taskId + " iniciada");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println(Thread.currentThread().getName() + ": Tarefa " + taskId + " concluída");
                });
            }
        }
    }

    // CachedThreadPool: cria threads sob demanda e as reutiliza.
    // Threads ociosas por 60s são encerradas. Ideal para muitas tarefas curtas.
    static void demoCachedThreadPool() {
        try (ExecutorService executor = Executors.newCachedThreadPool()) {
            for (int i = 1; i <= 5; i++) {
                final int taskId = i;
                executor.execute(() -> System.out.println(Thread.currentThread().getName() + ": Tarefa rápida " + taskId));
            }
        }
    }

    // SingleThreadExecutor: garante execução sequencial em uma única thread.
    // Todas as tarefas são executadas na ordem de submissão.
    static void demoSingleThreadExecutor() {
        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
            for (int i = 1; i <= 4; i++) {
                final int taskId = i;
                executor.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + ": Tarefa sequencial " + taskId);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
        }
    }

    // ScheduledThreadPool: agendamento de tarefas com delay ou em intervalos fixos.
    static void demoScheduledThreadPool() throws InterruptedException {
        try (ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2)) {
            // Executa uma vez após 1 segundo de delay
            scheduler.schedule(
                () -> System.out.println(Thread.currentThread().getName() + ": Tarefa agendada com delay de 1s"),
                1, TimeUnit.SECONDS
            );

            // Executa a cada 500ms, começando após 500ms
            ScheduledFuture<?> periodicTask = scheduler.scheduleAtFixedRate(
                () -> System.out.println(Thread.currentThread().getName() + ": Tarefa periódica (a cada 500ms)"),
                500, 500, TimeUnit.MILLISECONDS
            );

            Thread.sleep(2200);
            periodicTask.cancel(false);
            System.out.println("Tarefa periódica cancelada.");
        }
    }

    // Callable retorna um resultado (diferente de Runnable).
    // Future permite recuperar o resultado ou aguardar a conclusão da tarefa.
    static void demoCallableFuture() throws InterruptedException, ExecutionException {
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Callable<Integer> somatorio = () -> {
                System.out.println(Thread.currentThread().getName() + ": Calculando somatório de 1 a 100...");
                Thread.sleep(300);
                int soma = 0;
                for (int i = 1; i <= 100; i++) soma += i;
                return soma;
            };

            Callable<String> mensagem = () -> {
                System.out.println(Thread.currentThread().getName() + ": Gerando mensagem...");
                Thread.sleep(200);
                return "Olá do Callable!";
            };

            Future<Integer> futureInt = executor.submit(somatorio);
            Future<String> futureStr = executor.submit(mensagem);

            // get() bloqueia até o resultado ficar disponível
            System.out.println("Resultado somatório: " + futureInt.get());
            System.out.println("Resultado mensagem: " + futureStr.get());
        }
    }

    // invokeAll: submete uma lista de Callables e aguarda todos terminarem.
    // Retorna uma lista de Futures já concluídos.
    static void demoInvokeAll() throws InterruptedException, ExecutionException {
        try (ExecutorService executor = Executors.newFixedThreadPool(3)) {
            List<Callable<String>> tarefas = Arrays.asList(
                () -> { Thread.sleep(300); return "Resultado A"; },
                () -> { Thread.sleep(100); return "Resultado B"; },
                () -> { Thread.sleep(200); return "Resultado C"; }
            );

            List<Future<String>> futures = executor.invokeAll(tarefas);

            for (Future<String> future : futures) {
                System.out.println("invokeAll resultado: " + future.get());
            }
        }
    }

    // invokeAny: submete uma lista de Callables e retorna o resultado
    // do primeiro que concluir com sucesso. As demais são canceladas.
    static void demoInvokeAny() throws InterruptedException, ExecutionException {
        try (ExecutorService executor = Executors.newFixedThreadPool(3)) {
            List<Callable<String>> tarefas = Arrays.asList(
                () -> { Thread.sleep(500); return "Servidor A respondeu"; },
                () -> { Thread.sleep(100); return "Servidor B respondeu (mais rápido!)"; },
                () -> { Thread.sleep(300); return "Servidor C respondeu"; }
            );

            String primeiro = executor.invokeAny(tarefas);
            System.out.println("invokeAny: " + primeiro);
        }
    }

    // CompletableFuture: permite encadear operações assíncronas de forma fluente.
    // Não depende de ExecutorService diretamente, mas usa o ForkJoinPool comum por padrão.
    static void demoCompletableFuture() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> future = CompletableFuture
            .supplyAsync(() -> {
                System.out.println(Thread.currentThread().getName() + ": Buscando preço...");
                try { Thread.sleep(300); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                return 150;
            })
            .thenApply(preco -> {
                int desconto = preco - 30;
                System.out.println(Thread.currentThread().getName() + ": Aplicando desconto: " + preco + " -> " + desconto);
                return desconto;
            })
            .thenApply(preco -> {
                int comFrete = preco + 15;
                System.out.println(Thread.currentThread().getName() + ": Adicionando frete: " + preco + " -> " + comFrete);
                return comFrete;
            });

        System.out.println("Preço final: R$" + future.get());

        // exceptionally: tratamento de erros na cadeia
        CompletableFuture<String> comErro = CompletableFuture
            .supplyAsync(() -> {
                if (true) throw new RuntimeException("Falha ao buscar dados!");
                return "dados";
            })
            .exceptionally(ex -> {
                System.out.println("Erro capturado: " + ex.getMessage());
                return "valor padrão";
            });

        System.out.println("Resultado com fallback: " + comErro.get());
    }
}
