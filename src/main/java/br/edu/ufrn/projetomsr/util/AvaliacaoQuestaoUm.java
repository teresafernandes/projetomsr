package br.edu.ufrn.projetomsr.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHMilestone;
import org.kohsuke.github.GHMilestoneState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/**
 * Classe genérica utilizada para realizar a mineração de dados referente à questão 1,
 * para diferentes projetos.
 * 
 * A questão 1 é a seguinte: "As metas das Sprints estão sendo cumpridas?"
 * 
 * @author Renan
 */
public class AvaliacaoQuestaoUm {
	
	public static void minerarQuestaoUm(String repositorio){
		
		try {
			System.out.println("REPOSITÓRIO: " + repositorio);
			System.out.println("QUESTÃO 1: As metas das Sprints estão sendo cumpridas?");
			System.out.println("Minerando dados... Aguarde...");
			
			GitHub github = GitHub.connect();
			GHRepository repo = github.getRepository(repositorio);
			
			int i = 1;
			
			//Algumas variáveis a serem calculadas
			int milestonesSemPrazo = 0;
			int milestonesAvaliados = 0;
			int milestonesAbertos = 0;
			int totalMilestones = 0;
			
			int milestonesComAtraso = 0;
			int issuesAtrasadas = 0;
			int totalIssues = 0;
			
			int contadorTerminoLaco = 5;
			
			//Percorrendo os milestones.
			//A API do GitHub para Java não fornece uma maneira muito eficiente de percorrer os milestones
			//de um repositório. Por isso, busco pelos números deles até que não haja mais nenhum milestone,
			//já que os milestones são ordenados sequencialmente.
			while (true){
				
				GHMilestone ms = null;
				
				try {
					ms = repo.getMilestone(i);
					
				} catch (FileNotFoundException e){
					//Teoricamente, não existem mais milestones. Seria o fim do laço.
					//Porém, às vezes acontece de pular um dos índices. Acredito que é por causa de milestones removidos.
					//Sendo assim, para garantir, considero que terminou de percorrer os milestones apenas quando essa exceção é lançada 5 vezes seguidas.
					
					contadorTerminoLaco--;
					
					if (contadorTerminoLaco == 0)
						//Por 5 vezes seguidas, não encontrou um próximo milestone. Nesse caso, encerra. 
						break;
					else {
						//Continua o laço
						i++;
						continue;
					}
				}
				
				totalMilestones++;
				contadorTerminoLaco = 5;
				
				//Devem ser avaliados apenas os milestones já fechados
				if (ms.getState() == GHMilestoneState.OPEN){
					i++;
					milestonesAbertos++;
					continue;
				}
					
				
				Date dataPrazoMS = ms.getDueOn();
				
				if (dataPrazoMS == null || dataPrazoMS.after(new Date())){
					milestonesSemPrazo++;
					continue; //Não há como avaliar milestones sem prazos ou cujos prazos ainda não se venceram
				}
				
				//Avaliando milestone
				
				//Avaliando issues fechadas do milestone
				List<GHIssue> issuesFechadas = repo.getIssues(GHIssueState.CLOSED, ms);
				boolean possuiAtraso = false;
				
				if (issuesFechadas != null){
					for (GHIssue issue : issuesFechadas){
						totalIssues++;
						
						if (issue.getClosedAt().after(dataPrazoMS)){
							issuesAtrasadas++;
							possuiAtraso = true;
						}
					}
				}
				
				//Caso existam issues abertas, então automaticamente já estão atrasadas, neste caso.
				int qtdIssuesAbertas = ms.getOpenIssues();
				
				if (qtdIssuesAbertas > 0){
					issuesAtrasadas += qtdIssuesAbertas;
					totalIssues += qtdIssuesAbertas;
					possuiAtraso = true;
				}
				
				if (possuiAtraso)
					milestonesComAtraso++;
				
				i++;
				milestonesAvaliados++;
			}
			
			double porcMilestonesAtrasados = ((double) milestonesComAtraso/(double) milestonesAvaliados) * 100;
			double porcIssuesAtrasadas = ((double) issuesAtrasadas/(double) totalIssues) * 100;
			
			System.out.println("Total de milestones considerados: " + totalMilestones);
			System.out.println("Milestones avaliados: " + milestonesAvaliados);
			System.out.println("Milestones sem prazo: " + milestonesSemPrazo);
			System.out.println("Milestones abertos: " + milestonesAbertos);
			System.out.println("Milestones com atraso: " + milestonesComAtraso);
			System.out.println("-");
			System.out.println("Total de Issues analisadas: " + totalIssues);
			System.out.println("Issues atrasadas: " + issuesAtrasadas);
			System.out.println("-");
			System.out.println("Porcentagem de milestones atrasados: " + porcMilestonesAtrasados + "%");
			System.out.println("Porcentagem de issues atrasadas: " + porcIssuesAtrasadas + "%");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
