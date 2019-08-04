
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;


//016: versión que calcula identificadores no usados
//017: * Ya no se usa lista de objetos (un objeto por fichero).
//     * listaDatosFicheroXML pasa a ser datosFicheroXML
//     * el parseador de XML se mete en la clase DatosFichero

public class main {
	// indicamos la ruta donde vamos a buscar los xml	
	static String rutaBase = "C:\\temp3";
	//static String rutaBase = "C:\\CAFs_SBS\\ENCE\\ramas\\ENCE_DESARROLLO\\SIST\\ValidaciÃ³n\\Entorno\\ContextosPrueba\\";
	static File ruta = new File(rutaBase);
	
	static File ficheroSalida = new File(ruta.getAbsolutePath() + "\\analisisXML.txt");
	static int indiceFichero=0;
	static String texto="";	

	public static void main(String argv[])   {		
		if (ficheroSalida.exists()) {
			ficheroSalida.delete();
		}
		
		List<File> listaFicherosXML = new ArrayList<File>();
		List<File> listaFicherosXML_validos = new ArrayList<File>();
		listaFicherosXML = Archivos.listarArchivosRecursivamente(ruta);			
				
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

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		timestamp = new Timestamp(System.currentTimeMillis());
		texto = timestamp+"";
		System.out.println(timestamp);
		escribeResultados.escribe(texto+"\n", ficheroSalida);
				
		// bucle para procesar cada objeto contenido en el array de listaDatosFicheroXML			
		for (indiceFichero=0;indiceFichero<listaFicherosXML_validos.size();indiceFichero++){
			Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
			DatosFicheroXML datosFicheroXML = new DatosFicheroXML(listaFicherosXML.get(indiceFichero)); 
   	    
			texto=indiceFichero+ " "+datosFicheroXML.getFichero()+"";
			System.out.println(texto);
			escribeResultados.escribe(texto+"\n", ficheroSalida);
				    	    	
	    	// llamamos a la funcion para que cuente los obejtos del S3e del fichero 0
			datosFicheroXML.cuentaObjetosS3e();					
			
			// sacamos lista de numero de cada objeto del S3e. (60 MCS, 100 ED, etc)
			texto="" + datosFicheroXML.imprimelistaNumeroObjetosS3e();
			System.out.println(texto);
			escribeResultados.escribe(texto+"\n", ficheroSalida);	

			System.out.println("");
			escribeResultados.escribe("\n", ficheroSalida);	
			
			// llamamos a la funcion para que cuente los objetos del S3e del fichero 
			datosFicheroXML.calculaNombreObjetosRepetidosS3e();
					
			// imprimimos solo los elementos repetidos por tipo					
			texto="" + datosFicheroXML.imprimeListaNombreObjetosRepetidosEnXML();
			System.out.println(texto);
			escribeResultados.escribe(texto+"\n", ficheroSalida);	
			
			texto="Elementos no usados:";
			System.out.println(texto);
			escribeResultados.escribe(texto+"\n", ficheroSalida);
			
			datosFicheroXML.calculaIdentificadorObjetosNoUsadosS3e();					
			texto=datosFicheroXML.imprimeIdentificadorObjetosNoUsadosS3e();
			System.out.println(texto);
			escribeResultados.escribe(texto+"\n", ficheroSalida);
			
			Timestamp timestamp2 = new Timestamp(System.currentTimeMillis());
			long milisenconds = timestamp2.getTime()-timestamp1.getTime();
			
			texto = "duracion: " + milisenconds+" mseg";
			System.out.println(texto);
			escribeResultados.escribe(texto+"\n", ficheroSalida);
			
			texto="--------------------------------------------------------\n";
			System.out.println(texto);
			escribeResultados.escribe(texto+"\n", ficheroSalida);
		}

	}
	

}
