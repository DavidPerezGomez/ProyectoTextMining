package Parte1.makeCompatible;

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
			 String documentacion = "Este ejecutable convierte un archivo .arff crudo a un .arff con atributos de tipo word-vector asegurándose de que es compatible con un diccionario de palabras dado.\n" +
                                    "El archivo .arff original debe tener por lo menos un atributo de tipo string.\n" +
                                    "Cuarto argumetos esperados:\n" +
                                        "\t1 - Ruta del archivo .arff a leer\n" +
                                        "\t2 - Ruta del diccionario\n" +
                                        "\t3 - Ruta del archivo .arff a generar\n" +
                                    "\nEjemplo: java -jar makeCompatible.jar /path/to/input/arff /path/to/dicc /path/to/output/arff";
            System.out.println(documentacion);
            System.exit(1);
        }
        makeCompatible(inputDev, inputDicc, outputDev);
    }

    /**
     * Utiliza un diccionario para convertir los atributos string de un archivo .arff a word-vector
     *
     * @param inputDev ruta del .arff que se quiere transformar
     * @param pInputDicc ruta del diccionario que se quiere utilizar
     * @param outputDev ruta del .arff que se quiere generar
     * @throws IOException
     */
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