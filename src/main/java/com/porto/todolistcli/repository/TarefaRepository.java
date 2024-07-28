package com.porto.todolistcli.repository;

import com.porto.todolistcli.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
}
