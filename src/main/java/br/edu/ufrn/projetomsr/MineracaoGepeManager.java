package br.edu.ufrn.projetomsr;


/** 
 * Esta classe realiza minerações de dados no Projeto Gepe-Manager.
 * 
 * @see <a href="https://github.com/4Soft/gepe-manager">Gepe-Manager</a> 
 * @author Renan
 */
public class MineracaoGepeManager extends MineracaoGenericManager{
	
	private static final String NOME_REPOSITORIO = "4Soft/gepe-manager";
	
	public MineracaoGepeManager() {
		super(NOME_REPOSITORIO);
	}
	
}
