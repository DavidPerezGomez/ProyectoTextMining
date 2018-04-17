package Parte2.fss;


import java.io.File;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

public class FSSInfoGain {
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
        Instances datos=utils.Utils.loadInstances(trainBow,0); 
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

    /**
     * Filtra los datos quitando atributos, para ello recorrera el parametro Threshold para hallar el valor optimo,
     * teniendo en cuenta que parara la primera vez que disminuyan los atributos. 
     * @param pDatos
     */
    public static Instances useFilter(Instances pDatos) throws Exception {
		AttributeSelection as=new AttributeSelection();
		Ranker r=new Ranker();
		
		boolean salir=false;
		double i=-0.005; 
		int numAtributos=pDatos.numAttributes();
		Instances filtrado=null;
		while(!salir) {
			r.setThreshold(i);
			as.setEvaluator(new InfoGainAttributeEval());
			as.setSearch(r);
			as.setInputFormat(pDatos);
			filtrado=Filter.useFilter(pDatos, as);
			if(numAtributos!=filtrado.numAttributes()) {
				salir=true;
			}
			i=i+0.0005;
			System.out.println(i);
		}
		System.out.println("==========================================================");
		System.out.println("NUMTOSELECT: POR DEFECTO (-1)");
		System.out.println("THRESHOLD UTILIZADO: "+i);
    	System.out.println("==========================================================");
    	System.out.println("");
    	System.out.println("");
    	System.out.println("");
		return filtrado;	
    }
   
}
