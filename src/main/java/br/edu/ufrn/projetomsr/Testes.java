package br.edu.ufrn.projetomsr;

import java.io.IOException;

import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class Testes {

	public static void main(String[] args) {
		//getDadosUsuario();
		getExemploGitHubJavaAPI();
	}

	/** 
	 * Exemplo de uma requisição GET usando uma biblioteca REST convencional. 
	 * */
	public static void getDadosUsuario() {
		try {

			Client client = Client.create();

			WebResource webResource = client
					.resource("https://api.github.com/users/fmarquesfilho");

			ClientResponse response = webResource.accept("application/json")
					.get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			String output = response.getEntity(String.class);

			System.out.println("Output from Server .... \n");
			System.out.println(output);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Exemplo de uma requisição usando uma bilioteca do GitHub API para Java. <br/>
	 * Mais detalhes em: http://github-api.kohsuke.org/
	 * @throws IOException 
	 */
	public static void getExemploGitHubJavaAPI() {
		
		try {
			GitHub github = GitHub.connect();
			
//			GHRepository repo = github.createRepository(
//			  "new-repository","this is my new repository",
//			  "http://www.kohsuke.org/",true/*public*/);
//			
//			repo.addCollaborators(github.getUser("abayer"),github.getUser("rtyler"));
//			repo.delete();
			
//			GHUser usuario = github.getUser("fmarquesfilho");
//			
//			System.out.println(usuario.getLogin());
//			System.out.println(usuario.getEmail());
		
			GHRepository repo = github.getRepository("IMD-UFRN/OpenRes");
			repo.getBranches();
			//repo.getCollaborators();
			repo.getIssues(GHIssueState.OPEN);
			repo.getMilestone(1);
			repo.getMilestone(2);
			repo.getMilestone(3);
			repo.getMilestone(4);
			repo.getMilestone(19);
			repo.getMilestone(20);
			repo.getMilestone(21);
			repo.getMilestone(22);
			repo.getMilestones();
			repo.getOpenIssueCount();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
