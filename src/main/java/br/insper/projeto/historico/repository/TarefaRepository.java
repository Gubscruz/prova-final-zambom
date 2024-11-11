package br.insper.projeto.historico.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import br.insper.projeto.historico.model.Tarefa;

@Repository
public interface TarefaRepository extends MongoRepository<Tarefa, String> {
}