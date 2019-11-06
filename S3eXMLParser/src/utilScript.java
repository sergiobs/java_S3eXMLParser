import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class utilScript {
	static Integer buscaStringScript_delimitado (String stringBuscar, File fileScript) {
		int apariciones = 0;
		try {			
			Scanner sc = new Scanner(fileScript);
			String linea = "";
			while (sc.hasNext()) {
				linea = sc.nextLine();
				if (
						linea.contains(" "+stringBuscar+" ")||
						linea.contains(","+stringBuscar+",")||
						linea.contains(","+stringBuscar+")")||
						linea.contains("("+stringBuscar+",")||
						
						linea.contains("\""+stringBuscar+"\"")||
						
						linea.contains(" "+stringBuscar+",")||
						linea.contains(","+stringBuscar+" ")||
						linea.contains("("+stringBuscar+" ")||
						linea.contains(" "+stringBuscar+")")

						) {
					apariciones++;						
				}
			}			
			sc.close();							
		} catch (IOException e) {
			e.printStackTrace();
			//return  imprimeError+" " + e;
		}
		return apariciones;
	}
	static Integer buscaStringScript (String stringBuscar, File fileScript) {
		int apariciones = 0;
		try {			
			Scanner sc = new Scanner(fileScript);
			String linea = "";
			while (sc.hasNext()) {
				linea = sc.nextLine();
				if (linea.contains(stringBuscar)) {
					apariciones++;						
				}
			}			
			sc.close();							
		} catch (IOException e) {
			e.printStackTrace();
			//return  imprimeError+" " + e;
		}
		return apariciones;
	}
	
	static Integer busca1vezStringScript (String stringBuscar, File fileScript) {
		int apariciones = 0;
		try {			
			Scanner sc = new Scanner(fileScript);
			String linea = "";
			while (sc.hasNext()) {
				linea = sc.nextLine();
				if (linea.contains(stringBuscar)) {
					apariciones++;	
					break;
				}
			}			
			sc.close();							
		} catch (IOException e) {
			e.printStackTrace();
			//return  imprimeError+" " + e;
		}
		return apariciones;
	}
}
