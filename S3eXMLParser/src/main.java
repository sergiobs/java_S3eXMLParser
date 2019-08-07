
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

//016 		* versión que calcula identificadores no usados
//017 		* Ya no se usa lista de objetos (un objeto por fichero).
//  		* listaDatosFicheroXML pasa a ser datosFicheroXML
//     		* el parseador de XML se mete en la clase DatosFichero
//018 		* Se leen sicamPC y se obtiene lista de ficheros script
//019 		* Se hace busqueda de elementos repetidos en script		
//				019.01 	* fichero de salida contiene timestamp en su nombre

public class main {
	// indicamos la ruta donde vamos a buscar los xml	
	//static String rutaBase = "C:\\temp4";
	static String rutaBase = "C:\\CAFs_SBS\\ENCE\\ramas\\ENCE_DESARROLLO\\SIST\\Validación\\Entorno\\";
	static File ruta = new File(rutaBase);
	
	
	static int indiceFichero=0;
	static String texto="";	

	public static void main(String argv[])   {		
		List<File> listaFicherosXML = new ArrayList<File>();
		List<File> listaFicherosXML_validos = new ArrayList<File>();
		listaFicherosXML = Archivos.listarArchivosXMLRecursivamente(ruta);		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String ficheroSalidaNombre="";
		ficheroSalidaNombre =timestamp+"";
		ficheroSalidaNombre=ficheroSalidaNombre.replace(":", "-");
		//ficheroSalidaNombre="";
		System.out.println(ficheroSalidaNombre);
		
		File ficheroSalida = new File(ruta.getAbsolutePath() + "\\analisisXML_"+ficheroSalidaNombre+".txt");
		
		if (ficheroSalida.exists()) {
			ficheroSalida.delete();
		}
				
		// se recorre todos los archivos y crea un objeto tipo DatosFicheroXML por fichero valido	
		int nFicheroValidado = 0;
		for (int i = 0; i < listaFicherosXML.size(); i++) {
		    File ficheroXMLaValidar;
		    ficheroXMLaValidar = listaFicherosXML.get(i);
		    boolean esS3e = Archivos.ficheroXMLValido(ficheroXMLaValidar);
		   		
			if (esS3e) {
				nFicheroValidado++;
				listaFicherosXML_validos.add(ficheroXMLaValidar);
				//datosFicheroXML.add(new DatosFicheroXML());
				//datosFicheroXML.get(nFicheroValidado-1).setFichero(listaFicherosXML.get(i));
				texto=nFicheroValidado+ "\t "+listaFicherosXML_validos.get(nFicheroValidado-1);
				System.out.println(texto);
				escribeResultados.escribe(texto+"\n", ficheroSalida);
			}
		}
				
		timestamp = new Timestamp(System.currentTimeMillis());
		texto = timestamp+" - " + "Comienza el analisis de ficheros: .....\n";
		System.out.println(timestamp);
		escribeResultados.escribe(texto+"\n", ficheroSalida);
		
		int elementosRepesUsadosTotal=0;
				
		// bucle para procesar cada objeto contenido en el array de listaDatosFicheroXML			
		for (indiceFichero=0;indiceFichero<listaFicherosXML_validos.size();indiceFichero++){
			Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
			DatosFicheroXML datosFicheroXML = new DatosFicheroXML(listaFicherosXML_validos.get(indiceFichero)); 
   	    
			texto=timestamp+" - " + indiceFichero+ " "+datosFicheroXML.getFichero()+"\n-------------------------------------------\n";
			System.out.println(texto);
			escribeResultados.escribe(texto+"\n", ficheroSalida);
				    	    	
	    	// llamamos a la funcion para que cuente los obejtos del S3e del fichero 0
			datosFicheroXML.cuentaObjetosS3e();					
			
			// sacamos lista de numero de cada objeto del S3e. (60 MCS, 100 ED, etc)
			texto="*** Numero de objetos:\n" + datosFicheroXML.imprimelistaNumeroObjetosS3e();
			System.out.println(texto);
			escribeResultados.escribe(texto+"\n", ficheroSalida);	

			System.out.println("");
			escribeResultados.escribe("\n", ficheroSalida);	
			
			// llamamos a la funcion para que cuente los objetos del S3e del fichero 
			datosFicheroXML.calculaNombreObjetosRepetidosS3e();
					
			// imprimimos solo los elementos repetidos por tipo
			texto="*** Objetos repetidos:";
			System.out.println(texto);
			escribeResultados.escribe(texto+"\n", ficheroSalida);	
			
			texto="" + datosFicheroXML.imprimeListaNombreObjetosRepetidosEnXML();
			System.out.println(texto);
			escribeResultados.escribe(texto+"\n", ficheroSalida);	
			
			texto="*** Elementos no usados:";
			System.out.println(texto);
			escribeResultados.escribe(texto+"\n", ficheroSalida);
			
			datosFicheroXML.calculaIdentificadorObjetosNoUsadosS3e();					
			texto=datosFicheroXML.imprimeIdentificadorObjetosNoUsadosS3e();
			System.out.println(texto);
			escribeResultados.escribe(texto+"\n", ficheroSalida);
			
			Timestamp timestamp2 = new Timestamp(System.currentTimeMillis());
			long milisenconds = timestamp2.getTime()-timestamp1.getTime();
			
			List<File> listaFicherosScriptTotales = new ArrayList<File>();		
			listaFicherosScriptTotales = Archivos.listarArchivosScript(datosFicheroXML.getFileScriptsAutomaticos());
			int longitud = listaFicherosScriptTotales.size();
			texto="numero de scripts: "+ longitud;
			System.out.println(texto);
			escribeResultados.escribe(texto+"\n", ficheroSalida);
			
			texto="getPathScriptsAutomaticos: "+ datosFicheroXML.getPathScriptsAutomaticos();
			System.out.println(texto);
			escribeResultados.escribe(texto+"\n", ficheroSalida);			
						
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
		    if (usadoElemRepes==0) {
		    	texto = "INFO: No se usan elementos repes en scripts.";
		    	System.out.println(texto);
				escribeResultados.escribe(texto+"\n", ficheroSalida);
		    }
		    
			texto = "elementosRepesUsadosTotal: " + elementosRepesUsadosTotal;
			System.out.println(texto);
			escribeResultados.escribe(texto+"\n", ficheroSalida);		    
		    
			texto = "duracion: " + milisenconds+" mseg\n\n\n";
			System.out.println(texto);
			escribeResultados.escribe(texto+"\n", ficheroSalida);
		}
		
		texto = "elementosRepesUsadosTotal: " + elementosRepesUsadosTotal;
		System.out.println(texto);
		escribeResultados.escribe(texto+"\n", ficheroSalida);
		
		texto = "****************************************************\nFin del analisis\n\n\n";
		System.out.println(texto);
		escribeResultados.escribe(texto+"\n", ficheroSalida);

	}
	

}
