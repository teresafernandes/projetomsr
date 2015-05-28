package br.edu.ufrn.projetomsr.dominio;

import java.util.Date;
import java.util.List;

import org.kohsuke.github.GHIssue;


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
	
}
