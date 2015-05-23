package br.edu.ufrn.projetomsr.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHMilestone;
import org.kohsuke.github.GHMilestoneState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import br.edu.ufrn.projetomsr.dominio.Milestone;

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
			int qtdIssuesAtrasadas = 0;
			int totalIssues = 0;
			
			List<GHIssue> issuesAtrasadas = new ArrayList<GHIssue>();
			Map<GHMilestone, List<GHIssue>> milestonesAtrasados = new HashMap<GHMilestone, List<GHIssue>>();
			
			List<Milestone> milestones = new ArrayList<Milestone>();
			
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
				
				issuesAtrasadas = new ArrayList<GHIssue>();
				
				//Devem ser avaliados apenas os milestones já fechados
				if (ms.getState() == GHMilestoneState.OPEN){
					i++;
					milestonesAbertos++;
					continue;
				}
					
				Date dataPrazoMS = CalendarUtils.removeTime(ms.getDueOn());
				Date dataHoje = CalendarUtils.removeTime(new Date());
				
				if (dataPrazoMS == null || dataPrazoMS.after(dataHoje) || dataPrazoMS.equals(dataHoje)){
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
						
						Date dataFechamento = CalendarUtils.removeTime(issue.getClosedAt());
						
						if (dataFechamento.after(dataPrazoMS)){
							qtdIssuesAtrasadas++;
							issuesAtrasadas.add(issue);
							possuiAtraso = true;
						}
					}
				}
				
				//Caso existam issues abertas, então automaticamente já estão atrasadas, neste caso.
				int qtdIssuesAbertas = ms.getOpenIssues();
				
				if (qtdIssuesAbertas > 0){
					qtdIssuesAtrasadas += qtdIssuesAbertas;
					totalIssues += qtdIssuesAbertas;
					possuiAtraso = true;
					
					issuesAtrasadas.addAll(repo.getIssues(GHIssueState.OPEN, ms));
				}
				
				if (possuiAtraso){
					milestonesAtrasados.put(ms, issuesAtrasadas);
					milestonesComAtraso++;
				}
				
				i++;
				milestonesAvaliados++;
				
				//Guardando milestone avaliado
				double totalIssuesMilestone = qtdIssuesAbertas + ms.getClosedIssues();
				double qtdIssuesAtrasadasMilestone = issuesAtrasadas.size();
				double porcConclusaoMilestone = ((totalIssuesMilestone - qtdIssuesAtrasadasMilestone) / totalIssuesMilestone) * 100;
				
				Milestone m = new Milestone();
				m.setPorcConclusaoFechamento(porcConclusaoMilestone);
				m.setTitulo(ms.getTitle());
				
				milestones.add(m);
			}
			
			double porcMilestonesAtrasados = ((double) milestonesComAtraso/(double) milestonesAvaliados) * 100;
			double porcIssuesAtrasadas = ((double) qtdIssuesAtrasadas/(double) totalIssues) * 100;
			
			System.out.println();
			System.out.println("Total de milestones considerados: " + totalMilestones);
			System.out.println("Milestones avaliados: " + milestonesAvaliados);
			System.out.println("Milestones sem prazo: " + milestonesSemPrazo);
			System.out.println("Milestones abertos: " + milestonesAbertos);
			System.out.println("Milestones com atraso: " + milestonesComAtraso);
			System.out.println();
			System.out.println("Total de Issues analisadas: " + totalIssues);
			System.out.println("Issues atrasadas: " + qtdIssuesAtrasadas);
			System.out.println();
			System.out.println("Porcentagem de milestones atrasados: " + round(porcMilestonesAtrasados, 2, BigDecimal.ROUND_HALF_UP) + "%");
			System.out.println("Porcentagem de issues atrasadas: " + round(porcIssuesAtrasadas, 2, BigDecimal.ROUND_HALF_UP) + "%");
			System.out.println();
			System.out.println("-------------------------");
			System.out.println();
			
			//Exibindo as issues atrasadas de cada milestone
			
			if (!milestonesAtrasados.isEmpty()){
				for (GHMilestone m : milestonesAtrasados.keySet()){
					System.out.println("Issues atrasadas da sprint " + m.getTitle() 
							+ " (" + Formatador.getInstance().formatarData(m.getDueOn()) + ")");
					
					issuesAtrasadas = milestonesAtrasados.get(m);
					
					for (GHIssue issue : issuesAtrasadas){
						String textoData = issue.getClosedAt() != null ?
								Formatador.getInstance().formatarData(issue.getClosedAt()) : 
								"aberta";
								
						System.out.println(issue.getNumber() + " - " + issue.getTitle() +
								" (" + textoData + ")");
					}
					
					System.out.println();
				}
			}
			
			System.out.println();
			System.out.println("-------------------------");
			System.out.println();
			
			//Exibindo a porcentagem de conclusão de cada milestone no momento do encerramento de seus prazos
			
			Collections.sort(milestones, new Comparator<Milestone>() {

				public int compare(Milestone o1, Milestone o2) {
					if (o1.getPorcConclusaoFechamento() > o2.getPorcConclusaoFechamento())
						return 1;
					if (o1.getPorcConclusaoFechamento() < o2.getPorcConclusaoFechamento())
						return -1;
					return 0;
				}
				
			});
			
			System.out.println("Porcentagens de conclusão das sprints no encerramento de seus prazos");
			
			for (Milestone m : milestones){
				System.out.println(
						m.getTitulo() + ": " +
						round(m.getPorcConclusaoFechamento(), 2, BigDecimal.ROUND_HALF_UP) + "%"
						);
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
