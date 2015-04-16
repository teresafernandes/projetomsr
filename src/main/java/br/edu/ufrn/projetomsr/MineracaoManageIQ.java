package br.edu.ufrn.projetomsr;

import br.edu.ufrn.projetomsr.util.AvaliacaoQuestaoUm;

/** 
 * Esta classe realiza minerações de dados no Projeto ManageIQ.
 * 
 * @see <a href="https://github.com/ManageIQ/manageiq">ManageIQ</a> 
 * @author Renan
 */
public class MineracaoManageIQ {
	
	private static final String NOME_REPOSITORIO = "ManageIQ/manageiq";
	
	/**
	 * Avalia a questão 1 da mineração de dados no projeto ManageIQ.
	 */
	public static void avaliarQuestaoUm(){
		AvaliacaoQuestaoUm.minerarQuestaoUm(NOME_REPOSITORIO);
	}
	
	public static void main(String[] args) {
		avaliarQuestaoUm();
	}

}
