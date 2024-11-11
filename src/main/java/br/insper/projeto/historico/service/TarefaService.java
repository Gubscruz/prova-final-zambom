package br.insper.projeto.historico.service;

import br.insper.projeto.historico.model.Tarefa;
import br.insper.projeto.historico.repository.TarefaRepository;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import br.insper.projeto.historico.exception.TarefaNotFoundException;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;

    public TarefaService(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    public Tarefa criarTarefa(Tarefa tarefa) {
        if (tarefa.getId() != null && tarefaRepository.existsById(tarefa.getId())) {
            throw new IllegalArgumentException("Tarefa com o ID " + tarefa.getId() + " já existe.");
        }
        tarefa.setDataCriacao(LocalDate.now());

        return tarefaRepository.save(tarefa);
    }

    public void deletarTarefa(String id) {
        if (!tarefaRepository.existsById(id)) {
            throw new TarefaNotFoundException(id);
        }

        tarefaRepository.deleteById(id);
    }

    public List<Tarefa> listarTarefas() {
        return tarefaRepository.findAll();
    }

    public Tarefa obterTarefaPorId(String id) {

        return tarefaRepository.findById(id)
                  .orElseThrow(() -> new TarefaNotFoundException(id));
    }
}

