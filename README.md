### Passo a Passo para Criação de um Aplicativo CLI com Java e Spring Boot para Cadastro de Tarefas

#### Pré-requisitos:

1. **Java Development Kit (JDK)**: Instale a versão mais recente do JDK.
2. **Maven ou Gradle**: Ferramenta de build para gerenciar dependências.
3. **IDE**: IntelliJ IDEA, Eclipse, ou qualquer outra de sua preferência.
4. **Git**: Para controle de versão (opcional).

#### Passo 1: Configurar o Projeto Spring Boot

1. Acesse [Spring Initializr](https://start.spring.io/).
2. Configure o projeto:
    - **Project**: Maven Project (ou Gradle Project)
    - **Language**: Java
    - **Spring Boot**: Escolha a versão mais recente estável.
    - **Project Metadata**:
        - **Group**: com.exemplo
        - **Artifact**: cadastro-tarefas
        - **Name**: CadastroTarefas
        - **Package Name**: com.exemplo.cadastro.tarefas
    - **Dependencies**: Adicione as dependências necessárias:
        - Spring Boot DevTools
        - Spring Web
        - Spring Data JPA
        - H2 Database (para fins de teste e desenvolvimento)

3. Clique em "Generate" para baixar o projeto e extraia o arquivo ZIP.

#### Passo 2: Configurar o Banco de Dados

1. No arquivo `application.properties`, configure o banco de dados H2:
    ```properties
    spring.datasource.url=jdbc:h2:mem:testdb
    spring.datasource.driverClassName=org.h2.Driver
    spring.datasource.username=sa
    spring.datasource.password=password
    spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
    spring.h2.console.enabled=true
    ```

#### Passo 3: Criar a Entidade Tarefa

1. Crie um pacote `model` dentro de `src/main/java/com/exemplo/cadastro/tarefas/`.
2. Dentro do pacote `model`, crie a classe `Tarefa`:
    ```java
    package com.exemplo.cadastro.tarefas.model;

    import javax.persistence.Entity;
    import javax.persistence.GeneratedValue;
    import javax.persistence.GenerationType;
    import javax.persistence.Id;

    @Entity
    public class Tarefa {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String descricao;
        private boolean concluida;

        // Getters e setters
    }
    ```

#### Passo 4: Criar o Repositório de Tarefas

1. Crie um pacote `repository` dentro de `src/main/java/com/exemplo/cadastro/tarefas/`.
2. Dentro do pacote `repository`, crie a interface `TarefaRepository`:
    ```java
    package com.exemplo.cadastro.tarefas.repository;

    import com.exemplo.cadastro.tarefas.model.Tarefa;
    import org.springframework.data.jpa.repository.JpaRepository;

    public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    }
    ```

#### Passo 5: Criar o Serviço de Tarefas

1. Crie um pacote `service` dentro de `src/main/java/com/exemplo/cadastro/tarefas/`.
2. Dentro do pacote `service`, crie a classe `TarefaService`:
    ```java
    package com.exemplo.cadastro.tarefas.service;

    import com.exemplo.cadastro.tarefas.model.Tarefa;
    import com.exemplo.cadastro.tarefas.repository.TarefaRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.List;
    import java.util.Optional;

    @Service
    public class TarefaService {

        @Autowired
        private TarefaRepository tarefaRepository;

        public Tarefa criarTarefa(Tarefa tarefa) {
            return tarefaRepository.save(tarefa);
        }

        public List<Tarefa> listarTarefas() {
            return tarefaRepository.findAll();
        }

        public Optional<Tarefa> buscarTarefa(Long id) {
            return tarefaRepository.findById(id);
        }

        public void excluirTarefa(Long id) {
            tarefaRepository.deleteById(id);
        }
    }
    ```

#### Passo 6: Criar o Controlador CLI

1. Crie um pacote `cli` dentro de `src/main/java/com/exemplo/cadastro/tarefas/`.
2. Dentro do pacote `cli`, crie a classe `TarefaCLI`:
    ```java
    package com.exemplo.cadastro.tarefas.cli;

    import com.exemplo.cadastro.tarefas.model.Tarefa;
    import com.exemplo.cadastro.tarefas.service.TarefaService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.CommandLineRunner;
    import org.springframework.stereotype.Component;

    import java.util.Scanner;

    @Component
    public class TarefaCLI implements CommandLineRunner {

        @Autowired
        private TarefaService tarefaService;

        @Override
        public void run(String... args) {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("1. Criar tarefa");
                System.out.println("2. Listar tarefas");
                System.out.println("3. Buscar tarefa por ID");
                System.out.println("4. Excluir tarefa");
                System.out.println("5. Sair");
                int opcao = scanner.nextInt();
                scanner.nextLine();  // Consome a nova linha

                switch (opcao) {
                    case 1:
                        System.out.println("Digite a descrição da tarefa:");
                        String descricao = scanner.nextLine();
                        Tarefa tarefa = new Tarefa();
                        tarefa.setDescricao(descricao);
                        tarefa.setConcluida(false);
                        tarefaService.criarTarefa(tarefa);
                        System.out.println("Tarefa criada!");
                        break;
                    case 2:
                        System.out.println("Lista de tarefas:");
                        for (Tarefa t : tarefaService.listarTarefas()) {
                            System.out.println(t.getId() + ": " + t.getDescricao() + " - " + (t.isConcluida() ? "Concluída" : "Pendente"));
                        }
                        break;
                    case 3:
                        System.out.println("Digite o ID da tarefa:");
                        Long id = scanner.nextLong();
                        scanner.nextLine();  // Consome a nova linha
                        Optional<Tarefa> tarefaOptional = tarefaService.buscarTarefa(id);
                        if (tarefaOptional.isPresent()) {
                            Tarefa t = tarefaOptional.get();
                            System.out.println(t.getId() + ": " + t.getDescricao() + " - " + (t.isConcluida() ? "Concluída" : "Pendente"));
                        } else {
                            System.out.println("Tarefa não encontrada.");
                        }
                        break;
                    case 4:
                        System.out.println("Digite o ID da tarefa:");
                        Long idExcluir = scanner.nextLong();
                        scanner.nextLine();  // Consome a nova linha
                        tarefaService.excluirTarefa(idExcluir);
                        System.out.println("Tarefa excluída!");
                        break;
                    case 5:
                        System.out.println("Saindo...");
                        return;
                    default:
                        System.out.println("Opção inválida.");
                }
            }
        }
    }
    ```

#### Passo 7: Executar o Projeto

1. Execute o projeto usando sua IDE ou pelo terminal:
    ```sh
    mvn spring-boot:run
    ```
2. O CLI será iniciado e você poderá interagir com ele conforme as opções do menu.

Pronto! Você criou um aplicativo CLI com Java e Spring Boot para cadastro de tarefas.