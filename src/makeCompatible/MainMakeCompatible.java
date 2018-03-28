package makeCompatible;

import java.io.File;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.FixedDictionaryStringToWordVector;

public class MainMakeCompatible {

    public static void main(String[] args) throws IOException {
        String inputDev = null;
        String inputDicc = null;
        String outputDev = null;

        try {
            inputDev = args[0];
            inputDicc = args[1];
            outputDev = args[2];
        } catch (IndexOutOfBoundsException e) {
            utils.Utils.printlnWarning("Cuarto argumetos esperados:\n" +
                                               "\t1 - Ruta del archivo dev.arff a leer\n" +
                                               "\t2 - Ruta del diccionario de train.arff" +
                                               "\t3 - Ruta del archivo devBow.arff generado");
            System.exit(1);
        }

        makeCompatible(inputDev, inputDicc, outputDev);
    }

    private static void makeCompatible(String inputDev, String pInputDicc, String outputDev) throws IOException {
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