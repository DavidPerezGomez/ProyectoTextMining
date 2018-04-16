package Parte2.fss;


import java.io.File;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

public class FSSInfoGame {
    public static void main(String[] args) {
        String trainBow = null;
        String trainBowFSS=null;
        try {
        	trainBow = args[0];
        	trainBowFSS=args[1];
        	
        } catch (IndexOutOfBoundsException e) {
			 String documentacion = "Este ejecutable se utiliza para descartar atributos redudantes o irrelevantes para el proceso de clasificacion\n" +
                                    "El archivo .arff original debe tener por lo menos un atributo de tipo string.\n" +
                                    "Dos argumentos esperados:\n" +
                                    "\t1 - Ruta del archivo .arff a leer\n" +
                                    "\t2 - Ruta del archivo .arff a crear\n"+
                                    "\nEjemplo: java -jar FSS.jar /path/to/input/arff /path/to/output/arff";
            System.out.println(documentacion);
            System.exit(1);
        } catch (IllegalArgumentException e) {
            utils.Utils.printlnWarning(String.format("Argumento %s incorrecto."));
            System.exit(1);
        }
        //cargamos el fichero
        Instances datos=utils.Utils.loadInstances(trainBow); 
        
        try {
			Instances filtrado=useFilter(datos);
			ArffSaver saver = new ArffSaver();
	        saver.setInstances(filtrado);
	        saver.setFile(new File(trainBowFSS));
	        saver.writeBatch();
			
		} catch (Exception e) {
			System.out.println("No se ha podido completar el filtrado ");
			e.printStackTrace();
		}
        
        
    }
    public static Instances useFilter(Instances datos) throws Exception {
		AttributeSelection as=new AttributeSelection();
				
		as.setEvaluator(new InfoGainAttributeEval());
		as.setSearch(new Ranker());
		as.setInputFormat(datos);
		
		return Filter.useFilter(datos, as);
    	
    }
}
