package br.edu.ufrn.projetomsr;

import br.edu.ufrn.projetomsr.util.AvaliacaoQuestaoUm;

/** 
 * Esta classe realiza minerações de dados no Projeto OpenRes.
 * 
 * @see <a href="https://github.com/IMD-UFRN/OpenRes">OpenRes</a> 
 * @author Renan
 */
public class MineracaoOpenRes {
	
	private static final String NOME_REPOSITORIO = "IMD-UFRN/OpenRes";
	
	/**
	 * Avalia a questão 1 da mineração de dados no projeto OpenRes.
	 */
	public static void avaliarQuestaoUm(){
		AvaliacaoQuestaoUm.minerarQuestaoUm(NOME_REPOSITORIO);
	}
	
	public static void main(String[] args) {
		avaliarQuestaoUm();
	}

}
