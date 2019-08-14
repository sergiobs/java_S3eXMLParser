
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

public class main {
	
	public static void main(String argv[])   {		
		String texto="";
		
		Scanner entradaEscaner = new Scanner (System.in);
		boolean calculaNombreObjetosRepetidosEnXML = false;
		boolean calculaIdentificadorObjetos_noUsadosEnXML = false;
		boolean calculaNombreObjetosRepetidosEnXML_usados_en_Scripts = false;
		int nivelTraza = 2;
		
		// indicamos la ruta donde vamos a buscar los xml o bien la recogemos de argumento 1
		//String rutaBase = "C:\\temp3";
		String rutaBase = "C:\\CAFs_SBS\\ENCE\\ramas\\ENCE_DESARROLLO\\SIST\\Validación\\Entorno\\";	
		
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

		List<File> listaFicherosXML = new ArrayList<File>();
		List<File> listaFicherosXML_validos = new ArrayList<File>();		
 		texto = "Buscando ficheros en " + rutaBase;
		escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);
		listaFicherosXML = Archivos.listarArchivosXMLRecursivamente(ruta);
		
		texto = "1. ¿Calculamos elementos con Nombre repetidos en XML (s/n)?: ";		
		escribeResultados.escribe(texto, ficheroSalida, nivelTraza);
		texto = entradaEscaner.nextLine ();		 		
		escribeResultados.escribe(texto+"\n", ficheroSalida, 1);
        if (texto.equalsIgnoreCase("s")) {
        	calculaNombreObjetosRepetidosEnXML = true;
        }
        
		texto = "2. ¿Calculamos elementos en XML no utilizados (s/n)?: ";		
		escribeResultados.escribe(texto, ficheroSalida, nivelTraza);
		texto = entradaEscaner.nextLine ();			
		escribeResultados.escribe(texto+"\n", ficheroSalida, 1);
        if (texto.equalsIgnoreCase("s")) {
        	calculaIdentificadorObjetos_noUsadosEnXML = true;
        }    
        
		texto = "3. ¿Calculamos para los elementos con Nombre repetidos en XML, si son usados en scripts (s/n)?: ";		
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
        
		Timestamp timestamp_inicial = new Timestamp(System.currentTimeMillis());		 
				
		// se recorre todos los archivos y crea un objeto tipo DatosFicheroXML por fichero valido	
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
			DatosFicheroXML datosFicheroXML = new DatosFicheroXML(listaFicherosXML_validos.get(indiceFichero)); 
   	    
			texto="\n"+timestamp+" - " + indiceFichero+ " "+datosFicheroXML.getFichero()+"\n---------------------------------------------------------------------------------------------";			
			escribeResultados.escribe(texto+"", ficheroSalida, nivelTraza);
			texto=indiceFichero+ "\t"+datosFicheroXML.getFichero();
			escribeResultados.escribe(texto+"", ficheroSalida_numMCS, 1);
			
