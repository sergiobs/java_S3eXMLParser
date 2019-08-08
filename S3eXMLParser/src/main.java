
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

public class main {
	//static int indiceFichero=0;
	static String texto="";	

	public static void main(String argv[])   {		
		String entradaTeclado = "";
		Scanner entradaEscaner = new Scanner (System.in);
		boolean calculaNombreObjetosRepetidosEnXML = false;
		boolean calculaIdentificadorObjetos_noUsadosEnXML = false;
		boolean calculaNombreObjetosRepetidosEnXML_usados_en_Scripts = false;
		
		// indicamos la ruta donde vamos a buscar los xml o bien la recogemos de argumento 1
		String rutaBase = "C:\\temp5";
		//static String rutaBase = "C:\\CAFs_SBS\\ENCE\\ramas\\ENCE_DESARROLLO\\SIST\\Validación\\Entorno\\";	
		
		if (argv.length> 0) {
			rutaBase = argv[0];
		}
		File ruta = new File(rutaBase);

		List<File> listaFicherosXML = new ArrayList<File>();
		List<File> listaFicherosXML_validos = new ArrayList<File>();
		listaFicherosXML = Archivos.listarArchivosXMLRecursivamente(ruta);		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String ficheroSalidaNombre="";
		ficheroSalidaNombre =timestamp+"";
		ficheroSalidaNombre=ficheroSalidaNombre.replace(":", "-");		
		System.out.println(ficheroSalidaNombre);
		
		File ficheroSalida = new File(ruta.getAbsolutePath() + "\\analisisXML_"+ficheroSalidaNombre+".txt");
		
		if (ficheroSalida.exists()) {
			ficheroSalida.delete();
		}
		
		texto = "1. ¿Calculamos elementos con Nombre repetidos en XML (s/n)?: ";
		System.out.print(texto);
		escribeResultados.escribe(texto, ficheroSalida);
        entradaTeclado = entradaEscaner.nextLine ();
		texto = entradaTeclado;
		System.out.println(texto);
		escribeResultados.escribe(texto+"\n", ficheroSalida);
        if (entradaTeclado.equalsIgnoreCase("s")) {
        	calculaNombreObjetosRepetidosEnXML = true;
        }
        
		texto = "2. ¿Calculamos elementos en XML no utilizados (s/n)?: ";
		System.out.print(texto);
		escribeResultados.escribe(texto, ficheroSalida);
        entradaTeclado = entradaEscaner.nextLine ();
		texto = entradaTeclado;
		System.out.println(texto);
		escribeResultados.escribe(texto+"\n", ficheroSalida);
        if (entradaTeclado.equalsIgnoreCase("s")) {
        	calculaIdentificadorObjetos_noUsadosEnXML = true;
        }    
        
		texto = "3. ¿Calculamos para los elementos con Nombre repetidos en XML, si son usados en scripts (s/n)?: ";
		System.out.print(texto);
		escribeResultados.escribe(texto, ficheroSalida);
        entradaTeclado = entradaEscaner.nextLine ();
		texto = entradaTeclado;
		System.out.println(texto);
		escribeResultados.escribe(texto+"\n", ficheroSalida);
        if (entradaTeclado.equalsIgnoreCase("s")) {        	
            if (calculaNombreObjetosRepetidosEnXML) {            	
            	calculaNombreObjetosRepetidosEnXML_usados_en_Scripts = true;
            } else {
        		texto = "No es posible sin calcular elementos repetidos en XML";
        		System.out.println(texto);
        		escribeResultados.escribe(texto+"\n", ficheroSalida);
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
				System.out.println(texto);
				escribeResultados.escribe(texto+"\n", ficheroSalida);
			}
		}
		
		
		
		int elementosRepesUsadosTotal=0;
				
		// bucle para procesar cada objeto contenido en el array de listaDatosFicheroXML			
		for (int indiceFichero=0;indiceFichero<listaFicherosXML_validos.size();indiceFichero++){
			Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
			DatosFicheroXML datosFicheroXML = new DatosFicheroXML(listaFicherosXML_validos.get(indiceFichero)); 
   	    
			texto="\n"+timestamp+" - " + indiceFichero+ " "+datosFicheroXML.getFichero()+"\n---------------------------------------------------------------------------------------------";
			System.out.println(texto);
			escribeResultados.escribe(texto+"\n", ficheroSalida);
			
			if (datosFicheroXML.getFicheroSicamPC() == null) {
				texto="ERROR: No existe sicampc.xml";
				System.out.println(texto);
				escribeResultados.escribe(texto+"\n", ficheroSalida);
			}
				    	    	
	    	// llamamos a la funcion para que cuente los obejtos del S3e del fichero 0
			datosFicheroXML.cuentaObjetosS3e();					
			
			// sacamos lista de numero de cada objeto del S3e. (60 MCS, 100 ED, etc)
			texto="*** Numero de objetos:\n" + datosFicheroXML.imprimelistaNumeroObjetosS3e();
			System.out.println(texto);
			escribeResultados.escribe(texto+"\n", ficheroSalida);	
		//	System.out.println("");
		//	escribeResultados.escribe("\n", ficheroSalida);	
			
			if (calculaNombreObjetosRepetidosEnXML) {
				// llamamos a la funcion para que cuente los objetos del S3e del fichero 
				datosFicheroXML.calculaNombreObjetosRepetidosS3e();
						
				// imprimimos solo los elementos repetidos por tipo
				texto="*** Objetos repetidos:";
				System.out.println(texto);
				escribeResultados.escribe(texto+"\n", ficheroSalida);	
				
				texto="" + datosFicheroXML.imprimeListaNombreObjetosRepetidosEnXML();
				System.out.println(texto);
				escribeResultados.escribe(texto+"\n", ficheroSalida);
			}
				
			
			if (calculaIdentificadorObjetos_noUsadosEnXML) {
				texto="*** Elementos no usados:";
				System.out.println(texto);
				escribeResultados.escribe(texto+"\n", ficheroSalida);
				
				datosFicheroXML.calculaIdentificadorObjetosNoUsadosS3e();					
				texto=datosFicheroXML.imprimeIdentificadorObjetosNoUsadosS3e();
				System.out.println(texto);
				escribeResultados.escribe(texto+"\n", ficheroSalida);				
			}
			
			if (calculaNombreObjetosRepetidosEnXML_usados_en_Scripts&&calculaNombreObjetosRepetidosEnXML) {
				// BUSQUEDA DE ELEMENTOS REPETIDOS EN SCRIPTS _______________________________________________________________
				List<File> listaFicherosScriptTotales = new ArrayList<File>();		
//				texto="datosFicheroXML.getFileScriptsAutomaticos(): " + datosFicheroXML.getFileScriptsAutomaticos();
//				System.out.println(texto);
//				escribeResultados.escribe(texto+"\n", ficheroSalida);			
				
				boolean existeScripts=false;	
				if (datosFicheroXML.getFileScriptsAutomaticos()!=null) {
					if (datosFicheroXML.getFileScriptsAutomaticos().exists()) {
						existeScripts = true;
						listaFicherosScriptTotales = Archivos.listarArchivosScript(datosFicheroXML.getFileScriptsAutomaticos());				
					} else {
						texto="ERROR. No se encuentra carpeta de scripts";
						System.out.println(texto);
						escribeResultados.escribe(texto+"\n", ficheroSalida);
					}
				} else {
					texto="ERROR. No se ha declarado pathScriptsAutomaticos en sicamPC.sml";
					System.out.println(texto);
					escribeResultados.escribe(texto+"\n", ficheroSalida);
				}

				
				int longitud = listaFicherosScriptTotales.size();
				texto="numero de scripts: "+ longitud;
				System.out.println(texto);
				escribeResultados.escribe(texto+"\n", ficheroSalida);
				
//				texto="getPathScriptsAutomaticos: "+ datosFicheroXML.getPathScriptsAutomaticos();
//				System.out.println(texto);
//				escribeResultados.escribe(texto+"\n", ficheroSalida);			
							
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
					    	System.out.println(texto);
							escribeResultados.escribe(texto+"\n", ficheroSalida);
						}
				    }
				}
			    if (usadoElemRepes==0&&calculaNombreObjetosRepetidosEnXML_usados_en_Scripts) {
			    	texto = "INFO: No se usan elementos repes en scripts.";
			    	System.out.println(texto);
					escribeResultados.escribe(texto+"\n", ficheroSalida);
			    }		    
			    // ____________________________________________________________________________________________
			    
