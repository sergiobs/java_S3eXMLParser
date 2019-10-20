import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainFunciones {

	
    static void cuentaMCS_enScripts(String rutaBase, int nivelTraza, File ficheroSalida, File ruta){
    	List<File> listaFicherosSCRIPT = new ArrayList<File>();
 		String texto = "Buscando scripts en " + rutaBase;
		escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);
		listaFicherosSCRIPT = Archivos.listarArchivosScript(ruta);	
		
		int apariciones = 0;
		int apariciones_total =0;
		String string_a_buscar = ("MCS");
		for (int in_Fichero_script=0;in_Fichero_script<listaFicherosSCRIPT.size();in_Fichero_script++){ 
			File ficheroScript = listaFicherosSCRIPT.get(in_Fichero_script);
	    	apariciones = utilScript.buscaStringScript(string_a_buscar, ficheroScript);
    		if (apariciones > 0) {
    			texto = "ERROR. Se usa " + apariciones + " veces el elemento (" + string_a_buscar + ") en script: " + ficheroScript;
    			escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);
    			apariciones_total +=apariciones;
    		}
		}	
    	if (apariciones>0){
    		texto = "Apariciones de " + string_a_buscar + ": " + apariciones_total;
    		escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);	
    	}    	
    }
    
    static void borra_noUsados_enPruebas(String rutaBase, int nivelTraza, File ruta){
// 		File fichero_original = new File("c:\\temp\\CC100ESP.xml"); 		
// 		File fichero_modificado = new File("c:\\temp\\CC100ESP_modificado.xml");  		
//
//		if (fichero_modificado.exists()) {
//			fichero_modificado.delete();
//		}
// 		
//		String string_a_buscar = ("<MF Identificador=\"7000\"");
//
//		
//		try {			
//			Scanner sc = new Scanner(fichero_original);
//        	BufferedWriter bw = null;
//            FileWriter fw = null;
//            
//            if (!fichero_modificado.exists()) {
//            	fichero_modificado.createNewFile();
//            }
//            fw = new FileWriter(fichero_modificado.getAbsoluteFile(), true);
//            bw = new BufferedWriter(fw);
//            
//			
//			String linea = "";
//			//String nuevo_texto_total =""; //contendra el nuevo fichero xml
//			String linea_nueva = "";
//			while (sc.hasNext()) {
//				linea = sc.nextLine();				
//				if (linea.contains(string_a_buscar)) 					
//					linea_nueva = "<!--" + linea + "-->";			
//				else 
//					linea_nueva = linea;				
//				bw.write(linea_nueva+"\r\n");
//			}		
//
//            if (bw != null)
//                bw.close();
//            if (fw != null)
//                fw.close();
//			
//			sc.close();							
//		} catch (IOException e) {
//			e.printStackTrace();
//			//return  imprimeError+" " + e;
//		}
//		
//		
	   	
    }
 
    static List<File>  calculaListaFicherosXML_validos(String rutaBase, int nivelTraza, File ficheroSalida, File ruta){
		List<File> listaFicherosXML = new ArrayList<File>();
		List<File> listaFicherosXML_validos = new ArrayList<File>();		
		String texto = "Buscando ficheros en " + rutaBase;
		escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);
		listaFicherosXML = Archivos.listarArchivosXMLRecursivamente(ruta);
        		
		// se recorre todos los archivos xml y se queda solo con los validos en la lista listaFicherosXML_validos	
		int nFicheroValidado = 0;
		for (int i = 0; i < listaFicherosXML.size(); i++) {
		    File ficheroXMLaValidar;
		    ficheroXMLaValidar = listaFicherosXML.get(i);
		    boolean esS3e = Archivos.ficheroXMLValido(ficheroXMLaValidar);		   		
			if (esS3e) {
				nFicheroValidado++;
				listaFicherosXML_validos.add(ficheroXMLaValidar);
				texto=nFicheroValidado+ "\t "+listaFicherosXML_validos.get(nFicheroValidado-1);				
				escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);
			}
		}
		return listaFicherosXML_validos;
    }
    
    
    static void tratar_cada_XML(int nivelTraza, File ficheroSalida, File ficheroSalida_numMCS, File ruta, List<File> listaFicherosXML_validos, Boolean calculaNombreObjetosRepetidosEnXML, Boolean calculaIdentificadorObjetos_noUsadosEnXML, Boolean borraObjetos_noUsadosEnXML, Boolean calculaNombreObjetosRepetidosEnXML_usados_en_Scripts, Boolean estimaMCS){

		String texto = "Entramos en tratarXML";
		escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);

				
		int elementosRepesUsadosTotal=0;				
		// bucle para procesar cada objeto contenido en el array de listaDatosFicheroXML			
		for (int indiceFichero=0;indiceFichero<listaFicherosXML_validos.size();indiceFichero++){
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			
			DatosFicheroXML datos_ficheroXML_bajo_test = new DatosFicheroXML(listaFicherosXML_validos.get(indiceFichero)); 
   	    
			texto="\n"+timestamp+" - " + indiceFichero+ " "+datos_ficheroXML_bajo_test.getFichero()+"\n---------------------------------------------------------------------------------------------";			
			escribeResultados.escribe(texto+"", ficheroSalida, nivelTraza);
			texto=indiceFichero+ "\t"+datos_ficheroXML_bajo_test.getFichero();
			escribeResultados.escribe(texto+"", ficheroSalida_numMCS, 1);
			
			if (datos_ficheroXML_bajo_test.getFicheroSicamPC() == null) {
				texto="ERROR: No existe sicampc.xml";				
				escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);
			}
				    	    	
	    	// llamamos a la funcion para que cuente los objetos del S3e del fichero
			datos_ficheroXML_bajo_test.cuentaObjetosS3e();		

			
			if (calculaNombreObjetosRepetidosEnXML) {
				datos_ficheroXML_bajo_test.calculaNombreObjetosRepetidosS3e();
				
				// imprimimos solo los elementos repetidos por tipo
				texto="\n*** Objetos repetidos:";				
				escribeResultados.escribe(texto+"\t", ficheroSalida, nivelTraza);					
				texto="" + datos_ficheroXML_bajo_test.imprimeListaNombreObjetosRepetidosEnXML();				
				escribeResultados.escribe(texto+"\t", ficheroSalida, nivelTraza);
			}
				
			if (calculaIdentificadorObjetos_noUsadosEnXML) {
				texto="\n*** Elementos no usados:";				
				escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);				
				datos_ficheroXML_bajo_test.calcula_Id_Objetos_UsadosS3e();					
				texto=datos_ficheroXML_bajo_test.imprime_Objetos_NoUsadosS3e();				
				escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);				
			}

			if (borraObjetos_noUsadosEnXML) {
				texto="\n*** Borra Elementos no usados:";				
				escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);
				// parto de una lista de elementos no usados en el xml (ignoro los MCS de momento).				
				datos_ficheroXML_bajo_test.borra_Objetos_NoUsadosS3e();	
				
			
			}
			
			// sacamos lista de numero de cada objeto del S3e. (60 MCS, 100 ED, etc)
			texto="\n*** Numero de objetos:\n" + datos_ficheroXML_bajo_test.imprimelistaNumeroObjetosS3e();			
			escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);	

			// Estimamos MCS
			if (estimaMCS) {
				if (datos_ficheroXML_bajo_test.estimaMCS()==0) {
					texto="*** Estimacion MCS:\n" + datos_ficheroXML_bajo_test.imprimeMCS_Estimados();
					escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);
					texto=datos_ficheroXML_bajo_test.imprimeMCS_Estimados_corto();
					escribeResultados.escribe(texto+"", ficheroSalida_numMCS, 1);
					
					// si ademas hemos buscado los no usados, recalculamos la estimación
					if (calculaIdentificadorObjetos_noUsadosEnXML) {
						datos_ficheroXML_bajo_test.estimaMCS_ahorro();
						texto="*** Estimacion MCS, tras eliminar los repetidos:\n" + datos_ficheroXML_bajo_test.imprimeMCS_EstimadosF();
						escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);		
						texto=datos_ficheroXML_bajo_test.imprimeMCS_EstimadosF_corto();
						escribeResultados.escribe(texto+"\n", ficheroSalida_numMCS, 1);
					}
					
					
				} else if(datos_ficheroXML_bajo_test.estimaMCS()==-1) {
					texto="*** ERROR en estimaMCS";
					escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);
				}
			}
			
			if (calculaNombreObjetosRepetidosEnXML_usados_en_Scripts&&calculaNombreObjetosRepetidosEnXML) {
				// BUSQUEDA DE ELEMENTOS REPETIDOS EN SCRIPTS _______________________________________________________________
				List<File> listaFicherosScriptTotales = new ArrayList<File>();
				boolean existeScripts=false;	
				if (datos_ficheroXML_bajo_test.getFileScriptsAutomaticos()!=null) {
					if (datos_ficheroXML_bajo_test.getFileScriptsAutomaticos().exists()) {
						existeScripts = true;
						listaFicherosScriptTotales = Archivos.listarArchivosScript(datos_ficheroXML_bajo_test.getFileScriptsAutomaticos());				
					} else {
						texto="ERROR. No se encuentra carpeta de scripts";						
						escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);
					}
				} else {
					texto="ERROR. No se ha declarado pathScriptsAutomaticos en sicamPC.sml";					
					escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);
				}
				
				int longitud = listaFicherosScriptTotales.size();
				texto="numero de  scripts: "+ longitud;				
				escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);			
							
				// se busca en los scripts------------------------------------------------			
				//el for de inFileScript es para cada script					
				int usadoElemRepes=0;
				for (int inFileScript = 0; inFileScript < listaFicherosScriptTotales.size(); inFileScript++) {			    
				    //buscar elementos con nombre repetido en cada script //listaNombreObjetosRepetidosEnXML
				    List<String> listaTotalNombreObjetosRepetidosEnXML = datos_ficheroXML_bajo_test.getListaTotalNombreObjetosRepetidosEnXML();
				    for (int iElemRep=0;iElemRep<listaTotalNombreObjetosRepetidosEnXML.size();iElemRep++) {
				    	String string_a_buscar = listaTotalNombreObjetosRepetidosEnXML.get(iElemRep);
				    	File ficheroScript = listaFicherosScriptTotales.get(inFileScript);				    	
				    	int apariciones = utilScript.buscaStringScript_delimitado(string_a_buscar, ficheroScript);
				    	if (apariciones>0) {
				    		texto = "ERROR. Se usa " + apariciones + " veces el elemento repetido (" + string_a_buscar + ") en script: " + ficheroScript;

							usadoElemRepes+=apariciones;
							elementosRepesUsadosTotal+=apariciones;					    	
							escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);
						}
				    }
				}
			    if (usadoElemRepes==0&&calculaNombreObjetosRepetidosEnXML_usados_en_Scripts) {
			    	texto = "INFO: No se usan elementos repes en scripts.";			    	
					escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);
			    }	
			    if (usadoElemRepes>0&&calculaNombreObjetosRepetidosEnXML_usados_en_Scripts) {
			    	texto = "ERROR: Elementos repetidos que se usan en script: " + usadoElemRepes;			    	
					escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);
			    }		
			    // ____________________________________________________________________________________________			    
				texto = "elementosRepesUsadosTotal: " + elementosRepesUsadosTotal;				
				escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);				
			}
		} // fin del for de todos los ficheros xml
		
	    if (calculaNombreObjetosRepetidosEnXML_usados_en_Scripts) {
			texto = "elementosRepesUsadosTotal en todo el analisis: " + elementosRepesUsadosTotal;			
			escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);
	    }
    
    	
    }
	
}
