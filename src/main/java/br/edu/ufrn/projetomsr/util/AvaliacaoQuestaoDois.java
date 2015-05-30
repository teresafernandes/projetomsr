package br.edu.ufrn.projetomsr.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHLabel;
import org.kohsuke.github.GHMilestone;
import org.kohsuke.github.GHMilestoneState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import br.edu.ufrn.projetomsr.dominio.Milestone;
import br.edu.ufrn.projetomsr.dominio.QuantidadeIssues;

/**
 * Classe genérica utilizada para realizar a mineração de dados referente à questão 2,
 * para diferentes projetos.
 * 
 * A questão 2 é a seguinte: "Como os bugs afetam a produtividade da equipe com relação ao cumprimento dos prazos?"
 * 
 * @author Renan
 */
public class AvaliacaoQuestaoDois {
	
	private static final double FATOR_QTD_BUGS = 0.3333;
	private static final double FATOR_TEMPO_RESOLUCAO_BUGS = 0.3333;
	
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
				m.setCriadoEm(ms.getCreatedAt());
				
				milestones.add(m);
				
				i++;
			}
			
			for (Milestone m : milestones){
				int qtdBugs = m.getBugIssues() != null ? m.getBugIssues().size() : 0;
				
				System.out.println();
				System.out.println("SPRINT " + m.getTitulo());
				System.out.println("Quantidade de issues: " + m.getQtdTotalIssues());
				System.out.println("Quantidade de bugs: " + qtdBugs);
				System.out.println("Quantidade de issues atrasadas: " + (m.getIssuesAtrasadas() != null ? m.getIssuesAtrasadas().size() : 0));
				
				/* 
				 * Se a quantidade de bugs da sprint foi maior ou igual a 1/3 do total de issues,
				 * então considera-se que houve muitos bugs 
				 */
				if (qtdBugs >= m.getQtdTotalIssues() * FATOR_QTD_BUGS){
					System.out.println("Quantidade de bugs avaliada negativamente");
					
					m.setNotaBugs(m.getNotaBugs() + 1);
					
					if (m.getIssuesAtrasadas() != null && m.getIssuesAtrasadas().size() != 0){
						//Nesse caso, a sprint teve muitos bugs e sofreu atraso
						//Considera-se novamente que a sprint foi prejudicada
						m.setNotaBugs(m.getNotaBugs() + 1);
						
						System.out.println("Sprint com muitos bugs e sofreu atraso");
					}
				}
				
				//Calculando tempo total de resolução dos bugs da sprint (em dias)
				
				int tempoSprint = CalendarUtils.calculoDias(m.getCriadoEm(), m.getDataPrazo());
				int tempoBugs = 0;
				Set<Date> diasTrabalhadosEmBugs = new HashSet<Date>();
				
				if (m.getBugIssues() != null){
					for (GHIssue issue : m.getBugIssues()){
						
						/* 
						 * Para evitar incluir, no cálculo de quantidade de dias de resolução de bugs,
						 * tarefas que demoraram a serem iniciadas, considero que o máximo de tempo
						 * que pode ser dedicado à resolução de um bug é de 5 dias.
						 * Na prática, realmente é difícil esse tempo ser superior a isso.
						 * Realizando uma análise qualitativa, verifiquei que felizmente poucas issues
						 * encontram-se nessa situação.
						 */
						int maxDias = 5;
						
						Date diasIssue = issue.getCreatedAt().before(m.getCriadoEm()) ? 
								CalendarUtils.removeTime(m.getCriadoEm()) :
								CalendarUtils.removeTime(issue.getCreatedAt());
						Date dataFechamentoIssue = CalendarUtils.removeTime(issue.getClosedAt());
						
						while (!diasIssue.after(dataFechamentoIssue) && maxDias != 0){ 
							diasTrabalhadosEmBugs.add(diasIssue);
							diasIssue = CalendarUtils.adicionaDias(diasIssue, 1);
							maxDias--;
						}
					}
				}
				
				tempoBugs = diasTrabalhadosEmBugs.size();
				
				System.out.println("Tempo total da sprint: " + tempoSprint + " dias");
				System.out.println("Tempo total de resolução de bugs: " + tempoBugs + " dias");
				
				/* 
				 * Se o tempo total de resolução de bugs foi maior ou igual a 1/3 do prazo total da sprint,
				 * então considera-se que a sprint foi prejudicada pelos bugs
				 */
				if (tempoBugs >= tempoSprint * FATOR_TEMPO_RESOLUCAO_BUGS){
					m.setNotaBugs(m.getNotaBugs() + 1);
					
					System.out.println("Tempo de resolução de bugs avaliado negativamente");
				}
				
				System.out.println("Nota: " + m.getNotaBugs());
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

	
	
	/**
	 * Analisar: quantidade de bugs na sprint, tempo de resolução do bug, prazo da sprint
	 * */
	public static void minerarQuestaoDois_versao2(String repositorio) {
		try {
			System.out.println("REPOSITÓRIO: " + repositorio);
			System.out.println("QUESTÃO 2: Como os bugs afetam a produtividade da equipe com relação ao cumprimento dos prazos?");
			System.out.println("Minerando dados... Aguarde...");
			
			GitHub github = GitHub.connect();
			GHRepository repo = github.getRepository(repositorio);
			int i = 1;
			int contadorTerminoLaco = 5;
			
			//Map para armazenar as issues por milestones
			Map<GHMilestone, QuantidadeIssues> issuesPorMilestone = new LinkedHashMap<GHMilestone, QuantidadeIssues>();
			
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
				
				// armazena todas as issues (aberta e fechadas)
				List<GHIssue> issues = new ArrayList<GHIssue>();
				issues.addAll(repo.getIssues(GHIssueState.OPEN, ms));
				issues.addAll(repo.getIssues(GHIssueState.CLOSED, ms));
				
				//inicializa map
				if(issuesPorMilestone.get(ms) == null)	
					issuesPorMilestone.put(ms, new QuantidadeIssues());
				
				//percorre as issues, identificando se é um bug e se está atrasado
				for(GHIssue is : issues){
					if(IssuesUtil.isIssueBug(is)){
						issuesPorMilestone.get(ms).incrementarIssuesBug();
						if(IssuesUtil.isIssueAtrasada(is))
							issuesPorMilestone.get(ms).incrementarIssuesBugAtrasadas();
					}
					if(IssuesUtil.isIssueAtrasada(is))
						issuesPorMilestone.get(ms).incrementarIssuesAtrasadas();
				}
				
				i++;
			}
			
			// imprime no console as issues processadas
			for(GHMilestone m : issuesPorMilestone.keySet()){
				System.out.println("\nMilestone: "+ m.getTitle() + " ("+m.getState().toString()+")");
				System.out.println(m.getClosedIssues()+m.getOpenIssues() + " issues, "+ issuesPorMilestone.get(m).getIssuesAtrasadas() + " issues atrasadas");
				System.out.println( issuesPorMilestone.get(m).getIssuesBug() + " bugs, "+ issuesPorMilestone.get(m).getIssuesBugAtrasadas() + " bugs atrasadas");
				System.out.println();
			}
			
						
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
