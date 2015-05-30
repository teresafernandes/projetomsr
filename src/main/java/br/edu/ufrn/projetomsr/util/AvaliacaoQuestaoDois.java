package br.edu.ufrn.projetomsr.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHLabel;
import org.kohsuke.github.GHMilestone;
import org.kohsuke.github.GHMilestoneState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import br.edu.ufrn.projetomsr.dominio.Milestone;

/**
 * Classe genérica utilizada para realizar a mineração de dados referente à questão 2,
 * para diferentes projetos.
 * 
 * A questão 2 é a seguinte: "Como os bugs afetam a produtividade da equipe com relação ao cumprimento dos prazos?"
 * 
 * @author Renan
 */
public class AvaliacaoQuestaoDois {
	
	public static void minerarQuestaoDois(String repositorio){
		
		/*
		 * Estratégia de avaliação:
		 * 
		 * 1) calcular a quantidade de bugs em cada sprint
		 * 2) verificar o tempo de resolução dos bugs de cada sprint, e compará-lo com o prazo total da sprint
		 * 3) verificar se os bugs atrasaram a sprint ou sprints com bugs tiveram atrasos
		 * 
		 */
		
		try {
			System.out.println("REPOSITÓRIO: " + repositorio);
			System.out.println("QUESTÃO 2: Como os bugs afetam a produtividade da equipe com relação ao cumprimento dos prazos?");
			System.out.println("Minerando dados... Aguarde...");
			
			GitHub github = GitHub.connect();
			GHRepository repo = github.getRepository(repositorio);
			
			int i = 1;
			
			//Algumas variáveis a serem calculadas
			
			List<GHIssue> issuesAtrasadas = new ArrayList<GHIssue>(); //Issues atrasadas de cada milestone
			List<GHIssue> bugIssues = new ArrayList<GHIssue>(); //Issues de bugs de cada milestone
			List<Milestone> milestones = new ArrayList<Milestone>(); //Armazenará informações sobre os milestones
			
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
				
				contadorTerminoLaco = 5;
				
				issuesAtrasadas = new ArrayList<GHIssue>();
				bugIssues = new ArrayList<GHIssue>();
				
				//Devem ser avaliados apenas os milestones já fechados
				if (ms.getState() == GHMilestoneState.OPEN){
					i++;
					continue;
				}
					
				Date dataPrazoMS = CalendarUtils.removeTime(ms.getDueOn());
				Date dataHoje = CalendarUtils.removeTime(new Date());
				
				if (dataPrazoMS == null || dataPrazoMS.after(dataHoje) || dataPrazoMS.equals(dataHoje)){
					continue; //Não há como avaliar milestones sem prazos ou cujos prazos ainda não se venceram
				}
				
				//Avaliando milestone
				
				//Avaliando issues fechadas do milestone
				List<GHIssue> issuesFechadas = repo.getIssues(GHIssueState.CLOSED, ms);
				
				if (issuesFechadas != null){
					for (GHIssue issue : issuesFechadas){
						
						Date dataFechamento = CalendarUtils.removeTime(issue.getClosedAt());
						
						if (dataFechamento.after(dataPrazoMS)){
							issuesAtrasadas.add(issue);
						}
						
						Iterator<GHLabel> it = issue.getLabels().iterator(); 
						
						while (it.hasNext()){
							if (it.next().getName().contains("bug"))
								bugIssues.add(issue);
								
						}
					}
				}
				
				//Caso existam issues abertas, então automaticamente já estão atrasadas, neste caso.
				int qtdIssuesAbertas = ms.getOpenIssues();
				
				if (qtdIssuesAbertas > 0){
					issuesAtrasadas.addAll(repo.getIssues(GHIssueState.OPEN, ms));
				}
				
				//Guardando milestone avaliado
				int totalIssuesMilestone = qtdIssuesAbertas + ms.getClosedIssues();
				
				Milestone m = new Milestone();
				m.setQtdTotalIssues(totalIssuesMilestone);
				m.setTitulo(ms.getTitle());
				m.setIssuesAtrasadas(issuesAtrasadas);
				m.setBugIssues(bugIssues);
				m.setDataPrazo(ms.getDueOn());
				
				milestones.add(m);
				
				i++;
			}
			
			for (Milestone m : milestones){
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static double round(double unrounded, int precision, int roundingMode){
	    BigDecimal bd = new BigDecimal(unrounded);
	    BigDecimal rounded = bd.setScale(precision, roundingMode);
	    return rounded.doubleValue();
	}

}
