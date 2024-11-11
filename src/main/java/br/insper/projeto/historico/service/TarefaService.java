package br.insper.projeto.historico.service;

import br.insper.projeto.historico.model.Tarefa;
import br.insper.projeto.historico.repository.TarefaRepository;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;

    public TarefaService(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    public Tarefa criarTarefa(Tarefa tarefa) {
        tarefa.setDataCriacao(LocalDate.now());
        return tarefaRepository.save(tarefa);
    }

    public void deletarTarefa(String id) {
        tarefaRepository.deleteById(id);
    }

    public List<Tarefa> listarTarefas() {
        return tarefaRepository.findAll();
    }

    public Tarefa obterTarefaPorId(String id) {
        return tarefaRepository.findById(id)
                  .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
    }
}
