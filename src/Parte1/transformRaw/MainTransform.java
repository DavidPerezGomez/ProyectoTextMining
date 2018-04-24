package Parte1.transformRaw;

import java.io.File;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class MainTransform {

    private static final String BOW = "BoW";
    private static final String TFIDF = "TF-IDF";

    public static void main(String[] args) {
        String inputPath = null;
        String outputPath = null;
        String diccPath=null;
        String format = null;
        boolean sparse = false;
        try {
            inputPath = args[0];
            outputPath = args[1];
            diccPath=args[2];
            format = args[3];
            sparse =Boolean.parseBoolean(args[4]);
            if (format.toLowerCase().equals(BOW.toLowerCase()))
                format = BOW;
            else if (format.toLowerCase().equals(TFIDF.toLowerCase()))
                format = TFIDF;
            else
                throw new IllegalArgumentException();
        } catch (IndexOutOfBoundsException e) {
			 String documentacion = "Este ejecutable convierte un archivo .arff crudo a un .arff con atributos de tipo string vector generando el un diccionario de palabras correspondiente.\n" +
                                    "El archivo .arff original debe tener por lo menos un atributo de tipo string.\n" +
                                    "Cuarto argumetos esperados:\n" +
                                        "\t1 - Ruta del archivo .arff a leer\n" +
                                        "\t2 - Ruta del archivo .arff a crear\n" +
                                        "\t3 - Ruta donde guardar el diccionario de palabras\n"+
                                        "\t4 - Formato del nuevo archivo (" + BOW + " o " + TFIDF + ")\n" +
                                        "\t5 - Sparse o no sparse (true o false)\n" +
                                    "\nEjemplo: java -jar transformRaw.jar /path/to/input/arff /path/to/output/arff /path/to/dicc BoW true";
            System.out.println(documentacion);
            System.exit(1);
        } catch (IllegalArgumentException e) {
            utils.Utils.printlnWarning(String.format("Argumento %s incorrecto. Formato esperado %s o %s", format, BOW, TFIDF));
            System.exit(1);
        }
        arffToWordVector(inputPath, outputPath, diccPath, format, sparse);
    }

    /**
     * Convierte los atributos de tipo string de un archivo .arff a formato word-vector y genera
     * el diccionario correspodiente
     *
     * @param pInputPath ruta del archivo .arff a transformar
     * @param pOutputPath ruta del arhivo .arff a generar
     * @param pDiccPath ruta donde guardar el diccionario
     * @param pFormat formato de word-vector en el que convertir los atributos (Bag of Words / TF-IDF)
     * @param pSparse indica si el atributo word-vector estará en formato sparse (disperso) o no
     */
    private static void arffToWordVector(String pInputPath, String pOutputPath,String pDiccPath, String pFormat, boolean pSparse) {
        Instances instances = utils.Utils.loadInstances(pInputPath);
        StringToWordVector toWordVectorFilter = new StringToWordVector(20000);
        if (instances != null) {
            try {
                toWordVectorFilter.setLowerCaseTokens(true);
                toWordVectorFilter.setOutputWordCounts(true);
                toWordVectorFilter.setDictionaryFileToSaveTo(new File(pDiccPath));
                toWordVectorFilter.setIDFTransform(pFormat.equals(TFIDF));
                toWordVectorFilter.setTFTransform(pFormat.equals(TFIDF));
                toWordVectorFilter.setInputFormat(instances);
                String relationName = instances.relationName();
                instances = Filter.useFilter(instances, toWordVectorFilter);

                // las instancias vienen por defecto en formato disperso (sparse)
                if (!pSparse) {
                    instances = utils.Utils.nonSparseFilter(instances);
                }
                if (instances.classIndex() == 0) {
                    instances = utils.Utils.moveFirstAttrToLast(instances);
                }
                instances.setRelationName(relationName);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
            
            utils.Utils.saveInstances(instances, pOutputPath);
            System.out.println(String.format("Conversión completa. Nuevo archivo: %s", pOutputPath));
        }
    }

}
