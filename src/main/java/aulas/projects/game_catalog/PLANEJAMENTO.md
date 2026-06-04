# Planejamento — CRUD JDBC com Flyway e Terminal

## Tema da Aplicação

**Biblioteca de Jogos** — um usuário cadastra **Jogos** (`Game`), cada jogo pertence a um **Gênero** (`Genre`, tabela de
domínio pré-populada) e possui uma lista de **Plataformas** associadas (`Platform`, relação many-to-many via tabela
associativa `game_platform`).

---

## Modelo de Dados

### Tabelas

```
genre (domínio — pré-populada)
├── id   SERIAL PK
└── name VARCHAR(50) NOT NULL

game (entidade principal)
├── id           SERIAL PK
├── title        VARCHAR(150) NOT NULL
├── release_year INT
├── genre_id     FK → genre(id)
└── description  TEXT

platform (domínio da lista — pré-populada)
├── id   SERIAL PK
└── name VARCHAR(50) NOT NULL

game_platform (tabela de junção — one game to many platforms)
├── game_id     FK → game(id)
└── platform_id FK → platform(id)
    PK(game_id, platform_id)
```

### Relações

- `game` → `genre` : N:1 (cada jogo tem exatamente 1 gênero)
- `game` ↔ `platform` : N:M via `game_platform` (um jogo pode rodar em várias plataformas)

---

## Scripts Flyway (resources/db/migration)

| Arquivo                    | Conteúdo                                                |
|----------------------------|---------------------------------------------------------|
| `V1__create_tables.sql`    | Criação de `genre`, `platform`, `game`, `game_platform` |
| `V2__seed_domain_data.sql` | INSERT em `genre` e `platform`                          |

> Flyway controla execuções via tabela `flyway_schema_history` — scripts já aplicados não são re-executados.

---

## Estrutura de Pacotes

```
aulas/jdbc/
├── Main.java                        ← ponto de entrada; inicializa Flyway e chama UI
├── config/
│   └── DatabaseConfig.java          ← lê db.properties, fornece Connection via DataSource simples
├── migration/
│   └── FlywayMigration.java         ← encapsula execução do Flyway
├── domain/
│   ├── Genre.java                   ← record: id, name
│   ├── Platform.java                ← record: id, name
│   └── Game.java                    ← record: id, title, releaseYear, genre, platforms, description
├── repository/
│   ├── GenreRepository.java         ← findAll()
│   ├── PlatformRepository.java      ← findAll()
│   └── GameRepository.java          ← findAll(page), findById, save, update, delete
└── ui/
    ├── ConsoleUI.java               ← loop principal com Scanner
    ├── GameListView.java            ← listagem paginada (5 por vez)
    ├── GameFormView.java            ← tela de cadastro/edição campo a campo
    └── GameDetailView.java          ← visualização detalhada de um jogo
```

---

## Dependências a Adicionar no `pom.xml`

| Dependência                               | Versão | Motivo                                        |
|-------------------------------------------|--------|-----------------------------------------------|
| `org.flywaydb:flyway-core`                | 10.x   | Migração de banco                             |
| `org.flywaydb:flyway-database-postgresql` | 10.x   | Suporte explícito ao PostgreSQL no Flyway 10+ |

> PostgreSQL e Lombok já estão no `pom.xml`.

---

## Fluxo da Aplicação (Terminal)

```
[Início]
  └─► FlywayMigration.run()           ← aplica migrações pendentes
  └─► ConsoleUI.start()

[Tela: Listagem de Jogos — página 1]
  Exibe: id | título | gênero | plataformas
  Opções:
    [N] Próxima página
    [P] Página anterior
    [A] Adicionar jogo
    [número] Selecionar jogo
    [0] Sair

[Tela: Cadastro de Jogo]
  Passo 1 → Digite o título:
  Passo 2 → Digite o ano de lançamento:
  Passo 3 → Descrição (opcional):
  Passo 4 → Escolha o gênero:
              1. Action   2. RPG   3. Strategy  ...
  Passo 5 → Escolha as plataformas (ex: 1,3):
              1. PC   2. PlayStation 5   3. Xbox ...
  Passo 6 → Confirmar? (S/N)
  └─► GameRepository.save(game)
  └─► volta para Listagem

[Tela: Detalhe do Jogo]
  Título: ...
  Ano: ...
  Gênero: ...
  Plataformas: ..., ...
  Descrição: ...
  Opções:
    [E] Editar
    [D] Deletar
    [V] Voltar

[Tela: Edição de Jogo]
  Mesmos passos do Cadastro, campos pré-preenchidos
  └─► GameRepository.update(game)
  └─► volta para Detalhe
```

---

## Camada de Repositório — Boas Práticas JDBC

- **`PreparedStatement`** em todas as queries com parâmetros → previne SQL Injection
- **Transações explícitas** (`conn.setAutoCommit(false)` + `commit`/`rollback`) no `save` e `update` do
  `GameRepository` (insert em `game` + inserts em `game_platform` devem ser atômicos)
- **`try-with-resources`** em toda `Connection`, `PreparedStatement` e `ResultSet`
- **`ResultSet` mapeado por nome de coluna**, não por índice, para legibilidade e resistência a mudanças de ordem
- **Paginação via `LIMIT/OFFSET`** com parâmetros bindados: `SELECT ... LIMIT ? OFFSET ?`
- **Busca por ID** retorna `Optional<Game>` com `LEFT JOIN` para trazer o gênero e as plataformas em uma única query (ou
  query + sub-query), montando o objeto completo em memória
- `DatabaseConfig` expõe um único método `getConnection()` reutilizável — sem pool, mas isolado para futura troca por
  HikariCP

---

## Checklist de Implementação

### Fase 1 — Infraestrutura

- [ ] Adicionar dependências Flyway no `pom.xml`
- [ ] Criar `DatabaseConfig.java`
- [ ] Criar `FlywayMigration.java`
- [ ] Criar `V1__create_tables.sql`
- [ ] Criar `V2__seed_domain_data.sql`
- [ ] Criar `Main.java`

### Fase 2 — Domínio e Repositórios

- [ ] Criar records `Genre`, `Platform`, `Game`
- [ ] Criar `GenreRepository` com `findAll()`
- [ ] Criar `PlatformRepository` com `findAll()`
- [ ] Criar `GameRepository` com `findAll(page)`, `findById`, `save`, `update`, `delete`

### Fase 3 — Interface de Terminal

- [ ] Criar `ConsoleUI` com loop principal e paginação
- [ ] Criar `GameListView` (listagem paginada)
- [ ] Criar `GameFormView` (cadastro e edição)
- [ ] Criar `GameDetailView` (visualização + ações)

### Fase 4 — Integração e Testes Manuais

- [ ] Testar migração Flyway do zero
- [ ] Testar CRUD completo pelo terminal
- [ ] Verificar paginação
- [ ] Verificar transação: rollback em caso de erro no `game_platform`

---

## Dúvidas / Decisões em Aberto

1. **Conexão**: manter `DriverManager` simples ou já adicionar HikariCP?
2. **`db.properties`**: já existe no projeto — usar o mesmo arquivo ou criar um separado para o módulo jdbc?
3. **Edição parcial**: ao editar, permitir deixar campo em branco para manter valor atual, ou sempre re-preencher tudo?
4. **Plataformas na listagem**: exibir como lista separada por vírgula ou só o count?
