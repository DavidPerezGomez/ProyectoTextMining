package Parte2.MPOptimization;

import utils.Utils;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;

import java.util.Random;

public class optimizeMP {

    public static void main(String[] args){
        String inputPath = null;
        try {
            inputPath = args[0];
        } catch (IndexOutOfBoundsException e) {
            String documentacion = "Este ejecutable devuelve los valores óptimos de los parámetros Hidden Layer y " +
                                   "Validation Threshold del clasificador Multilayer Perceptron para el set de instancias dado.\n" +
                                    "Un argumeto esperado:\n" +
                                         "\t1 - Ruta del archivo arff con las instancias a evaluar\n" +
                                    "\nEjemplo: java -jar optimizeMP.jar /path/to/arff/file";
            System.out.println(documentacion);
            System.exit(1);
        }
        Instances instances = Utils.loadInstances(inputPath);
        getOptimalParameters(instances);
    }

    private static void getOptimalParameters(Instances pInstances) {
        MultilayerPerceptron classifier = new MultilayerPerceptron();

        // atributo 1: hiddenLayers
        String[] hiddenLayersOptions = {"a", "i", "o", "t"};
        String bestHiddenLayers = "";

        // atributo 2: validationThreshold
        int minValidationThreshold = 0;
        int maxValidationThreshold = 50;
        int bestValThreshold = -1;


        int minClassIndex = Utils.getMinorityClassIndex(pInstances);
        double bestFMeasure = -1;

        for(String hiddenLayers : hiddenLayersOptions) {
            for (int valThreshold = minValidationThreshold; valThreshold <= maxValidationThreshold; valThreshold++) {
                try {
                    classifier.setValidationThreshold(valThreshold);
                    classifier.setHiddenLayers(hiddenLayers);
                    Evaluation evaluation = new Evaluation(pInstances);
                    evaluation.crossValidateModel(classifier, pInstances, 4, new Random(3));
                    double fMeasure = evaluation.fMeasure(minClassIndex);
                    if (fMeasure > bestFMeasure) {
                        bestFMeasure = fMeasure;
                        bestValThreshold = valThreshold;
                        bestHiddenLayers = hiddenLayers;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Parámetros óptimos del clasificador Multilayer Perceptron para las instancias dadas:");
        System.out.println(String.format("\thidden layers: %s", bestHiddenLayers));
        System.out.println(String.format("\tvalidation threshold: %d", bestValThreshold));

    }

}
