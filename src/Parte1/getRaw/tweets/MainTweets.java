package Parte1.getRaw.tweets;

import weka.core.Instances;
import weka.core.Utils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NominalToString;
import weka.filters.unsupervised.attribute.Remove;
import java.io.File;

public class MainTweets {

    public static void main(String[] args) {
        String inputPath = null;
        String outputPath = null;
        try {
            inputPath = args[0];
            outputPath = args[1];
        } catch (IndexOutOfBoundsException e) {
            String documentacion = "Este ejecutable convierte los sets de train y dev de registros de tweets archivos .arff crudos.\n" +
                                    "El archivo a convertir debe estar en formato .csv y su primera línea debe ser la cabecera de los valores.\n" +
                                    "Dos argumetos esperados:\n" +
                                         "\t1 - Ruta del archivo raw a leer\n" +
                                         "\t2 - Ruta del archivo .arff a crear\n" +
                                    "\nEjemplo: java -jar getRawTweets.jar /path/to/tweets/csv /path/to/output/arff";
            System.out.println(documentacion);
            System.exit(1);
        }
        csvToArff(inputPath, outputPath);
    }

    /**
     * Transforma un archivo .csv a formato .arff
     *
     * @param pInputPath ruta del arvhico .csv que se quiere transformar
     * @param pOutputPath ruta del archivo .arff que se quiere generar
     */
    private static void csvToArff(String pInputPath, String pOutputPath) {
        if (!tweetCSVToArff(pInputPath, pOutputPath, true)){
            // si la conversión no funciona, probamos a limpiar el csv
            // y lo intentamos de nuevo
            String tmpCSV = "./tmp_clean_csv.csv";
            System.out.println("Procediendo a limpeza del archivo .csv");
            CSVManager.cleanCSV(pInputPath, tmpCSV);
            tweetCSVToArff(tmpCSV, pOutputPath, true);
            //eliminamos el archivo temporal
            new File(tmpCSV).delete();
        }
    }

    /**
     * Convierte el archivo .csv de resgistros de tweets a formato .csv
     *
     * @param pInputPath ruta del arvhico .csv que se quiere transformar
     * @param pOutputPath ruta del archivo .arff que se quiere generar
     * @param pVerbose si true, se imprime más información por consola
     * @return
     */
    private static boolean tweetCSVToArff(String pInputPath, String pOutputPath, boolean pVerbose) {
        if(pVerbose)
            System.out.println(String.format("Procediendo a la conversión de %s a .arff", pInputPath));
        Instances instances = CSVManager.loadCSV(pInputPath);
        if (instances != null) {
            // ponemos el segundo atributo (Sentiment) como clase
            instances.setClassIndex(1);
            try {
                // eliminamos los atributos que sobran (Topic, TweetId y TweetDate)
                Remove rmFilter = new Remove();
                rmFilter.setAttributeIndicesArray(new int[]{0, 2, 3});
                rmFilter.setInputFormat(instances);
                instances = Filter.useFilter(instances, rmFilter);

                // convertimos el atributo TweetText a tipo String
                NominalToString toStringFilter = new NominalToString();
                toStringFilter.setAttributeIndexes("last");
                toStringFilter.setInputFormat(instances);
                instances = Filter.useFilter(instances, toStringFilter);

                // ponemos el nombre de la relación y los atributos
                instances.renameAttribute(instances.classIndex(), "@@class@@");
                instances.renameAttribute(1, "text");
                instances = utils.Utils.moveFirstAttrToLast(instances);
                instances.setRelationName("tweetSentiment");
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }

            utils.Utils.saveInstances(instances, pOutputPath);
            if(pVerbose)
                System.out.println(String.format("Conversión completa. Nuevo archivo: %s", pOutputPath));
            return true;
        } else {
            if(pVerbose)
                utils.Utils.printlnError("Error en la conversión");
            return false;
        }
    }

}
