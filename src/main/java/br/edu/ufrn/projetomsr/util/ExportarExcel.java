package br.edu.ufrn.projetomsr.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.kohsuke.github.GHMilestone;
import org.kohsuke.github.GHUser;

import br.edu.ufrn.projetomsr.dominio.Milestone;

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
	     }
	     
	     // salva o arquivo excel
	     nomeRepositorio =  nomeRepositorio.replace('/','-');
	     FileOutputStream fileOut = new FileOutputStream("relatorioQuestao3"+nomeRepositorio+".xls");
	     wb.write(fileOut);
	     fileOut.close();
	     wb.close();
	}
}
