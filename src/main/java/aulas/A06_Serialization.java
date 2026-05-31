import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

public class A06_Serialization {
    static void main() {

        System.out.println("\n");
        System.out.println("=".repeat(20));
        System.out.println("Serialization");
        System.out.println("=".repeat(20));
        System.out.println("\n");

        Aluno aluno = new Aluno(1L, "João", "123");
        System.out.println("Aluno antes da serialização: " + aluno);

        serialize(aluno);
        Aluno alunoDeserializado = deserialize();
        System.out.println("Aluno depois da deserialização: " + alunoDeserializado);
    }

    private static void serialize(Aluno aluno) {
        Path path = Path.of("serialized/aluno.ser");
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path))) {
            oos.writeObject(aluno);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Aluno deserialize() {
        Path path = Path.of("serialized/aluno.ser");
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
            return (Aluno) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}

class Aluno implements Serializable {

    private static final long serialVersionUID = 2180395480345453088L;
    
    private Long id;
    private String nome;
    private transient String senha;

    public Aluno(Long id, String nome, String senha) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }

    @Override
    public String toString() {
        return "Aluno [id=" + id + ", nome=" + nome + ", senha=" + senha + "]";
    }
}
