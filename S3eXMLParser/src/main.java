
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
//              024.04 * Prueba sin cambios
//025 		* Se simplifica main , creando MainFuncionesAcciones y MainFunciones
//026 		* Crea opcion de comentar en el xml los elmentos del s3e no utilizados
//				026.01 * Se renombra el fichero xml original con sufijo "_bck" y se guarda el fichero reducido 
//						 con el nombre original
//027 		* Se mejora la busqueda de elementos no usados teniendo en cuenta ""

public class main {
	
	public static void main(String argv[])   {		
		String texto="";
		
		Scanner entradaEscaner = new Scanner (System.in);
		boolean trataXML = false;
		boolean calculaNombreObjetosRepetidosEnXML = false;
		boolean calculaIdentificadorObjetos_noUsadosEnXML = false;
		boolean calculaNombreObjetosRepetidosEnXML_usados_en_Scripts = false;
		boolean calcula_MCS_en_scripts = false;
		boolean estimaMCS = false;
		boolean borraObjetos_noUsadosEnXML = false;
		
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

		// se pregunta por linea de comando las acciones______________________________________________
		trataXML = MainFuncionesAcciones.pregunta_trataXML(ficheroSalida, nivelTraza, entradaEscaner);		
		if (trataXML) {
			//trataXML = MainFuncionesAcciones.pregunta_trataXML(ficheroSalida, nivelTraza);
			calculaNombreObjetosRepetidosEnXML 
				= MainFuncionesAcciones.pregunta_calculaNombreObjetosRepetidosEnXML(ficheroSalida, nivelTraza, entradaEscaner);
			calculaIdentificadorObjetos_noUsadosEnXML 
				= MainFuncionesAcciones.pregunta_calculaIdentificadorObjetos_noUsadosEnXML(ficheroSalida, nivelTraza, entradaEscaner);
			borraObjetos_noUsadosEnXML 
				= MainFuncionesAcciones.pregunta_borraObjetos_noUsadosEnXML(ficheroSalida, nivelTraza, entradaEscaner, calculaIdentificadorObjetos_noUsadosEnXML);
			calculaNombreObjetosRepetidosEnXML_usados_en_Scripts 
				= MainFuncionesAcciones.pregunta_calculaNombreObjetosRepetidosEnXML_usados_en_Scripts(ficheroSalida, nivelTraza, entradaEscaner, calculaNombreObjetosRepetidosEnXML);
			estimaMCS  
				= MainFuncionesAcciones.pregunta_EstimaMCS(ficheroSalida, nivelTraza, entradaEscaner);
		
			
			
			
		}
		calcula_MCS_en_scripts = MainFuncionesAcciones.pregunta_calcula_MCS_en_scripts(ficheroSalida, nivelTraza, entradaEscaner);
		// Fin de las preguntas por linea de comandos______________________________________________
		
		
        Timestamp timestamp_inicial = new Timestamp(System.currentTimeMillis());
		
        // En funcion de las preguntas, se lanzan las acciones _______________________________________        
		// para cada script
        
//		if (true) {
//			MainFunciones.borra_noUsados_enPruebas(rutaBase, nivelTraza, ruta);
//			trataXML = false;
//		}       
        
        
        
		if (calcula_MCS_en_scripts) {
			MainFunciones.cuentaMCS_enScripts(rutaBase, nivelTraza, ficheroSalida, ruta);
		}
		
		if (trataXML) {
			List<File> listaFicherosXML_validos = new ArrayList<File>();
			
			//obtenemos lista de XML validos
			listaFicherosXML_validos = MainFunciones.calculaListaFicherosXML_validos(rutaBase, nivelTraza, ficheroSalida, ruta);
			
			//hacemos nuestras cosas
			MainFunciones.tratar_cada_XML(nivelTraza, ficheroSalida, ficheroSalida_numMCS, ruta, listaFicherosXML_validos, calculaNombreObjetosRepetidosEnXML, calculaIdentificadorObjetos_noUsadosEnXML, borraObjetos_noUsadosEnXML, calculaNombreObjetosRepetidosEnXML_usados_en_Scripts, estimaMCS);
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
