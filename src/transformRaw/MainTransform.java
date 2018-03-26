package transformRaw;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class MainTransform {

    private static final String BOW = "BoW";
    private static final String TFIDF = "TF-IDF";

    public static void main(String[] args) {
        String inputPath = null;
        String outputPath = null;
        String format = null;
        boolean sparse = false;
        try {
            inputPath = args[0];
            outputPath = args[1];
            format = args[2];
            if (format.toLowerCase().equals(BOW.toLowerCase()))
                format = BOW;
            else if (format.toLowerCase().equals(TFIDF.toLowerCase()))
                format = TFIDF;
            else
                throw new IllegalArgumentException();
            sparse = Boolean.parseBoolean(args[3]);
        } catch (IndexOutOfBoundsException e) {
            utils.Utils.printlnWarning("Cuarto argumetos esperados:\n" +
                                               "\t1 - Ruta del archivo .arff a leer\n" +
                                               "\t2 - Ruta del archivo .arff a crear\n" +
                                               "\t3 - Formato del nuevo archivo (" + BOW + " o " + TFIDF + ")\n" +
                                               "\t4 - Sparse o no sparse (true o false)");
            System.exit(1);
        } catch (IllegalArgumentException e) {
            utils.Utils.printlnWarning(String.format("Argumento %s incorrecto. Formato esperado %s o %s", format, BOW, TFIDF));
            System.exit(1);
        }

        foo(inputPath, outputPath, format, sparse);
    }

    private static void foo(String pInputPath, String pOutputPath, String pFormat, boolean pSparse) {
        Instances instances = utils.Utils.loadInstances(pInputPath, 0);
        if (instances != null) {
            try {
                instances.setClassIndex(0);
                StringToWordVector toWordVectorFilter = new StringToWordVector(20000);
                toWordVectorFilter.setLowerCaseTokens(true);
                toWordVectorFilter.setOutputWordCounts(true);
                toWordVectorFilter.setIDFTransform(pFormat.equals(TFIDF));
                toWordVectorFilter.setTFTransform(pFormat.equals(TFIDF));
                toWordVectorFilter.setInputFormat(instances);
                instances = Filter.useFilter(instances, toWordVectorFilter);
                // las instancias vienen por defecto en formato disperso
                if (!pSparse) {
                    instances = utils.Utils.nonSparseFilter(instances);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
            utils.Utils.saveInstances(instances, pOutputPath);
            System.out.println(String.format("Conversión completa. Nuevo archivo: %s", pOutputPath));
        }
    }

}
