package br.edu.ufrn.projetomsr;

import br.edu.ufrn.projetomsr.util.AvaliacaoQuestaoTres;
import br.edu.ufrn.projetomsr.util.AvaliacaoQuestaoUm;

/** 
 * Esta classe realiza minerações de dados no Projeto Gepe-Manager.
 * 
 * @see <a href="https://github.com/4Soft/gepe-manager">Gepe-Manager</a> 
 * @author Renan
 */
public class MineracaoGepeManager {
	
	private static final String NOME_REPOSITORIO = "4Soft/gepe-manager";
	
	/**
	 * Avalia a questão 1 da mineração de dados no projeto Gepe-Manager.
	 */
	public static void avaliarQuestaoUm(){
		AvaliacaoQuestaoUm.minerarQuestaoUm(NOME_REPOSITORIO);
	}
	
	public static void avaliarQuestaoTres(){
		AvaliacaoQuestaoTres.minerarQuestaoTres(NOME_REPOSITORIO);
	}
	
	public static void main(String[] args) {
		avaliarQuestaoTres();
	}
	
}
