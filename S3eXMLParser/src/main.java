
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


//015

public class main {
	static DatosFicheroXML fichero1 = new DatosFicheroXML(); 
	static List<DatosFicheroXML> listaDatosFicheroXML = new ArrayList<DatosFicheroXML>(); //una lista de objetos. un objeto por cada fichero xml
	
	// indicamos la ruta donde vamos a buscar los xml
	static File ruta = new File("C:\\temp\\");
	//static File ruta = new File("C:\\CAFs_SBS\\ENCE\\ramas\\ENCE_DESARROLLO\\SIST\\Validación\\Entorno\\ContextosPrueba\\");
	
	static File ficheroSalida = new File(ruta.getAbsolutePath() + "\\analisisXML.txt");
	static int indiceFichero=0;
	static String texto="";	

	public static void main(String argv[])   {		
		if (ficheroSalida.exists()) {
			ficheroSalida.delete();
		}
		
		// se crea una lista de ficheros xml en la ruta indicada (todos los ficheros, no se descarta ninguno)
		List<File> listaFicherosXML = new ArrayList<File>();			
		listaFicherosXML = Archivos.listarArchivosRecursivamente(ruta);						
				
		// se recorre todos los archivos y crea un objeto tipo DatosFicheroXML por fichero valido	
		int nFicheroValidado = 0;
		for (int i = 0; i < listaFicherosXML.size(); i++) {
		    File ficheroXMLaValidar;
		    ficheroXMLaValidar = listaFicherosXML.get(i);
		    boolean esS3e = Archivos.ficheroXMLValido(ficheroXMLaValidar);
		   		
			if (esS3e) {
				nFicheroValidado++;
				listaDatosFicheroXML.add(new DatosFicheroXML());
				listaDatosFicheroXML.get(nFicheroValidado-1).setFichero(listaFicherosXML.get(i));
				texto=nFicheroValidado+ "\t\t\t "+listaDatosFicheroXML.get(nFicheroValidado-1).getFichero();
				System.out.println(texto);
				escribeResultados.escribe(texto+"\n", ficheroSalida);
			}
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

					// buscamos el startElement en nuestra lista de objetos a evaluar
					for (int startE=0; startE<listaDatosFicheroXML.get(indiceFichero).getNumeroObjetosS3e();startE ++) {
						// si la cabecera leida del xml esta en mi lista de obj s3e
						if (qName.equalsIgnoreCase(listaDatosFicheroXML.get(indiceFichero).getListaNombreObjetosS3e().get(startE))) { 
							listaDatosFicheroXML.get(indiceFichero).addNombreObjetoEnXML(attributes.getValue("Nombre"), startE);							
						}
					}
				}
			};
			
			System.out.println("\n---");
			
			for (indiceFichero=0;indiceFichero<listaDatosFicheroXML.size();indiceFichero++){
			    File ficheroXMLaExaminar;
			    ficheroXMLaExaminar = listaDatosFicheroXML.get(indiceFichero).getFichero();			    
			    //esS3e = Archivos.ficheroXMLValido(ficheroXMLaExaminar);
	    	    if (true) {
					texto=indiceFichero+ "\t\t\t "+listaDatosFicheroXML.get(indiceFichero).getFichero();
					System.out.println(texto);
					escribeResultados.escribe(texto+"\n", ficheroSalida);
	    	    	saxParser.parse(ficheroXMLaExaminar, handler);
	    	    	// llamamos a la funcion para que cuente los obejtos del S3e del fichero 0
					listaDatosFicheroXML.get(indiceFichero).cuentaObjetosS3e();					
					
					// sacamos lista de numero de cada objeto del S3e. (60 MCS, 100 ED, etc)
					for (int aa=0; aa<listaDatosFicheroXML.get(indiceFichero).getNumeroObjetosS3e();aa++) {						
						texto=""+listaDatosFicheroXML.get(indiceFichero).getListaNombreObjetosS3e().get(aa) + ": " + listaDatosFicheroXML.get(indiceFichero).getListaNumeroObjetosS3e().get(aa) + "\t";
						System.out.print(texto);
						escribeResultados.escribe(texto, ficheroSalida);	
					}

					System.out.println("");
					escribeResultados.escribe("\n", ficheroSalida);	
					
					// llamamos a la funcion para que cuente los obejtos del S3e del fichero 
					listaDatosFicheroXML.get(indiceFichero).calculaObjetosRepetidosS3e();
							
					// sacamos solo los elementos repetidos por tipo					
					texto="************* " + listaDatosFicheroXML.get(indiceFichero).imprimeListaNombreObjetosRepetidosEnXML();
					System.out.println(texto);
					escribeResultados.escribe(texto+"\n", ficheroSalida);
					
	    	    }
			}
									
		} catch (Exception e) {
			System.out.println("main: " + e);
			e.printStackTrace();
		}

	}
	

}
