package br.edu.ufrn.projetomsr.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe que contém métodos para formatação de diversos tipos de dados.
 * @author Renan
 *
 */
public class Formatador {
	
	private static Formatador singleton;
	
	/**
	 * Mantem formatos para datas e horas.
	 */
	private SimpleDateFormat df, dfH, horaF;
	
	/**
	 * Contrutor Padr�o
	 */
	private Formatador() {
		df = new SimpleDateFormat("dd/MM/yyyy");
		df.setLenient(false);
		dfH = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		horaF = new SimpleDateFormat("HH:mm");
	}
	
	/**
	 * Retorna uma inst�ncia do Formatador. Singleton.
	 *
	 * @return
	 */
	public static Formatador getInstance() {
		if (singleton == null) {
			singleton = new Formatador();
		}
		return singleton;
	}
	
	/**
	 * Formata data e hora. M�scara: dd/MM/yyyy HH:mm
	 *
	 * @param data
	 * @return
	 */
	public String formatarDataHora(Date data) {
		return dfH.format(data);
	}
	
	/**
	 * Formatar hora com HH:mm
	 * @param data
	 * @return
	 */
	public String formatarHora(Date data) {
		try {
			return horaF.format(data);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Formata data no formato dd/MM/yyyy
	 */
	public String formatarData(Date data) {
		return (data == null ? "" : df.format(data));
	}
	
	/**
	 * Formata um CPF, inserindo pontos e tra�os.
	 *
	 * @param cpf
	 * @return
	 */
	public String formatarCPF(String cpf) {

		try {

			StringBuffer id = new StringBuffer(String.valueOf(cpf));

			if (id.length() < 11) {

				for (int a = id.length(); a < 11; a++) {
					id.insert(0, "0");
				}
			}

			int size = id.length();

			id.insert(size - 2, "-");
			id.insert(size - 5, ".");
			id.insert(size - 8, ".");

			return id.toString();

		} catch (Exception e) {
			return String.valueOf(cpf);
		}

	}
	
	/**
	 * Este m�todo retira pontos, tra�o e barras de um CPF ou CNPJ e o
	 * transforma em uma String
	 *
	 * @param req
	 */
	public String parseStringCPF(String cpfString) {

		if (cpfString == null)
			return "";

		int tamString = cpfString.length();
		String cpfLimpo = "";

		for (int i = 0; i < tamString; i++) {

			if ((cpfString.charAt(i) != '-') && (cpfString.charAt(i) != '.')
					&& (cpfString.charAt(i) != '/')
					&& (cpfString.charAt(i) != ' ')) {

				cpfLimpo += cpfString.charAt(i);
			}
		}

		return cpfLimpo;
	}
	
}
