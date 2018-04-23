package Parte2.baseline;

import utils.Utils;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

import javax.rmi.CORBA.Util;

public class GetBaselineModel {

	public static void main(String[] args) {
		String trainPath = null;
		String modelPath = null;
		String resultPath = null;
		try {
			trainPath = args[0];
			modelPath = args[1];
			resultPath = args[2];
		} catch (IndexOutOfBoundsException e) {
			String documentacion = "Este ejecutable se utiliza para obtener el modelo Baseline (Naive Bayes) y su calidad estimada.\n"
                    + "Dos argumentos esperados:\n"
                    + "\t1 - Ruta del archivo .arff con las instancias de entrenamiento (la clase debe ser el primer atributo)\n"
                    + "\t2 - Ruta donde guardar el modelo\n"
                    + "\t3 - Ruta donde guardar la estimación de la calidad\n"
                    + "\nEjemplo: java -jar getBaseline.jar /path/to/input/arff /path/to/output";
			System.out.println(documentacion);
			System.exit(1);
		}

        Instances train = Utils.loadInstances(trainPath, 0);
		NaiveBayes classifier = getNaiveBayes(train);
		Utils.saveModel(classifier, modelPath);
		String results = evaluateClassifier(classifier, train);
		Utils.writeToFile(results, resultPath);
	}

    /**
     * Entrena un y devuelve un clasificador Naive Bayes con la instancias dadas.
     * @param pTrain
     * @return
     */
	private static NaiveBayes getNaiveBayes(Instances pTrain) {
	    NaiveBayes classifier = new NaiveBayes();
	    try {
            classifier.buildClassifier(pTrain);
        } catch (Exception e) {
            Utils.printlnError("Error al entrenar el calsificador.");
            System.exit(1);
        }
	    return classifier;
    }

    /**
     * Dado un clasificador entrenado y unas instancias con el formato apropiado, realiza una serie de
     * evaluaciones sobre el clasificador y devuelve una string con los resultados.
     * @param pClassifier
     * @param pInstances
     * @return
     */
    private static String evaluateClassifier(Classifier pClassifier, Instances pInstances) {
	    StringBuilder results = new StringBuilder();

        try {
            Evaluation evNoHonesta = new Evaluation(pInstances);
            evNoHonesta.evaluateModel(pClassifier, pInstances);
            results.append("EVALUACIÓN NO HONESTA\n");
            results.append("\n");
            results.append(evNoHonesta.toClassDetailsString());
            results.append("\n");
            results.append(evNoHonesta.toMatrixString());
        } catch (Exception e) {
            e.printStackTrace();
        }

         try {
            Evaluation evKFCV = Utils.evalKFoldCrossValidation(pClassifier, pInstances, 10, 1);
            results.append("\nEVALUACIÓN 10-FOLD CROSS-VALIDATION\n");
            results.append("\n");
            results.append(evKFCV.toClassDetailsString());
            results.append("\n");
            results.append(evKFCV.toMatrixString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results.toString();
    }
}
