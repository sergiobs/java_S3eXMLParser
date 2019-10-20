import java.io.File;
import java.util.Scanner;

public class MainFuncionesAcciones {

	
    static boolean pregunta_trataXML(File ficheroSalida, Integer nivelTraza, Scanner entradaEscaner){

		String texto = "1. ¿Tratamos los ficheros XML (s/n)?: ";		
		escribeResultados.escribe(texto, ficheroSalida, nivelTraza);
		texto = entradaEscaner.nextLine ();	
		escribeResultados.escribe(texto+"\n", ficheroSalida, 1);
        if (texto.equalsIgnoreCase("s")) {
        	return true;
        } else return false;
    	
    }
    
    
    static boolean pregunta_calculaNombreObjetosRepetidosEnXML(File ficheroSalida, Integer nivelTraza, Scanner entradaEscaner){  
		String texto = "1.1. ¿Calculamos elementos con Nombre repetidos en XML (s/n)?: ";		
		escribeResultados.escribe(texto, ficheroSalida, nivelTraza);
		texto = entradaEscaner.nextLine ();		 	
		escribeResultados.escribe(texto+"\n", ficheroSalida, 1);
	    if (texto.equalsIgnoreCase("s")) {
	    	return true;
	    } else
	    	return false;
    }
    
    static boolean pregunta_calculaIdentificadorObjetos_noUsadosEnXML(File ficheroSalida, Integer nivelTraza, Scanner entradaEscaner){ 
		String texto = "1.2. ¿Calculamos elementos en XML no utilizados (s/n)?: ";		
		escribeResultados.escribe(texto, ficheroSalida, nivelTraza);
		texto = entradaEscaner.nextLine ();			
		escribeResultados.escribe(texto+"\n", ficheroSalida, 1);
	    if (texto.equalsIgnoreCase("s")) {
	    	return true;
	    } else 
	    	return false;
    }
    
    static boolean pregunta_borraObjetos_noUsadosEnXML(File ficheroSalida, Integer nivelTraza, Scanner entradaEscaner, Boolean calculaIdentificadorObjetos_noUsadosEnXML){
    	if (calculaIdentificadorObjetos_noUsadosEnXML) {
			String texto = "1.2.1 ¿Borramos elementos en XML no utilizados (s/n)?: ";		
			escribeResultados.escribe(texto, ficheroSalida, nivelTraza);
			texto = entradaEscaner.nextLine ();			
			escribeResultados.escribe(texto+"\n", ficheroSalida, 1);
		    if (texto.equalsIgnoreCase("s")) {
		    	return true;
		    } else 
		    	return false;
    	} else return false;
    }
    
    static boolean pregunta_calculaNombreObjetosRepetidosEnXML_usados_en_Scripts(File ficheroSalida, Integer nivelTraza, Scanner entradaEscaner, Boolean calculaNombreObjetosRepetidosEnXML){  
    	if (calculaNombreObjetosRepetidosEnXML) {
	    	String texto = "1.3. ¿Calculamos para los elementos con Nombre repetidos en XML, si son usados en scripts (s/n)?: ";		
			escribeResultados.escribe(texto, ficheroSalida, nivelTraza);
			texto = entradaEscaner.nextLine ();	
			escribeResultados.escribe(texto+"\n", ficheroSalida, 1);
	        if (texto.equalsIgnoreCase("s")) {        	
	        	return true;
	        } else 
	        	return false;
    	} else 
        	return false;
    }
    
    static boolean pregunta_EstimaMCS(File ficheroSalida, Integer nivelTraza, Scanner entradaEscaner){  
		String texto = "1.4. ¿Estimamos MCS (s/n)?: ";		
		escribeResultados.escribe(texto, ficheroSalida, nivelTraza);
		texto = entradaEscaner.nextLine ();	
		escribeResultados.escribe(texto+"\n", ficheroSalida, 1);
        if (texto.equalsIgnoreCase("s")) {  
        	return true;
        } else
        	return false;
    }  
    
    static boolean pregunta_calcula_MCS_en_scripts(File ficheroSalida, Integer nivelTraza, Scanner entradaEscaner){  
		String texto = "2. ¿Buscamos llamadas a MCS por XXX en ficheros script (s/n)?: ";		
		escribeResultados.escribe(texto, ficheroSalida, nivelTraza);
		texto = entradaEscaner.nextLine ();	
		escribeResultados.escribe(texto+"\n", ficheroSalida, 1);
        if (texto.equalsIgnoreCase("s")) {  
        	return true;
        } else
        	return false;
    }  
    

    
}
