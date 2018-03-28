package makeCompatible;

import java.io.File;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.FixedDictionaryStringToWordVector;

public class MainMakeCompatible {

    public static void main(String[] args) throws IOException {
        String inputTrain = null;
        String inputDev = null;
        String inputDicc = null;
        String outputDev = null;

        try {
            inputTrain = args[0];
            inputDev = args[1];
            inputDicc = args[2];
            outputDev = args[3];
        } catch (IndexOutOfBoundsException e) {
            utils.Utils.printlnWarning("Cuarto argumetos esperados:\n" +
                                               "\t1 - Ruta del archivo trainBow.arff a leer\n" +
                                               "\t2 - Ruta del archivo dev.arff a leer\n" +
                                               "\t3 - Ruta del diccionario de train.arff" +
                                               "\t4 - Ruta del archivo devBow.arff generado");
            System.exit(1);
        }

        makeCompatible(inputTrain, inputDev, inputDicc, outputDev);
    }

    private static void makeCompatible(String inputTrain, String inputDev, String pInputDicc, String outputDev) throws IOException {
        Instances trainBow = utils.Utils.loadInstances(inputTrain, 0);
        Instances dev = utils.Utils.loadInstances(inputDev, 0);
        Instances devBow = null;
        //preparamos el filtro con el diccionario de train y lo aplicamos a dev
        FixedDictionaryStringToWordVector filtro = new FixedDictionaryStringToWordVector();
        filtro.setDictionaryFile(new File(pInputDicc));
        try {
            //no se si habria que meterle el formato de dev tal vez
            filtro.setInputFormat(dev);
            devBow = Filter.useFilter(dev, filtro);
            devBow.setRelationName(dev.relationName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        File fichero = new File(outputDev);

        //creamos el arffsaver el cual, guarda en el destino en formato arff
        ArffSaver saver = new ArffSaver();
        saver.setInstances(devBow);
        saver.setFile(fichero);
        saver.writeBatch();
    }

}