package br.edu.ufrn.projetomsr.util;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHLabel;
import org.kohsuke.github.GHMilestoneState;

public class IssuesUtil {

	public static boolean isIssueAtrasada(GHIssue issue){
		//Devem ser avaliados apenas os milestones já fechados
		if (issue.getMilestone().getState() == GHMilestoneState.OPEN)
			return false;
		Date dataPrazoMS = CalendarUtils.removeTime(issue.getMilestone().getDueOn());
		//Não há como avaliar milestones sem prazos ou cujos prazos ainda não se venceram
		if (dataPrazoMS == null || dataPrazoMS.after(CalendarUtils.removeTime(new Date())))
			return false;
		//Caso existam issues abertas, então automaticamente já estão atrasadas, neste caso.
		if (issue.getState() == GHIssueState.OPEN)
			return true;
		//Avaliando issues fechadas do milestone
		if (issue.getState() == GHIssueState.CLOSED && CalendarUtils.removeTime(issue.getClosedAt()).after(dataPrazoMS))
			return true;		
		return false;			
	}
	
	public static boolean isIssueBug(GHIssue issue) {
		String[] labelsErro = {"Error", "Bug"};
		try {
			Collection<GHLabel> labels = issue.getLabels();
			for(GHLabel l: labels){
				for(String s: labelsErro){
					if(l.getName().toUpperCase().contains(s.toUpperCase()))
						return true;
				}				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
}
