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
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHMilestone;
import org.kohsuke.github.GHUser;

public class ExportarExcel {

	/**
	 * Método que exporta para excel os resultados obtidos na extração dos dados para a questão 3.
	 * @param issues
	 * @param colaboradoresRepositorio
	 * @param nomeRepositorio
	 * */
	public static void exportarQuestaoTres(Map<GHMilestone, Map<GHUser, List<GHIssue> >> issues, List<GHUser> colaboradoresRepositorio, String nomeRepositorio) throws IOException{

		//inicializar variaveis
		 int cellCount = 0, rowCount = 0, totalIssues = 0;
		 BigDecimal porcentagemIssues;
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
		     cell.setCellValue(u.getName());
	     }	     
		
	     //percorre as issues gravando a porcentagem de cada colaborador
	     for(GHMilestone m: issues.keySet()){
	    	 totalIssues = m.getOpenIssues()+ m.getClosedIssues();
	    	 cellCount = 0;
	    	 
	    	 row = sheet.createRow(rowCount++);
	    	 cell = row.createCell(cellCount++);
	    	 cell.setCellType(Cell.CELL_TYPE_STRING);
		     cell.setCellValue(m.getNumber());
		     
		     for(GHUser u : colaboradoresRepositorio){
		    	 cell = row.createCell(cellCount++);
		    	 cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			     
		    	 porcentagemIssues = BigDecimal.ZERO;
		    	 if(issues.get(m).get(u) != null){
				     porcentagemIssues = new BigDecimal(issues.get(m).get(u).size()).setScale(2,RoundingMode.HALF_EVEN)
								.divide(new BigDecimal(totalIssues),2, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100)).setScale(2,RoundingMode.HALF_EVEN);
		    	 }
			     cell.setCellValue(porcentagemIssues.doubleValue());
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
