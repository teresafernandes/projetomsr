package br.edu.ufrn.projetomsr.dominio;

import java.math.BigDecimal;

public class QuantidadeIssues {

	public QuantidadeIssues(){
		issuesRealizadas = BigDecimal.ZERO;
		issuesAtrasadas = BigDecimal.ZERO;
		issuesCriadas = BigDecimal.ZERO;
	}
	private BigDecimal issuesRealizadas;
	private BigDecimal issuesAtrasadas;
	private BigDecimal issuesCriadas;
	
	private BigDecimal issuesBug;
	private BigDecimal issuesBugAtrasadas;
	
	public BigDecimal getIssuesRealizadas() {
		if(issuesRealizadas == null) return BigDecimal.ZERO;
		return issuesRealizadas;
	}
	public void setIssuesRealizadas(BigDecimal issuesRealizadas) {
		this.issuesRealizadas = issuesRealizadas;
	}
	public BigDecimal getIssuesAtrasadas() {
		if(issuesAtrasadas == null) return BigDecimal.ZERO;
		return issuesAtrasadas;
	}
	public void setIssuesAtrasadas(BigDecimal issuesAtrasadas) {
		this.issuesAtrasadas = issuesAtrasadas;
	}
	public BigDecimal getIssuesCriadas() {
		if(issuesCriadas == null) return BigDecimal.ZERO;
		return issuesCriadas;
	}
	public void setIssuesCriadas(BigDecimal issuesCriadas) {
		this.issuesCriadas = issuesCriadas;
	}
	
	public void incrementarIssuesRealizadas() {
		this.issuesRealizadas = getIssuesRealizadas().add(BigDecimal.ONE);
	}
	public void incrementarIssuesAtrasadas() {
		this.issuesAtrasadas = getIssuesAtrasadas().add(BigDecimal.ONE);
	}
	public void incrementarIssuesCriadas() {
		this.issuesCriadas = getIssuesCriadas().add(BigDecimal.ONE);
	}
	public void incrementarIssuesBug() {
		this.issuesBug = getIssuesBug().add(BigDecimal.ONE);
	}
	public void incrementarIssuesBugAtrasadas() {
		this.issuesBugAtrasadas = getIssuesBugAtrasadas().add(BigDecimal.ONE);
	}
	public BigDecimal getIssuesBug() {
		if(issuesBug == null) return BigDecimal.ZERO;
		return issuesBug;
	}
	public void setIssuesBug(BigDecimal issuesBug) {
		this.issuesBug = issuesBug;
	}
	public BigDecimal getIssuesBugAtrasadas() {
		if(issuesBugAtrasadas == null) return BigDecimal.ZERO;
		return issuesBugAtrasadas;
	}
	public void setIssuesBugAtrasadas(BigDecimal issuesBugAtrasadas) {
		this.issuesBugAtrasadas = issuesBugAtrasadas;
	}
	
	
	
}
