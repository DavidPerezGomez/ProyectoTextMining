package Parte2.fss;


import utils.Utils;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

import java.io.File;

public class FSSInfoGain {

    public static void main(String[] args) {
        String inputArff = null;
        String outputArff = null;
        String inputDev, outputDev;
        try {
        	inputArff = args[0];
        	outputArff = args[1];
        } catch (IndexOutOfBoundsException e) {
			 String documentacion = "Este ejecutable se utiliza para descartar atributos redudantes o irrelevantes para el proceso de clasificacion\n" +
                                            "El archivo .arff original debe tener por lo menos un atributo de tipo string.\n" +
                                            "Dos argumentos esperados:\n" +
                                            "\t1 - Ruta del archivo .arff a leer (la clase debe ser el primer atributo)\n" +
                                            "\t2 - Ruta del archivo .arff a crear\n"+
                                            "Argumentos opcionales:\n"+
                                            "\t3 - Ruta del archivo .arff que se quiere hacer compatible con el .arff filtrado\n"+
                                            "\t4 - Ruta del archivo .arff compatibilizado a crear\n"+
                                            "\nEjemplo1: java -jar FSS.jar /path/to/input/arff /path/to/output/arff"+
                                            "\nEjemplo2: java -jar FSS.jar /path/to/input/arff /path/to/output/arff /path/to/other/input/arff /path/to/other/output/arff";
            System.out.println(documentacion);
            System.exit(1);
        }

        try {
            inputDev = args[2];
            outputDev = args[3];
        } catch (IndexOutOfBoundsException e) {
            inputDev = null;
            outputDev = null;
        }
        //cargamos el fichero
        Instances instances = utils.Utils.loadInstances(inputArff);
        try {
            AttributeSelection filter = getASFilter(instances);
			Instances filteredInstances = Filter.useFilter(instances, filter);
			filteredInstances.setRelationName(instances.relationName());
            Utils.saveInstances(filteredInstances, outputArff);
            if (inputDev != null) {
                Instances dev = utils.Utils.loadInstances(inputDev);
                Instances filteredDev = Filter.useFilter(dev, filter);
                Utils.saveInstances(filteredDev, outputDev);
            }
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
    private static AttributeSelection getASFilter(Instances pDatos) throws Exception {
		AttributeSelection filter = new AttributeSelection();
		Ranker ranker = new Ranker();
		boolean salir = false;
		double threshold = -0.001;
		double increment = 0.0005;
		int numAtributos = pDatos.numAttributes();
		Instances filtrado;
		Instances inputFormat = new Instances(pDatos, 0, 1);

		while(!salir) {
            filter.setInputFormat(inputFormat);
            filter.setEvaluator(new InfoGainAttributeEval());
			ranker.setThreshold(threshold);
            filter.setSearch(ranker);
			filtrado = Filter.useFilter(pDatos, filter);
			if(numAtributos != filtrado.numAttributes()) {
				salir = true;
			} else {
                threshold += increment;
            }
		}
		return filter;
    }
   
}
