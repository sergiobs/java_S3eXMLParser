
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.Timestamp;

//016 		* versión que calcula identificadores no usados
//017 		* Ya no se usa lista de objetos (un objeto por fichero).
//  		* listaDatosFicheroXML pasa a ser datosFicheroXML
//     		* el parseador de XML se mete en la clase DatosFichero
//018 		* Se leen sicamPC y se obtiene lista de ficheros script
//019 		* Se hace busqueda de elementos repetidos en script		
//				019.01 	* fichero de salida contiene timestamp en su nombre
//              019.02 	* se tiene en cuenta que puede no existir ficheros de script
//				019.03 	* se tiene en cuenta que puede no haber pathScriptsAutomaticos en sicamPC.xml
//				019.04 	* se tiene en cuenta que puede no haber fichero sicamPC.xml
//                      * Se habilita lectura de argumento de entrada para indicar ruta
//020 		* Lee tipos de objeto de S3e que cumplan criterios de varios arguemntos/valores
//			* Calcula numero MCSs teoricos en S3e y Q4
//			  020.01 	* optimizacion de codigo
//021 		* Lee tipos de objeto de S3e que cumplan criterios de varios arguemntos/valores y además 
//            un criterio que puede ser de '=' o '!' (distinto). ASi es posible buscar por ejemplo
//            objetos que cumplan que determinado argumento sea distinto de 65535
//022 		* Se corrige el conteo de MCS 
//				022.01 	* se restan los elementos NO-usados en el calculo de los MCS
//023 		* Se hacen busquedas basicas en script
//024 		* Se agregan los TRMF
//              024.03 * Prueba sin cambios
public class main {
	
	public static void main(String argv[])   {		
		String texto="";
		
		Scanner entradaEscaner = new Scanner (System.in);
		boolean trataXML = false;
		boolean calculaNombreObjetosRepetidosEnXML = false;
		boolean calculaIdentificadorObjetos_noUsadosEnXML = false;
		boolean calculaNombreObjetosRepetidosEnXML_usados_en_Scripts = false;
		boolean calcula_XXX_scripts = false;
		
		int nivelTraza = 2;
		
		// indicamos la ruta donde vamos a buscar los xml o bien la recogemos de argumento 1
		String rutaBase = "C:\\temp3";
		//String rutaBase = "C:\\CAFs_SBS\\ENCE\\ramas\\ENCE_DESARROLLO\\SIST\\Validación\\Entorno\\";	
		
		if (argv.length> 0) {
			rutaBase = argv[0];
		}
		File ruta = new File(rutaBase);		
			
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String ficheroSalidaNombre="";
		String ficheroSalidaNombre_numMCS="";
		
		ficheroSalidaNombre =timestamp+"";
		ficheroSalidaNombre=ficheroSalidaNombre.replace(":", "-");	
		ficheroSalidaNombre_numMCS=ficheroSalidaNombre+"_numMCS";
		
		File ficheroSalida = new File(ruta.getAbsolutePath() + "\\analisisXML_"+ficheroSalidaNombre+".txt");		
		File ficheroSalida_numMCS = new File(ruta.getAbsolutePath() + "\\analisisXML_"+	ficheroSalidaNombre_numMCS+".txt");
//		if (ficheroSalida.exists()) {
//			ficheroSalida.delete();
//		}


		texto = "1. ¿Tratamos los ficheros XML (s/n)?: ";		
		escribeResultados.escribe(texto, ficheroSalida, nivelTraza);
		texto = entradaEscaner.nextLine ();		 		
		escribeResultados.escribe(texto+"\n", ficheroSalida, 1);
        if (texto.equalsIgnoreCase("s")) {
        	trataXML = true;
        }		
		
        if (trataXML) {
        	texto = "1.1. ¿Calculamos elementos con Nombre repetidos en XML (s/n)?: ";		
    		escribeResultados.escribe(texto, ficheroSalida, nivelTraza);
    		texto = entradaEscaner.nextLine ();		 		
    		escribeResultados.escribe(texto+"\n", ficheroSalida, 1);
            if (texto.equalsIgnoreCase("s")) {
            	calculaNombreObjetosRepetidosEnXML = true;
            }
            
    		texto = "1.2. ¿Calculamos elementos en XML no utilizados (s/n)?: ";		
    		escribeResultados.escribe(texto, ficheroSalida, nivelTraza);
    		texto = entradaEscaner.nextLine ();			
    		escribeResultados.escribe(texto+"\n", ficheroSalida, 1);
            if (texto.equalsIgnoreCase("s")) {
            	calculaIdentificadorObjetos_noUsadosEnXML = true;
            }    
            
    		texto = "1.3. ¿Calculamos para los elementos con Nombre repetidos en XML, si son usados en scripts (s/n)?: ";		
    		escribeResultados.escribe(texto, ficheroSalida, nivelTraza);

    		texto = entradaEscaner.nextLine ();	
    		escribeResultados.escribe(texto+"\n", ficheroSalida, 1);
            if (texto.equalsIgnoreCase("s")) {        	
                if (calculaNombreObjetosRepetidosEnXML) {            	
                	calculaNombreObjetosRepetidosEnXML_usados_en_Scripts = true;
                } else {
            		texto = "No es posible sin calcular elementos repetidos en XML";        		
            		escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);
            		calculaNombreObjetosRepetidosEnXML_usados_en_Scripts = false;
                }
            }  
        }
        
