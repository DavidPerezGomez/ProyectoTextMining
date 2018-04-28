package Parte2.multilayerPerceptron;

import com.sun.org.apache.xpath.internal.operations.Mult;
import utils.Utils;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;

public class GetMP {

    public static void main(String[] args) {
        String inputPath = null;
        String hiddenLayers = null;
        double learningRate = -1;
        String modelPath = null;
        String outputPath = null;
        try {
            inputPath = args[0];
            hiddenLayers = args[1];
            learningRate = Double.parseDouble(args[2]);
            modelPath = args[3];
            outputPath = args[4];
        } catch (IndexOutOfBoundsException e) {
            String documentacion = "Este ejecutable crea y guarda un clasificador Multilayer Perceptron con los parámetros" +
                                   "hiddenLayers y learningRate indicados. Además realiza una evaluación no honesta y una 10-fold cross-validations" +
                                   "y guarda los resultados y la calidad estimada. \n" +
                                    "Cinco argumetos esperados:\n" +
                                         "\t1 - Ruta del archivo arff con las instancias de entrenamiento. La clase debe ser el último atributo.\n" +
                                         "\t2 - Valor del parámetro hiddenLayers.\n" +
                                         "\t3 - Valor del parámetro learningRate.\n" +
                                         "\t4 - Ruta donde guardar el modelo.\n" +
                                         "\t5 - Ruta donde el archivo de texto con los resultados de la evaluación.\n" +
                                    "\nEjemplo: java -jar GetMP.jar /path/to/arff/file a 0.5 /path/to/model /path/to/results/file";
            System.out.println(documentacion);
            System.exit(1);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.exit(1);
        }

        Instances instances = Utils.loadInstances(inputPath);
        MultilayerPerceptron classifier = buildMP(instances, hiddenLayers, learningRate);
        Utils.saveModel(classifier, modelPath);
        saveResults(classifier, instances, outputPath);
    }

    /**
     * Devuelve un clasificador MultilayerPerceptron con los valores dados para los parámetros hiddenLayers y
     * learningRate y que ha sido entrenado con las instancias dadas.
     * @param pInstances
     * @param pHidenLayers
     * @param pLearningRate
     * @return
     */
    private static MultilayerPerceptron buildMP(Instances pInstances, String pHidenLayers, double pLearningRate) {
        MultilayerPerceptron classifier = new MultilayerPerceptron();
        classifier.setHiddenLayers(pHidenLayers);
        classifier.setLearningRate(pLearningRate);
        try {
            classifier.buildClassifier(pInstances);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return classifier;
    }

    /**
     * Guarda los resultados de la estminación de la calidad mediante evaluación no honesta y 10-fold cross-validation
     * del clasificador y lsa instancias dados.
     * @param pClassifier
     * @param pInstances
     * @param pPath
     */
    private static void saveResults(MultilayerPerceptron pClassifier, Instances pInstances, String pPath) {
        try {
            Evaluation eval10Fold = Utils.evalKFoldCrossValidation(pClassifier, pInstances, 10, 1);

            pClassifier.buildClassifier(pInstances);
            Evaluation evalNH = new Evaluation(pInstances);
            evalNH.evaluateModel(pClassifier, pInstances);

            StringBuilder results = new StringBuilder();
            results.append("10-FOLD CROSS VALIDATION\n");
            results.append(eval10Fold.toSummaryString() + "\n");
            results.append(eval10Fold.toClassDetailsString() + "\n");
            results.append(eval10Fold.toMatrixString() + "\n");
            results.append("\nEVAL NO HONESTA\n");
            results.append(evalNH.toSummaryString() + "\n");
            results.append(evalNH.toClassDetailsString() + "\n");
            results.append(evalNH.toMatrixString() + "\n");
            Utils.writeToFile(results.toString(), pPath);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

}
