package br.edu.ufrn.projetomsr;

import br.edu.ufrn.projetomsr.util.AvaliacaoQuestaoDois;
import br.edu.ufrn.projetomsr.util.AvaliacaoQuestaoTres;
import br.edu.ufrn.projetomsr.util.AvaliacaoQuestaoUm;

/** 
 * Esta classe realiza minerações de dados no projeto informado através do parâmetro da classe.
 * 
 * @author Teresa
 */
public abstract class MineracaoGenericManager {
	
	private String nomeRepositorio ;
	
	public MineracaoGenericManager(String repositorio){
		this.nomeRepositorio = repositorio;
	}
	
	/**
	 * Avalia a questão 1 da mineração de dados no projeto.
	 */
	public  void avaliarQuestaoUm(){
		AvaliacaoQuestaoUm.minerarQuestaoUm(nomeRepositorio);
	}
	
	/**
	 * Avalia a questão 2 da mineração de dados no projeto.
	 */
	public  void avaliarQuestaoDois(){
		AvaliacaoQuestaoDois.minerarQuestaoDois(nomeRepositorio);
	}
	
	/**
	 * Avalia a questão 2 da mineração de dados no projeto.
	 */
	public  void avaliarQuestaoTres(){
		AvaliacaoQuestaoTres.minerarQuestaoTres(nomeRepositorio);
	}
	
	public String getNomeRepositorio() {
		return nomeRepositorio;
	}	
	
}
