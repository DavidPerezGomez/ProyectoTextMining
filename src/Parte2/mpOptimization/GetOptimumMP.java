package Parte2.mpOptimization;

import utils.Utils;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;

import java.util.Random;

public class GetOptimumMP {

    public static void main(String[] args){
        String inputPath = null;
        try {
            inputPath = args[0];
        } catch (IndexOutOfBoundsException e) {
            String documentacion = "Este ejecutable devuelve los valores óptimos de los parámetros Hidden Layer y " +
                                   "Validation Threshold del clasificador Multilayer Perceptron para el set de instancias dado.\n" +
                                    "Un argumeto esperado:\n" +
                                         "\t1 - Ruta del archivo arff con las instancias a evaluar. La clase debe ser el último atributo.\n" +
                                    "\nEjemplo: java -jar GetOptimumMP.jar /path/to/arff/file";
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

        // atributo 2: learningRate
        double minLearningRate = 0;
        double maxLearningRate = 1;
        double bestLearningRate = -1;
        double learningRateInc = 0.1;


        int minClassIndex = Utils.getMinorityClassIndex(pInstances);
        double bestFMeasure = -1;
        int it = 1;

        for(String hiddenLayers : hiddenLayersOptions) {
            for (double learningRate = minLearningRate; learningRate <= maxLearningRate; learningRate+=learningRateInc) {
                try {
                    classifier.setLearningRate(learningRate);
                    classifier.setHiddenLayers(hiddenLayers);
                    Evaluation evaluation = new Evaluation(pInstances);
                    evaluation.crossValidateModel(classifier, pInstances, 4, new Random(1));
                    double fMeasure = evaluation.fMeasure(minClassIndex);
                    System.out.println("iteration " + it);
                    System.out.println("learningRate " + learningRate);
                    System.out.println("hiddenLayers " + hiddenLayers);
                    System.out.print("fMeasure " + fMeasure);
                    if (fMeasure > bestFMeasure) {
                    System.out.print(" --> BEST!!!");
                        bestFMeasure = fMeasure;
                        bestLearningRate = learningRate;
                        bestHiddenLayers = hiddenLayers;
                    }
                    System.out.println("\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                it++;
            }
        }

        System.out.println("Parámetros óptimos del clasificador Multilayer Perceptron para las instancias dadas:");
        System.out.println(String.format("\thidden layers: %s", bestHiddenLayers));
        System.out.println(String.format("\tvalidation threshold: %f", bestLearningRate));

    }

}
