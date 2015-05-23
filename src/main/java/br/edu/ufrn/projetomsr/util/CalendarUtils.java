package br.edu.ufrn.projetomsr.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Classe que re�ne m�todos �teis referentes a datas em geral. 
 * @author Renan
 */
public class CalendarUtils {
	
	/**
	 * Retorna o ano corrente.
	 * @return
	 */
	public static int getAnoAtual() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.YEAR);
	}
	
	/**
	 * Retorna o m�s corrente.
	 * @return
	 */
	public static int getMesAtual() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.MONTH) + 1;
	}
	
	/**
	 * Retorna o dia corrente.
	 * @return
	 */
	public static int getDiaAtual() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * Incrementa uma data em uma quantidade de dias passada como par�metro
	 * 
	 * @param data
	 * @return
	 */
	public static Date adicionaDias(Date data, int dias) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.add(Calendar.DAY_OF_MONTH, dias);
		return c.getTime();
	}
	
	public static Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

}
