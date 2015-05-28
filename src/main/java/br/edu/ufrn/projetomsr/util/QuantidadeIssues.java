package br.edu.ufrn.projetomsr.util;

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
		this.issuesRealizadas = issuesRealizadas.add(BigDecimal.ONE);
	}
	public void incrementarIssuesAtrasadas() {
		this.issuesAtrasadas = issuesAtrasadas.add(BigDecimal.ONE);
	}
	public void incrementarIssuesCriadas() {
		this.issuesCriadas = issuesCriadas.add(BigDecimal.ONE);
	}
	
}
