package br.insper.projeto.historico.controller;

import br.insper.projeto.historico.model.Tarefa;
import br.insper.projeto.historico.service.TarefaService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/tarefa")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @PostMapping
    public ResponseEntity<Tarefa> criarTarefa(@RequestBody Tarefa tarefa) {
        Tarefa novaTarefa = tarefaService.criarTarefa(tarefa);
        return ResponseEntity.ok(novaTarefa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarTarefa(@PathVariable String id) {
        tarefaService.deletarTarefa(id);
        return ResponseEntity.ok("Tarefa deletada com sucesso");
    }

    @GetMapping
    public ResponseEntity<List<Tarefa>> listarTarefas() {
        List<Tarefa> tarefas = tarefaService.listarTarefas();
        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> detalhesTarefa(@PathVariable String id) {
        Tarefa tarefa = tarefaService.obterTarefaPorId(id);
        return ResponseEntity.ok(tarefa);
    }
}