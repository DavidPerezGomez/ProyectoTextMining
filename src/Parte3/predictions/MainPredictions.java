package Parte3.predictions;

import utils.Utils;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

public class MainPredictions {

    public static void main(String[] args) {
        String classifierPath = null;
        String instancesPath = null;
        String resultPath = null;
        try {
			classifierPath = args[0];
			instancesPath = args[1];
			resultPath = args[2];
		} catch (IndexOutOfBoundsException e) {
			String documentacion = "Este ejecutable se utiliza para clasificar un set de instancias con el clasificador deseado.\n"
                    + "Dos argumentos esperados:\n"
                    + "\t1 - Ruta del modelo (clasificador) a cargar\n"
                    + "\t2 - Ruta del archivo .arff con las instancias a clasificar\n"
                    + "\t3 - Ruta donde guardar los resultados de la clasficación\n"
                    + "\nEjemplo: java -jar predict.jar /path/to/model /path/to/input/arff path/to/results";
			System.out.println(documentacion);
			System.exit(1);
		}
        Classifier classifier = (Classifier) Utils.loadModel(classifierPath);
        Instances instances = Utils.loadInstances(instancesPath);
        String result = classifyInstances(classifier, instances);
        Utils.writeToFile(result, resultPath);
    }

    private static String classifyInstances(Classifier pClassifier, Instances pInstances) {
        StringBuilder result = new StringBuilder();
        result.append(String.format("Índice de la instancia -> Clase determinada por el clasificador\n"));
        result.append(String.format("----------------------------------------------------------------\n\n"));
        for (int i = 0; i < pInstances.numInstances(); i++) {
            try {
                Instance instance = pInstances.instance(i);
                double c = pClassifier.classifyInstance(instance);
                result.append(String.format("%d -> %s\n", i, pInstances.classAttribute().value((int) c)));
            } catch (Exception e) {
                result.append(String.format("%d -> error al clasificar\n", i));
                e.printStackTrace();
            }
        }
        return result.toString();
    }

}
