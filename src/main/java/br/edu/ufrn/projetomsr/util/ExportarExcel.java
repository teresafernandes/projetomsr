package br.edu.ufrn.projetomsr.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.kohsuke.github.GHMilestone;
import org.kohsuke.github.GHUser;

import br.edu.ufrn.projetomsr.dominio.Milestone;
import br.edu.ufrn.projetomsr.dominio.QuantidadeIssues;

public class ExportarExcel {

	/**
	 * Método que exporta para excel os resultados obtidos na extração dos dados para a questão 3.
	 * @param milestones
	 * @param colaboradoresRepositorio
	 * @param nomeRepositorio
	 * */
	public static void exportarQuestaoTres(List<Milestone> milestones, List<GHUser> colaboradoresRepositorio, String nomeRepositorio) throws IOException{

		//inicializar variaveis
		 int cellCount = 0, rowCount = 0;
		 BigDecimal porcentagemIssuesRealizadas, issuesAtrasadas, issuesRealizadas, issuesCriadas;
		 Workbook wb  = new XSSFWorkbook();
		 Sheet sheet = wb.createSheet();
		 
	     Row row = sheet.createRow(rowCount++);
	     Cell cell = row.createCell(cellCount++);
	     cell.setCellType(Cell.CELL_TYPE_STRING);
	     cell.setCellValue("Milestone/Contribuidor");
	     
	     // grava a primeira linha da tabela com os nomes dos colaboradores
	     for(GHUser u : colaboradoresRepositorio){
	    	 cell = row.createCell(cellCount++);
		     cell.setCellType(Cell.CELL_TYPE_STRING);
		     cell.setCellValue(u.getName()+"(Ordinal)");
		     
		     cell = row.createCell(cellCount++);
		     cell.setCellType(Cell.CELL_TYPE_STRING);
		     cell.setCellValue(u.getName()+"(% Realizadas)");
		     
		     cell = row.createCell(cellCount++);
		     cell.setCellType(Cell.CELL_TYPE_STRING);
		     cell.setCellValue(u.getName()+"(Realizadas)");
		     
		     cell = row.createCell(cellCount++);
		     cell.setCellType(Cell.CELL_TYPE_STRING);
		     cell.setCellValue(u.getName()+"(Atrasadas)");
		     
		     cell = row.createCell(cellCount++);
		     cell.setCellType(Cell.CELL_TYPE_STRING);
		     cell.setCellValue(u.getName()+"(Criadas)");
	     }	
	     
	     cell = row.createCell(cellCount++);
	     cell.setCellType(Cell.CELL_TYPE_STRING);
	     cell.setCellValue("Desvio padrão da distribuição de issues");
	     
	     cell = row.createCell(cellCount++);
	     cell.setCellType(Cell.CELL_TYPE_STRING);
	     cell.setCellValue("Total de Issues Atrasadas");  
		
	     //percorre as issues gravando a porcentagem de cada colaborador
	     for(Milestone m: milestones){
	    	 cellCount = 0;
	    	 
	    	 row = sheet.createRow(rowCount++);
	    	 cell = row.createCell(cellCount++);
	    	 cell.setCellType(Cell.CELL_TYPE_STRING);
		     cell.setCellValue(m.getTitulo());
		     
		     for(GHUser u : colaboradoresRepositorio){
		    	 porcentagemIssuesRealizadas = BigDecimal.ZERO;
		    	 issuesAtrasadas  = BigDecimal.ZERO;
		    	 issuesCriadas  = BigDecimal.ZERO;
		    	 issuesRealizadas  = BigDecimal.ZERO;
		    	 
		    	 if(m.getIssuesPorContribuidor().get(u) != null){
				     porcentagemIssuesRealizadas = m.getIssuesPorContribuidor().get(u).getIssuesRealizadas().setScale(2,RoundingMode.HALF_EVEN)
								.divide(new BigDecimal(m.getQtdTotalIssues()),2, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100)).setScale(2,RoundingMode.HALF_EVEN);
				     issuesAtrasadas = m.getIssuesPorContribuidor().get(u).getIssuesAtrasadas();
				     issuesRealizadas = m.getIssuesPorContribuidor().get(u).getIssuesRealizadas();
				     issuesCriadas  = m.getIssuesPorContribuidor().get(u).getIssuesCriadas();
		    	 }

		    	 cell = row.createCell(cellCount++);
		    	 cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			     cell.setCellValue(colaboradoresRepositorio.indexOf(u)+1);
			    		 
		    	 cell = row.createCell(cellCount++);
		    	 cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			     cell.setCellValue(porcentagemIssuesRealizadas.doubleValue());
			     
			     cell = row.createCell(cellCount++);
		    	 cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				 cell.setCellValue(issuesRealizadas.doubleValue());
				 
				 cell = row.createCell(cellCount++);
		    	 cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				 cell.setCellValue(issuesAtrasadas.doubleValue());
				 
				 cell = row.createCell(cellCount++);
		    	 cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				 cell.setCellValue(issuesCriadas.doubleValue());
		     }
		     
			 cell = row.createCell(cellCount++);
	    	 cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		     cell.setCellValue(m.getDesvioPadraoIssues());
		     
		     cell = row.createCell(cellCount++);
	    	 cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		     cell.setCellValue(m.getQtdIssuesAtrasadas());
	     }
	     
