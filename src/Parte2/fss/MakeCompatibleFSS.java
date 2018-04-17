package Parte2.fss;

import java.io.File;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.FixedDictionaryStringToWordVector;
import weka.filters.unsupervised.attribute.Standardize;

public class MakeCompatibleFSS {

    public static void main(String[] args) throws IOException {
        String inputDev = null;
        String inputTrain = null;
        String outputDev = null;
        try {
            inputDev = args[0];
            inputTrain = args[1];
            outputDev = args[2];
        } catch (IndexOutOfBoundsException e) {
			 String documentacion = "Este ejecutable adapta el espacio de atributos de evaluacion de modo que sea compatible con el conjunto de entrenamiento\n" +
                                    "El archivo .arff original debe tener por lo menos un atributo de tipo string.\n" +
                                    "Tres argumentos esperados:\n" +
                                        "\t1 - Ruta del archivo .arff a de evaluación\n" +
                                        "\t2 - Ruta del archivo .arff a de entrenamiento\n" +
                                        "\t3 - Ruta del archivo .arff a generar\n" +
                                    "\nEjemplo: java -jar makeCompatibleFSS.jar /path/to/dev/arff /path/to/train/arff /path/to/output/arff";
            System.out.println(documentacion);
            System.exit(1);
         
        }
        try {
			makeCompatible(inputDev, inputTrain, outputDev);
		} catch (Exception e) {
			
			System.out.println("No se ha podido completar el proceso ");
			e.printStackTrace();
		}
    }

    /**
     * Utiliza un diccionario para convertir los atributos string de un archivo .arff a word-vector
     *
     * @param inputDev ruta del .arff que se quiere transformar
     * @param outputDev ruta del .arff que se quiere generar
     * @throws IOException
     */
    private static void makeCompatible(String inputDev,String inputTrain, String outputDev) throws Exception {
    	Instances train = utils.Utils.loadInstances(inputTrain);
    	Instances dev=utils.Utils.loadInstances(inputDev);
    	
    	Standardize filter = new Standardize();
    	filter.setInputFormat(train);
    	
    	Instances newTrain = Filter.useFilter(train, filter);
    	Instances devFiltrado = Filter.useFilter(dev, filter);
    	ArffSaver saver = new ArffSaver();
        saver.setInstances(devFiltrado);
        saver.setFile(new File(outputDev));
        saver.writeBatch();
    }

}