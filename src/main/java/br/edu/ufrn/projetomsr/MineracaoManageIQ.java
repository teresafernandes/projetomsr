package br.edu.ufrn.projetomsr;


/** 
 * Esta classe realiza minerações de dados no Projeto ManageIQ.
 * 
 * @see <a href="https://github.com/ManageIQ/manageiq">ManageIQ</a> 
 * @author Renan
 */
public class MineracaoManageIQ extends MineracaoGenericManager{
	
	private static final String NOME_REPOSITORIO = "ManageIQ/manageiq";
	
	public MineracaoManageIQ() {
		super(NOME_REPOSITORIO);
	}


}
