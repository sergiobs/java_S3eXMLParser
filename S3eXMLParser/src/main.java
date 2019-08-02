import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


//009 lenovo 

public class main {
	static DatosFicheroXML fichero1 = new DatosFicheroXML(); 
	static List<DatosFicheroXML> listaDatosFicheroXML = new ArrayList<DatosFicheroXML>(); //una lista de objetos. un objeto por cada fichero xml
	
	// indicamos la ruta donde vamos a buscar los xml
	static File ruta = new File("C:\\temp\\");
	//static File ruta = new File("C:\\CAFs_SBS\\ENCE\\ramas\\ENCE_DESARROLLO\\SIST\\Validación\\Entorno\\ContextosPrueba\\");
	
	static File ficheroSalida = new File(ruta.getAbsolutePath() + "\\analisisXML.txt");
	static int indiceFichero=0;
	static String texto="";
	
	
	//******* ARREGLAR ESTO
	static boolean esS3e = false;
	static int contadorStart =0;

	public static void main(String argv[]) {		
		if (ficheroSalida.exists()) {
			ficheroSalida.delete();
		}
		
		// se crea una lista de ficheros xml en la ruta indicada
		List<File> listaFicherosXML = new ArrayList<File>();			
		listaFicherosXML = Archivos.listarArchivosRecursivamente(ruta);
						
		// se recorre todos los archivos y crea un objeto tipo DatosFicheroXML por fichero valido		
		for (int i = 0; i < listaFicherosXML.size(); i++) {
			listaDatosFicheroXML.add(new DatosFicheroXML());
			listaDatosFicheroXML.get(i).setFichero(listaFicherosXML.get(i));
			
			texto=i+ "\t\t\t "+listaDatosFicheroXML.get(i).getFichero();
			System.out.println(texto);
			escribeResultados.escribe(texto+"\n", ficheroSalida);			
		}
		
		
////////////////////////////////////////////		
//		SAXParserFactory saxParserFactory  = SAXParserFactory.newInstance();//		
//		try {
//			SAXParser saxParser = saxParserFactory.newSAXParser();
//	        MyHandler handler = new MyHandler();
//	        saxParser.parse(new File("C:\\temp\\ORI_3463_2_0.xml"), handler);
//			
//		} catch (ParserConfigurationException | SAXException | IOException e) {
//	        e.printStackTrace();
//	    }
////////////////////////////////////////////
		
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			
			DefaultHandler handler = new DefaultHandler() {
				
				public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
					String etiqueta = qName;							
										
					//if (etiqueta.equalsIgnoreCase("S3e")) 
						//esS3e = true;
					if (true) {
					// buscamos el startElement en nuestra lista de objetos a evaluar
						for (int startE=0; startE<listaDatosFicheroXML.get(indiceFichero).getNumeroObjetosS3e();startE ++) {
							// si la cabecera leida del xml esta en mi lista de obj s3e
							if (qName.equalsIgnoreCase(listaDatosFicheroXML.get(indiceFichero).getListaNombreObjetosS3e().get(startE))) { 
								listaDatosFicheroXML.get(indiceFichero).addNombreObjetoEnXML(attributes.getValue("Nombre"), startE);							
							}
						}	
					}
				}
			};
			
			System.out.println("\n");
			System.out.println("------------ Empieza la fiesta --------------------------------------------");
			for (indiceFichero=0;indiceFichero<listaFicherosXML.size();indiceFichero++){
				//System.out.println(listaDatosFicheroXML.get(indiceFichero).getFichero());
				
			    File ficheroXMLaExaminar;
			    ficheroXMLaExaminar = listaDatosFicheroXML.get(indiceFichero).getFichero();
			    
			    esS3e = Archivos.ficheroXMLValido(ficheroXMLaExaminar);
	    	    if (esS3e) {
					texto=indiceFichero+ "\t\t\t "+listaDatosFicheroXML.get(indiceFichero).getFichero();
					System.out.println(texto);
					escribeResultados.escribe(texto+"\n", ficheroSalida);
	    	    	saxParser.parse(ficheroXMLaExaminar, handler);
	    	    	// llamamos a la funcion para que cuente los obejtos del S3e del fichero 0
					listaDatosFicheroXML.get(indiceFichero).cuentaObjetosS3e();					
					
					// sacamos lista de numero de cada objeto del S3e
					for (int aa=0; aa<listaDatosFicheroXML.get(indiceFichero).getNumeroObjetosS3e();aa++) {
						//System.out.print(listaDatosFicheroXML.get(indiceFichero).getListaNombreObjetosS3e().get(aa) + ": " + listaDatosFicheroXML.get(indiceFichero).getListaNumeroObjetosS3e().get(aa) + "\t" )	;
						texto=""+listaDatosFicheroXML.get(indiceFichero).getListaNombreObjetosS3e().get(aa) + ": " + listaDatosFicheroXML.get(indiceFichero).getListaNumeroObjetosS3e().get(aa) + "\t";
						System.out.print(texto);
						escribeResultados.escribe(texto, ficheroSalida);	
					}
								
					//System.out.println("");
					texto="";
					System.out.println(texto);
					escribeResultados.escribe(texto+"\n", ficheroSalida);	
					
					// llamamos a la funcion para que cuente los obejtos del S3e del fichero 0
					listaDatosFicheroXML.get(indiceFichero).calculaObjetosRepetidosS3e();
					
					// imprimimos la lista de repetidos
					listaDatosFicheroXML.get(indiceFichero).getListaNombreObjetosRepetidosEnXML();
							
					// sacamos solo los elementos repetidos por tipo
					boolean existeRepetido = false;
					boolean primerRepetido = true;
					for (int aa=0; aa<listaDatosFicheroXML.get(indiceFichero).getNumeroObjetosS3e();aa++) {					
						if (listaDatosFicheroXML.get(indiceFichero).getListaNombreObjetosRepetidosEnXML().get(aa).size() > 0) {
							if (primerRepetido) {
								//System.out.println("ERROR: Existen nombres repetidos en este fichero");
								texto="ERROR: Existen nombres repetidos en este fichero";
								System.out.println(texto);
								escribeResultados.escribe(texto+"\n", ficheroSalida);	
								primerRepetido = false;
							}
							//System.out.print(listaDatosFicheroXML.get(indiceFichero).getListaNombreObjetosS3e().get(aa) + ": " + 
								//	listaDatosFicheroXML.get(indiceFichero).getListaNombreObjetosRepetidosEnXML().get(aa) + "\t" );
							texto=listaDatosFicheroXML.get(indiceFichero).getListaNombreObjetosS3e().get(aa) + ": " + 
									listaDatosFicheroXML.get(indiceFichero).getListaNombreObjetosRepetidosEnXML().get(aa) + "\t";
							System.out.print(texto);
							escribeResultados.escribe(texto+"\n", ficheroSalida);
							existeRepetido = true;
						}					
					}
					if (!existeRepetido) {
						//System.out.println("INFO: No existen nombres repetidos en este fichero"); 
						texto="INFO: No existen nombres repetidos en este fichero";
						System.out.println(texto);
						escribeResultados.escribe(texto+"\n", ficheroSalida);	
					}
					
					//System.out.println("");
					texto="";
					System.out.println(texto);
					escribeResultados.escribe(texto+"\n", ficheroSalida);	    	    	
	    	    }
				
								
				
				
				esS3e = false;
			}
									
		} catch (Exception e) {
			System.out.println("main: " + e);
			e.printStackTrace();
		}

	}
	

}
