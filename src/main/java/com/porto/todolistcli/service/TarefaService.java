package com.porto.todolistcli.service;

import com.porto.todolistcli.model.Tarefa;
import com.porto.todolistcli.repository.TarefaRepository;
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