		texto = "2. ¿Buscamos llamadas a MCS por XXX en ficheros script (s/n)?: ";		
		escribeResultados.escribe(texto, ficheroSalida, nivelTraza);
		texto = entradaEscaner.nextLine ();	
		escribeResultados.escribe(texto+"\n", ficheroSalida, 1);
        if (texto.equalsIgnoreCase("s")) {  
        	calcula_XXX_scripts = true;
        } 
     
        Timestamp timestamp_inicial = new Timestamp(System.currentTimeMillis());
		
		
		// para cada script
		if (calcula_XXX_scripts) {
			List<File> listaFicherosSCRIPT = new ArrayList<File>();
	 		texto = "Buscando scripts en " + rutaBase;
			escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);
			listaFicherosSCRIPT = Archivos.listarArchivosScript(ruta);	
			
			int apariciones = 0;
			int apariciones_total =0;
			String string_a_buscar = ("205");
			for (int in_Fichero_script=0;in_Fichero_script<listaFicherosSCRIPT.size();in_Fichero_script++){ 
				File ficheroScript = listaFicherosSCRIPT.get(in_Fichero_script);				    	
		    	//apariciones = utilScript.buscaStringScript_delimitado(string_a_buscar, ficheroScript);
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
		
		if (trataXML) {
			List<File> listaFicherosXML = new ArrayList<File>();
			List<File> listaFicherosXML_validos = new ArrayList<File>();		
	 		texto = "Buscando ficheros en " + rutaBase;
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
					
			int elementosRepesUsadosTotal=0;				
			// bucle para procesar cada objeto contenido en el array de listaDatosFicheroXML			
			for (int indiceFichero=0;indiceFichero<listaFicherosXML_validos.size();indiceFichero++){
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
					// llamamos a la funcion para que cuente los objetos del S3e del fichero 
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
					texto=datos_ficheroXML_bajo_test.imprime_Id_No_UsadosS3e();				
					escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);				
				}
				
				// sacamos lista de numero de cada objeto del S3e. (60 MCS, 100 ED, etc)
				texto="\n*** Numero de objetos:\n" + datos_ficheroXML_bajo_test.imprimelistaNumeroObjetosS3e();			
				escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);	
	
				// Estimamos MCS
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
		texto = "****************************************************\nFin del analisis\n\n\n";		
		escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);		
		Timestamp timestamp_fin = new Timestamp(System.currentTimeMillis());
		long milisenconds = timestamp_fin.getTime()-timestamp_inicial.getTime();
		texto = "\nduracion total: " + milisenconds+" mseg\n\n\n";		
		escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);		
		entradaEscaner.close();

	}
	

}
