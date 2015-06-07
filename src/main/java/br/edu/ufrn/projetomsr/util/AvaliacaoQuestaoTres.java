package br.edu.ufrn.projetomsr.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHMilestone;
import org.kohsuke.github.GHMilestoneState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

import br.edu.ufrn.projetomsr.dominio.Milestone;
import br.edu.ufrn.projetomsr.dominio.QuantidadeIssues;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * Classe genérica utilizada para realizar a mineração de dados referente à questão 3,
 * para diferentes projetos.
 * 
 * A questão 3 é a seguinte: "Como identificar se a distribuição de tarefas entre os membros está dificultando o cumprimento das metas da sprint?"
 * Há algum membro com um número sobrecarregado de issues, em comparação com os demais?
 * As issues desses membros "mais sobrecarregados" são as que estão atrasando?
 * É possível definir se esses membros tem algum papel específico dentro da equipe? de gerência talvez? 
 * 
 * @author Teresa
 */
public class AvaliacaoQuestaoTres {
	
	/**
	 * consultar milestones
	 * ver issues de milestones fechados: criador, data de criação, quem fechou, data fechado
	 * ver issues de milestones aberto: criador, data de criação, usuario do ultimo evento (contribuidor atual)
	 * verificar complexidade (se existir) de cada issue
	 * verificar tipo de issue (label)
	 * numero de issues fechadas e proporção de contribuidores
	 * https://api.github.com/orgs/4soft/repos?access_token=312d8940674b6b83a5812014be2777941f6485c1
	 * @throws ParseException 
	 * */
	public static void minerarQuestaoTres(String repositorio) {
		
		try {
			System.out.println("REPOSITÓRIO: " + repositorio);
			System.out.println("QUESTÃO 3: Como identificar se a distribuição de tarefas entre os membros está dificultando o cumprimento das metas da sprint?");
			System.out.println("Minerando dados... Aguarde...");
			
			GitHub github = GitHub.connect();
			GHRepository repo = github.getRepository(repositorio);
			int i = 1;
			
			//Map para armazenar as issues de cada contribuidor por milestones
			List<Milestone> listaMilestones = new ArrayList<Milestone>();
			List<GHUser> contribuidoresRepositorio = new ArrayList<GHUser>();
			Milestone milestone;
			int contadorTerminoLaco = 5;
			GHUser contribuidor = null, criador = null;
			BigDecimal porcentagemIssues, porcentagemIssuesAtrasadas = BigDecimal.ZERO;
			
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
				
				//Devem ser avaliados apenas os milestones já fechados
				if (ms.getState() == GHMilestoneState.OPEN){
					i++;
					continue;
				}
				
				// armazena todas as issues (aberta e fechadas)
				List<GHIssue> issues = new ArrayList<GHIssue>();
				issues.addAll(repo.getIssues(GHIssueState.OPEN, ms));
				issues.addAll(repo.getIssues(GHIssueState.CLOSED, ms));
			
				milestone = new Milestone();
				
				//percorre as issues, identificando o contribuidor responsavel por cada uma
				for(GHIssue is : issues){
					if(is.getState().equals(GHIssueState.OPEN)){
						// se a issue estiver aberta, o contribuidor será aquele que foi associado
						contribuidor = is.getAssignee();
						if(contribuidor==null)
							// caso não tenha sido associado um contribuidor, o responsável é o usuário que criou a issue
							contribuidor = is.getUser();		
						
					}else{
						// se a issue estiver fechada, o contribuidor será aquele que foi associado
						contribuidor = is.getAssignee();
						if(contribuidor==null)
							// caso não tenha sido associado um contribuidor, o responsável será quem a fechou
							contribuidor = is.getRepository().getIssue(is.getNumber()).getClosedBy();
					}
					
					if(milestone.getIssuesPorContribuidor().get(contribuidor) == null)
						milestone.getIssuesPorContribuidor().put(contribuidor, new QuantidadeIssues());
					// adiciona a issue no map respectivo ao milestone e contribuidores atuais	
					milestone.getIssuesPorContribuidor().get(contribuidor).incrementarIssuesRealizadas();
					if(IssuesUtil.isIssueAtrasada(is))
						milestone.getIssuesPorContribuidor().get(contribuidor).incrementarIssuesAtrasadas();

					//verifica qual o criador da issue para contabilizar a quantidade de tarefas que cada um abre em cada sprint
					criador = is.getUser();
					if(milestone.getIssuesPorContribuidor().get(criador) == null)
						milestone.getIssuesPorContribuidor().put(criador, new QuantidadeIssues());
					milestone.getIssuesPorContribuidor().get(criador).incrementarIssuesCriadas();
					
					// adiciona o contribuidor na lista de contribuidores do repositório
					if(!contribuidoresRepositorio.contains(contribuidor))
						contribuidoresRepositorio.add(contribuidor);
				}
				
				milestone.setTitulo(ms.getNumber()+"");
				milestone.setDataPrazo(ms.getDueOn());
				milestone.setQtdTotalIssues(ms.getOpenIssues()+ ms.getClosedIssues());
				milestone.setCriadoEm(ms.getCreatedAt());
				
				listaMilestones.add(milestone);
				
				i++;
			}
			
			//Armazena valores a serem usados no calculo da correlação de spearman
			double[] desvioMilestones = new double[listaMilestones.size()];
			double[] atrasoMilestones = new double[listaMilestones.size()];
			i = 0;
			// imprime no console as issues processadas
			for(Milestone m : listaMilestones){
				System.out.println("\nMilestone: "+ m.getTitulo() + " ("+Formatador.getInstance().formatarData(m.getCriadoEm())+")");
				for(GHUser u : m.getIssuesPorContribuidor().keySet()){
					
					porcentagemIssues = m.getIssuesPorContribuidor().get(u).getIssuesRealizadas().setScale(2,RoundingMode.HALF_EVEN)
											.divide(new BigDecimal(m.getQtdTotalIssues()),2, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100)).setScale(2,RoundingMode.HALF_EVEN);
					
					if(!m.getIssuesPorContribuidor().get(u).getIssuesRealizadas().equals(BigDecimal.ZERO))
						porcentagemIssuesAtrasadas  = m.getIssuesPorContribuidor().get(u).getIssuesAtrasadas().setScale(2,RoundingMode.HALF_EVEN)
														.divide(m.getIssuesPorContribuidor().get(u).getIssuesRealizadas(),2, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100)).setScale(2,RoundingMode.HALF_EVEN);
						
					System.out.println(u.getLogin()+": "+porcentagemIssues+"% das issues ("+m.getIssuesPorContribuidor().get(u).getIssuesRealizadas()+"), "+porcentagemIssuesAtrasadas+"% de issues atrasadas ("+m.getIssuesPorContribuidor().get(u).getIssuesAtrasadas()+")");
				}
				//calcula o desvio padrão da distribuição de issues entre contribuidores
				Double desvio = m.getDesvioPadraoIssues();
				desvioMilestones[i] = desvio;
				atrasoMilestones[i++] = m.getQtdIssuesAtrasadas();
				System.out.println("Desvio padrão: "+desvio);
				System.out.println();
			}
			
			SpearmansCorrelation s = new SpearmansCorrelation();
			System.out.println("Correlação (desvio da distribuição de issues VS issues com atraso no milestone): "+ s.correlation(desvioMilestones, atrasoMilestones));
			
			//exporta os resultados num arquivo excel
			ExportarExcel.exportarQuestaoTres(listaMilestones, contribuidoresRepositorio, repositorio);
						
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private static String getAutorUltimoEventoIssue(GHIssue i, String repositorio) throws ParseException{
		Client client = Client.create();
		WebResource webResource = client
				.resource("https://api.github.com/repos/"+repositorio+"/issues/"+i.getNumber()+"/events");
		ClientResponse response = webResource.accept("application/json")
				.get(ClientResponse.class);
		if (response.getStatus() != 200) 
			return null;		

		String output = response.getEntity(String.class);
		JSONArray obj = (JSONArray) JSONValue.parse(output);
		if(obj != null && obj.size() >0){
			//DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			//return format.parse(((String) ((JSONObject) obj.get(obj.size()-1)).get("created_at")).substring(0, 10)); 
			return (String) ((JSONObject) ((JSONObject) obj.get(obj.size()-1)).get("actor")).get("login");
		}
		return null;
	}
	
	@SuppressWarnings("unused")
	private static List<GHUser> getTodosContribuidores(String repositorio, GitHub github) throws IOException{
		Client client = Client.create();
		WebResource webResource = client
				.resource("https://api.github.com/repos/"+repositorio+"/contributors");
		ClientResponse response = webResource.accept("application/json")
				.get(ClientResponse.class);
		if (response.getStatus() != 200) 
			return null;		

		String output = response.getEntity(String.class);
		JSONArray obj = (JSONArray) JSONValue.parse(output);
		if(obj.isEmpty())
			return null;
		
		List<GHUser> contribuidores = new ArrayList<GHUser>();
		for(Object o : obj)
			contribuidores.add(github.getUser((String) ((JSONObject) o).get("login")));
		return contribuidores;
	}

}
