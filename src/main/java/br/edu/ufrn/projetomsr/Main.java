package br.edu.ufrn.projetomsr;

public class Main {

	public static void main(String[] args) {
		MineracaoGenericManager m;
		
//		m = new MineracaoGepeManager();
//		m.avaliarQuestaoUm();
//		m.avaliarQuestaoDois();
//		m.avaliarQuestaoTres();

//		m = new MineracaoManageIQ();
//		m.avaliarQuestaoUm();
//		m.avaliarQuestaoDois();
//		m.avaliarQuestaoTres();
//		
		m = new MineracaoOpenRes();
//		m.avaliarQuestaoUm();
		m.avaliarQuestaoDois();
//		m.avaliarQuestaoTres();
	}

}
