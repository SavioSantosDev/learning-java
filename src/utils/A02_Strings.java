package src.utils;

public class A02_Strings {
    public static void main(String[] args) {
        /*
        No java, as Strings são objetos que representam uma sequência de caracteres e são utilizadas para armazenas e manipular textos.
         */

        /*
        Strings em Java são imutáveis, ou seja, uma vez criadas, seu valor não pode ser alterado. Qualquer operação que
        pareça modificar uma string na verdade cria uma nova string. Isso é diferente de outras linguagens de programação
        onde as strings podem ser mutáveis. A imutabilidade das strings em Java traz benefícios de segurança e desempenho,
        pois permite que o Java otimize o uso de memória e evite efeitos colaterais indesejados em código concorrente.
         */
        System.out.println("Demonstrating String Immutability");
        String string1 = "Hello";
        string1 = string1 + " World"; // Isso cria uma nova string, não altera a original
        System.out.println(string1); // Hello World


        System.out.println("\nDemostrating String Pool");
        /*
        O String Pool no Java é uma área especial da memória dentro do Heap onde as Strings literais são armazenadas.
        Ele é usado para otimizar o usuo de memória, garantindo que apenas uma instância de cada string literal seja criada
        e reutilizada.

        Quando uma string é criada como literal (por exemplo, String str = "Hello";), o Java verifica se já existe uma string
        com o mesmo valor no String Pool. Se existir, a referência dessa string é reunilizada; caso contrário, a string é
        adicionada ao pool.

        Strings criadas com o operador new (por exemplo, String str = new String("Hello");) não são armazenadas no String Pool,
        mas podem ser adicionadas manualmente usando o método intern().
         */
        // Criando duas strings literais. Ambas serão armazenadas no String Pool.
        String str1 = "Java";
        String str2 = "Java";

        // Comparando as referências. Como ambas estão no String Pool, elas compartilham a mesma referência.
        System.out.println(str1 == str2); // true

        // Criando uma string usando o operador 'new'. Isso cria um novo objeto na heap, fora do String Pool.
        String str3 = new String("Java");

        // Comparando as referências. str1 está no String Pool, mas str3 está fora dele.
        System.out.println(str1 == str3); // false

        // Usando o método intern() para adicionar str3 ao String Pool.
        String str4 = str3.intern(); // str3 continua sendo um objeto fora do String Pool, mas str4 agora é uma referência ao mesmo objeto que str1.
        String str5 = new String("Java").intern();

        // Comparando as referências novamente. Agora str4 compartilha a mesma referência de str1 no String Pool.
        System.out.println(str1 == str3); // false
        System.out.println(str1 == str4); // true
        System.out.println(str1 == str5); // true

        System.out.println("\nPerformance Test");
        System.out.println("Concatenating strings with + operator");
        long initialTime = System.currentTimeMillis();
        concatStrings(100_000);
        long finalTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (finalTime - initialTime) + " milliseconds");

        System.out.println("\nConcatenating strings with StringBuilder");
        initialTime = System.currentTimeMillis();
        concatStringWithStringBuilder(100_000);
        finalTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (finalTime - initialTime) + " milliseconds");

        /*
        A concatenação utilizando StringBuilder é mais rápida porque ela evita criação de múltiplos objetos na heap, algo
        que ocorre quando utilizamos o operador +. O StringBuilder utiliza um buffer interno que permite adicionar
        caracteres sem criar novos objetos a cada concatenação. Isso reduz o overhead de memória e melhora o desempenho
        em operações de concatenação de strings, especialmente quando lidamos com grandes quantidades de dados.

        - Usando o operador + tem complexidade O(n²), pois cada operação copia o conteúdo acumulado anteriormente.
        - Com StringBuilder tem complexidade O(n), já que o buffer interno é ajustado dinamicamente e não há cópias desnecessárias.
         */
    }

    public static void concatStrings(int tamanho) {
        System.out.println("Concatenating string of size: " + tamanho);
        String texto = "";
        for (int i = 0; i < tamanho; i++) {
            texto += i;
        }
    }

    public static void concatStringWithStringBuilder(int tamanho) {
        System.out.println("Concatenating string of size: " + tamanho);
        StringBuilder texto = new StringBuilder();
        for (int i = 0; i < tamanho; i++) {
            texto.append(i);
        }
    }
}
