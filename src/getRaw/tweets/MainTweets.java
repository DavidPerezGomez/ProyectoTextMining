package getRaw.tweets;

import utils.Utils;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NominalToString;
import weka.filters.unsupervised.attribute.Remove;

import javax.rmi.CORBA.Util;
import java.io.File;

public class MainTweets {

    public static void main(String[] args) {

        String inputPath = null;
        String outputPath = null;
        try {
            inputPath = args[0];
            outputPath = args[1];
        } catch (IndexOutOfBoundsException e) {
            Utils.printlnWarning("Dos argumetos esperados:\n" +
                                         "\t1 - Ruta del archivo raw a leer\n" +
                                         "\t2 - Ruta del archivo .arff a crear");
            System.exit(1);
        }

        tweetCSVToArff(inputPath, outputPath);

    }

    private static void tweetCSVToArff(String pInputPath, String pOutputPath) {
        if (!csvToArff(pInputPath, pOutputPath)){
            // si la conversión no funciona, probamos a limpiar el csv
            // y lo intentamos de nuevo
            String tmpCSV = "./tmp_clean_csv.csv";
            CSVManager.cleanCSV(pInputPath, tmpCSV);
            csvToArff(tmpCSV, pOutputPath);
            new File(tmpCSV).delete();
        }
    }

    private static boolean csvToArff(String pInputPath, String pOutputPath) {
        Instances instances = CSVManager.loadCSV(pInputPath);
        if (instances != null) {
            // esta parte está escrita específicamente para nuestro caso
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

                // ponemos el nombre de la relación
                instances.setRelationName("tweetSentiment");
            } catch (Exception e) {
                e.printStackTrace();
            }

            Utils.saveInstances(instances, pOutputPath);
            return true;
        } else {
            return false;
        }
    }

}
