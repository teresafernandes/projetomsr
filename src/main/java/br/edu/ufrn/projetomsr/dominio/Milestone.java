package br.edu.ufrn.projetomsr.dominio;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHUser;


/**
 * Classe que guarda algumas informações adicionais sobre milestones.
 *  
 * @author Renan
 */
public class Milestone {
	
	private String titulo;
	
	private Date dataPrazo;

	/** Indica a porcentagem de conclusão de um milestone no momento em que ele encerrou seu prazo. */
	private double porcConclusaoFechamento;
	
	private List<GHIssue> issuesAtrasadas;

	private List<GHIssue> bugIssues;
	
	private int qtdTotalIssues;
	
	/** 
	 * Valor utilizado pela questão 2. Serve de base para identificar se as issues foram prejudicadas
	 * por causa de bugs. Quanto maior a nota, pior a classificação.
	 */
	private double notaBugs;
	
	private Date criadoEm;
	
	private Map<GHUser, QuantidadeIssues> issuesPorContribuidor;
	
	public Milestone() {
		issuesPorContribuidor = new LinkedHashMap<GHUser, QuantidadeIssues>();
	}
	
	@Override
	public String toString() {
		return titulo;
	}

	public double getPorcConclusaoFechamento() {
		return porcConclusaoFechamento;
	}

	public void setPorcConclusaoFechamento(double porcConclusaoFechamento) {
		this.porcConclusaoFechamento = porcConclusaoFechamento;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public List<GHIssue> getIssuesAtrasadas() {
		return issuesAtrasadas;
	}

	public void setIssuesAtrasadas(List<GHIssue> issuesAtrasadas) {
		this.issuesAtrasadas = issuesAtrasadas;
	}

	public Date getDataPrazo() {
		return dataPrazo;
	}

	public void setDataPrazo(Date dataPrazo) {
		this.dataPrazo = dataPrazo;
	}

	public List<GHIssue> getBugIssues() {
		return bugIssues;
	}

	public void setBugIssues(List<GHIssue> bugIssues) {
		this.bugIssues = bugIssues;
	}

	public int getQtdTotalIssues() {
		return qtdTotalIssues;
	}

	public void setQtdTotalIssues(int qtdTotalIssues) {
		this.qtdTotalIssues = qtdTotalIssues;
	}

	public double getNotaBugs() {
		return notaBugs;
	}

	public void setNotaBugs(double notaBugs) {
		this.notaBugs = notaBugs;
	}

	public Date getCriadoEm() {
		return criadoEm;
	}

	public void setCriadoEm(Date criadoEm) {
		this.criadoEm = criadoEm;
	}

	public Map<GHUser, QuantidadeIssues> getIssuesPorContribuidor() {
		return issuesPorContribuidor;
	}

	public void setIssuesPorContribuidor(
			Map<GHUser, QuantidadeIssues> issuesPorContribuidor) {
		this.issuesPorContribuidor = issuesPorContribuidor;
	}	
	

	public double getDesvioPadraoIssues() {
		if(issuesPorContribuidor != null){
			StandardDeviation desvioPadrao = new StandardDeviation();
			return desvioPadrao.evaluate(getArrayDistribuicaoIssues());
		}
		return 0;
	}

	
	public double[] getArrayDistribuicaoIssues(){
		if(issuesPorContribuidor != null){
			double[] distribuicao = new double[issuesPorContribuidor.keySet().size()];
			int i = 0;
			for(GHUser u : issuesPorContribuidor.keySet())
				distribuicao[i++] = issuesPorContribuidor.get(u).getIssuesRealizadas().doubleValue();
			
			return distribuicao;
		}
		return null;	
	}

	/** Retorna a quantidade de issues atrasadas no milestone.
	 * Utilizado somente na avaliação da questão três, pois depende do map de issues por contribuidor*/
	public double getQtdIssuesAtrasadas() {
		double issuesAtrasadas = 0;
		if(issuesPorContribuidor != null){
			for(GHUser u : issuesPorContribuidor.keySet())
				issuesAtrasadas += issuesPorContribuidor.get(u).getIssuesAtrasadas().doubleValue();
		}
		return issuesAtrasadas;
	}
	

}