				texto = "elementosRepesUsadosTotal: " + elementosRepesUsadosTotal;
				System.out.println(texto);
				escribeResultados.escribe(texto+"\n", ficheroSalida);		    
				
			}

			
//			Timestamp timestamp2 = new Timestamp(System.currentTimeMillis());
//			long milisenconds = timestamp2.getTime()-timestamp1.getTime();
			
//			texto = "duracion: " + milisenconds+" mseg\n\n\n";
//			System.out.println(texto);
//			escribeResultados.escribe(texto+"\n", ficheroSalida);
		}
		
	    if (calculaNombreObjetosRepetidosEnXML_usados_en_Scripts) {
			texto = "elementosRepesUsadosTotal en todo el analisis: " + elementosRepesUsadosTotal;
			System.out.println(texto);
			escribeResultados.escribe(texto+"\n", ficheroSalida);
	    }
		
		
		texto = "****************************************************\nFin del analisis\n\n\n";
		System.out.println(texto);
		escribeResultados.escribe(texto+"\n", ficheroSalida);
		
		Timestamp timestamp_fin = new Timestamp(System.currentTimeMillis());
		long milisenconds = timestamp_fin.getTime()-timestamp_inicial.getTime();
		texto = "\nduracion total: " + milisenconds+" mseg\n\n\n";
		System.out.println(texto);
		escribeResultados.escribe(texto+"\n", ficheroSalida);

	}
	

}