	     // salva o arquivo excel
	     nomeRepositorio =  nomeRepositorio.replace('/','-');
	     FileOutputStream fileOut = new FileOutputStream("relatorioQuestao3"+nomeRepositorio+".xls");
	     wb.write(fileOut);
	     fileOut.close();
	     wb.close();
	}
	
	/**
	 * Método que exporta para excel os resultados obtidos na extração dos dados para a questão 3.
	 * @param issuesPorMilestone
	 * @param colaboradoresRepositorio
	 * @param nomeRepositorio
	 * */
	public static void exportarQuestaoDois(Map<GHMilestone, QuantidadeIssues> issuesPorMilestone, String nomeRepositorio) throws IOException{

		//inicializar variaveis
		 int cellCount = 0, rowCount = 0;
		 Workbook wb  = new XSSFWorkbook();
		 Sheet sheet = wb.createSheet();
		 
	     Row row = sheet.createRow(rowCount++);
	     Cell cell = row.createCell(cellCount++);
	     cell.setCellType(Cell.CELL_TYPE_STRING);
	     cell.setCellValue("Milestone");
	     
	     cell = row.createCell(cellCount++);
	     cell.setCellType(Cell.CELL_TYPE_STRING);
	     cell.setCellValue("Bug Issues");
	     
	     cell = row.createCell(cellCount++);
	     cell.setCellType(Cell.CELL_TYPE_STRING);
	     cell.setCellValue("Total de Issues Atrasadas");  
		
	     //percorre as issues gravando a porcentagem de cada colaborador
	     for(GHMilestone m: issuesPorMilestone.keySet()){
	    	 cellCount = 0;
	    	 
	    	 row = sheet.createRow(rowCount++);
	    	 cell = row.createCell(cellCount++);
	    	 cell.setCellType(Cell.CELL_TYPE_STRING);
		     cell.setCellValue(m.getNumber());
		     
			 cell = row.createCell(cellCount++);
	    	 cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		     cell.setCellValue(issuesPorMilestone.get(m).getIssuesBug().doubleValue());
		     
		     cell = row.createCell(cellCount++);
	    	 cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		     cell.setCellValue(issuesPorMilestone.get(m).getIssuesAtrasadas().doubleValue());
	     }
	     
	     // salva o arquivo excel
	     nomeRepositorio =  nomeRepositorio.replace('/','-');
	     FileOutputStream fileOut = new FileOutputStream("relatorioQuestao2"+nomeRepositorio+".xls");
	     wb.write(fileOut);
	     fileOut.close();
	     wb.close();
	}
}
