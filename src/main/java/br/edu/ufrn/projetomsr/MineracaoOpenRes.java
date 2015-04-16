package br.edu.ufrn.projetomsr;


/** 
 * Esta classe realiza minerações de dados no Projeto OpenRes.
 * 
 * @see <a href="https://github.com/IMD-UFRN/OpenRes">OpenRes</a> 
 * @author Renan
 */
public class MineracaoOpenRes extends MineracaoGenericManager{
	
	private static final String NOME_REPOSITORIO = "IMD-UFRN/OpenRes";
	
	public MineracaoOpenRes() {
		super(NOME_REPOSITORIO);
	}
	

}
