package br.edu.ufrn.projetomsr.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHMilestone;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

/**
 * Classe genérica utilizada para realizar a mineração de dados referente à questão 3,
 * para diferentes projetos.
 * 
 * A questão 3 é a seguinte: "Como identificar se a distribuição de tarefas entre os membros está dificultando o cumprimento das metas da sprint?"
 * 
 * @author Teresa
 */
public class AvaliacaoQuestaoTres {
	
	/** TODO 
	 * consultar milestones
	 * ver issues de milestones fechados: criador, data de criação, quem fechou, data fechado
	 * ver issues de milestones aberto: criador, data de criação, usuario do ultimo evento (desenvolvedor atual)
	 * verificar complexidade (se existir) de cada issue
	 * verificar tipo de issue (label)
	 * numero de issues fechadas e proporção de contribuidores
	 * https://api.github.com/orgs/4soft/repos?access_token=9820d78f80626b3a6637cb5fe5514059278c33f6
	 * */
	public static void minerarQuestaoTres(String repositorio){
		
		try {
			System.out.println("REPOSITÓRIO: " + repositorio);
			System.out.println("QUESTÃO 3: Como identificar se a distribuição de tarefas entre os membros está dificultando o cumprimento das metas da sprint?");
			System.out.println("Minerando dados... Aguarde...");
			
			GitHub github = GitHub.connect();
			GHRepository repo = github.getRepository(repositorio);
			
			int i = 1;
			
			//Map para armazenar as issues de cada contribuidor por milestones
			Map<GHMilestone, Map<GHUser, List<GHIssue> >> issuesPorMilestonePorContribuidor = new LinkedHashMap<GHMilestone, Map<GHUser,List<GHIssue>>>();
			int contadorTerminoLaco = 5;
			GHUser desenvolvedor;
			BigDecimal porcentagemIssues;
			
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
				if(issuesPorMilestonePorContribuidor.get(ms) == null)	
					issuesPorMilestonePorContribuidor.put(ms, new LinkedHashMap<GHUser, List<GHIssue>>());
				
				// percorre as issues identificando qual o desenvolvedor
				for(GHIssue is : issues){
					if(is.getState().equals(GHIssueState.OPEN)){
						// se a issue estiver aberta, o desenvolvedor será quem a criou
						// o ideal é pegar o autor do ultimo evento da issue, mas ainda não consegui pegar os eventos de um issue (TODO)
						desenvolvedor = is.getUser();
					}else{
						// se a issue estiver fechada, o desenvolvedor será quem a fechou
						//desenvolvedor = is.getClosedBy();
						desenvolvedor = is.getUser();
					}
					
					if(issuesPorMilestonePorContribuidor.get(ms).get(desenvolvedor) == null)
						issuesPorMilestonePorContribuidor.get(ms).put(desenvolvedor, new ArrayList<GHIssue>());
					// adiciona a issue no map respectivo ao milestone e desenvolvedor atuais	
					issuesPorMilestonePorContribuidor.get(ms).get(desenvolvedor).add(is);					
				}
				
				i++;
			}
			
			//TODO armazenar as issues de cada desenvolvedor e em seguida analisar a data de criação e fechamento
			for(GHMilestone m : issuesPorMilestonePorContribuidor.keySet()){
				System.out.println("\nMilestone "+ m.getNumber());
				for(GHUser u : issuesPorMilestonePorContribuidor.get(m).keySet()){
					porcentagemIssues = new BigDecimal(issuesPorMilestonePorContribuidor.get(m).get(u).size()).setScale(2,RoundingMode.HALF_EVEN)
											.divide(new BigDecimal(m.getOpenIssues()+ m.getClosedIssues()),2, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100)).setScale(2,RoundingMode.HALF_EVEN);
											
					System.out.println(u.getName()+" :"+porcentagemIssues+"% das issues");
				}
				System.out.println();
			}
						
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
