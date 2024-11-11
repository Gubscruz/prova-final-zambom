package br.insper.projeto.historico.exception;

public class TarefaNotFoundException extends RuntimeException {

	public TarefaNotFoundException(String id) {
		super("Tarefa com o ID " + id + " não foi encontrada.");
	}
}
