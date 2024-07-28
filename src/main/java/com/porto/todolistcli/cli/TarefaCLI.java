package com.porto.todolistcli.cli;

import com.porto.todolistcli.model.Tarefa;
import com.porto.todolistcli.service.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Scanner;

@Component
public class TarefaCLI implements CommandLineRunner {
    @Autowired
    private TarefaService tarefaService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--------------------- Iniciando o App ---------------------");

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("1. Criar tarefa");
            System.out.println("2. Listar tarefas");
            System.out.println("3. Buscar tarefa por ID");
            System.out.println("4. Excluir tarefa");
            System.out.println("5. Sair");

            int opt = sc.nextInt();
            sc.nextLine();

            switch (opt) {
                case 1:
                    System.out.print("Digite a descrição da tarefa: ");
                    String descricao = sc.nextLine();
                    Tarefa tarefa = new Tarefa();
                    tarefa.setDescricao(descricao);
                    tarefa.setConcluida(false);
                    tarefaService.criarTarefa(tarefa);
                    System.out.println("Tarefa criada com sucesso!");
                    break;
                case 2:
                    System.out.println("Lista de tarefas:");
                    for (Tarefa t : tarefaService.listarTarefas()) {
                        System.out.println(t.getId() + ": " + t.getDescricao() + " - " + (t.getConcluida() ? "Concluída" : "Pendente"));
                    }
                    break;
                case 3:
                    System.out.println("Digite o ID da tarefa:");
                    Long id = sc.nextLong();
                    sc.nextLine();  // Consome a nova linha
                    Optional<Tarefa> tarefaOptional = tarefaService.buscarTarefa(id);
                    if (tarefaOptional.isPresent()) {
                        Tarefa t = tarefaOptional.get();
                        System.out.println(t.getId() + ": " + t.getDescricao() + " - " + (t.getConcluida() ? "Concluída" : "Pendente"));
                    } else {
                        System.out.println("Tarefa não encontrada.");
                    }
                    break;
                case 4:
                    System.out.println("Digite o ID da tarefa:");
                    Long idExcluir = sc.nextLong();
                    sc.nextLine();  // Consome a nova linha
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