			if (datosFicheroXML.getFicheroSicamPC() == null) {
				texto="ERROR: No existe sicampc.xml";				
				escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);
			}
				    	    	
	    	// llamamos a la funcion para que cuente los objetos del S3e del fichero
			datosFicheroXML.cuentaObjetosS3e();		

			
			if (calculaNombreObjetosRepetidosEnXML) {
				// llamamos a la funcion para que cuente los objetos del S3e del fichero 
				datosFicheroXML.calculaNombreObjetosRepetidosS3e();
				
				// imprimimos solo los elementos repetidos por tipo
				texto="*** Objetos repetidos:";				
				escribeResultados.escribe(texto+"\t", ficheroSalida, nivelTraza);					
				texto="" + datosFicheroXML.imprimeListaNombreObjetosRepetidosEnXML();				
				escribeResultados.escribe(texto+"\t", ficheroSalida, nivelTraza);
			}
				
			if (calculaIdentificadorObjetos_noUsadosEnXML) {
				texto="\n*** Elementos no usados:";				
				escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);				
				datosFicheroXML.calcula_Id_Objetos_UsadosS3e();					
				texto=datosFicheroXML.imprime_Id_No_UsadosS3e();				
				escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);				
			}
			
			// sacamos lista de numero de cada objeto del S3e. (60 MCS, 100 ED, etc)
			texto="*** Numero de objetos:\n" + datosFicheroXML.imprimelistaNumeroObjetosS3e();			
			escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);	

			// Estimamos MCS
			if (datosFicheroXML.estimaMCS()==0) {
				texto="*** Estimacion MCS:\n" + datosFicheroXML.imprimeMCS_Estimados();
				escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);
				texto=datosFicheroXML.imprimeMCS_Estimados_corto();
				escribeResultados.escribe(texto+"", ficheroSalida_numMCS, 1);
				
				// si ademas hemos buscado los no usados, recalculamos la estimación
				if (calculaIdentificadorObjetos_noUsadosEnXML) {
					datosFicheroXML.estimaMCS_ahorro();
					texto="*** Estimacion MCS, tras eliminar los repetidos:\n" + datosFicheroXML.imprimeMCS_EstimadosF();
					escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);		
					texto=datosFicheroXML.imprimeMCS_EstimadosF_corto();
					escribeResultados.escribe(texto+"\n", ficheroSalida_numMCS, 1);
				}
				
				
			} else if(datosFicheroXML.estimaMCS()==-1) {
				texto="*** ERROR en estimaMCS";
				escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);
			}
			
			if (calculaNombreObjetosRepetidosEnXML_usados_en_Scripts&&calculaNombreObjetosRepetidosEnXML) {
				// BUSQUEDA DE ELEMENTOS REPETIDOS EN SCRIPTS _______________________________________________________________
				List<File> listaFicherosScriptTotales = new ArrayList<File>();
				boolean existeScripts=false;	
				if (datosFicheroXML.getFileScriptsAutomaticos()!=null) {
					if (datosFicheroXML.getFileScriptsAutomaticos().exists()) {
						existeScripts = true;
						listaFicherosScriptTotales = Archivos.listarArchivosScript(datosFicheroXML.getFileScriptsAutomaticos());				
					} else {
						texto="ERROR. No se encuentra carpeta de scripts";						
						escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);
					}
				} else {
					texto="ERROR. No se ha declarado pathScriptsAutomaticos en sicamPC.sml";					
					escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);
				}
				
				int longitud = listaFicherosScriptTotales.size();
				texto="numero de scripts: "+ longitud;				
				escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);			
							
				// se busca en los scripts------------------------------------------------			
				//el for de inFileScript es para cada script					
				int usadoElemRepes=0;
				for (int inFileScript = 0; inFileScript < listaFicherosScriptTotales.size(); inFileScript++) {			    
				    //buscar elementos con nombre repetido en cada script //listaNombreObjetosRepetidosEnXML
				    List<String> listaTotalNombreObjetosRepetidosEnXML = datosFicheroXML.getListaTotalNombreObjetosRepetidosEnXML();
				    for (int iElemRep=0;iElemRep<listaTotalNombreObjetosRepetidosEnXML.size();iElemRep++) {
				    	texto = datosFicheroXML.buscaStringScript(listaTotalNombreObjetosRepetidosEnXML.get(iElemRep), 
				    			listaFicherosScriptTotales.get(inFileScript));
						if (texto.length()>0) {
							usadoElemRepes++;
							elementosRepesUsadosTotal++;					    	
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
		}
		
	    if (calculaNombreObjetosRepetidosEnXML_usados_en_Scripts) {
			texto = "elementosRepesUsadosTotal en todo el analisis: " + elementosRepesUsadosTotal;			
			escribeResultados.escribe(texto+"\n", ficheroSalida, nivelTraza);
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
