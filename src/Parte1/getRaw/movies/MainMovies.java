package Parte1.getRaw.movies;

import java.io.File;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.TextDirectoryLoader;

public class MainMovies {

    public static void main(String[] args) throws Exception {
        String inputPath = null;
        String outputPath = null;
        try {
            inputPath = args[0];
            outputPath = args[1];
            throw new IndexOutOfBoundsException();
        } catch (IndexOutOfBoundsException e) {
            String documentacion = "Este ejecutable convierte los sets de train y dev de reviews de películas en archivos .arff crudos.\n" +
                                    "Los archivos de texto (uno por cada review) deben estar en varios directorios que los clasifiquen según el criterio elegido.\n" +
                                    "Dos argumetos esperados:\n" +
                                        "\t1 - Ruta de la raíz del árbol de directorios.\n" +
                                        "\t2 - Ruta del archivo .arff a crear\n" +
                                    "\nEjemplo: java -jar getRawMovies.jar /path/to/movies/dir /path/to/output/arff";
            System.out.println(documentacion);
            System.exit(1);
        }
        convertirAArff(inputPath, outputPath);
    }

    /**
     * Convierte un árbol de archivos en un documento .arff, donde cada archivo es
     * una instancia, clasificada según el direcotorio en el que está.
     *
     * @param pathOrigen ruta de la raíz del árbol de directorios a transformar
     * @param pathDestino ruta del archivo .arff que se quiere generar
     * @throws Exception
     */
    public static void convertirAArff(String pathOrigen, String pathDestino) throws Exception {
        //Coge el path de origen y lo convierte en un dataset
        TextDirectoryLoader loader = new TextDirectoryLoader();
        File fichero = new File(pathOrigen);
        loader.setDirectory(fichero);
        Instances dataRaw = loader.getDataSet();

        // ponemos el nombre de la relación y los atributos
        dataRaw.setRelationName("review");
        dataRaw.renameAttribute(dataRaw.classIndex(), "@@class@@");
        dataRaw.renameAttribute(0, "text");

        //cargamos el path destino
        fichero = new File(pathDestino);

        //creamos el arffsaver, el cual, guarda en el destino en formato arff
        ArffSaver saver = new ArffSaver();
        saver.setInstances(dataRaw);
        saver.setFile(fichero);
        saver.writeBatch();
        System.out.println(String.format("Conversión completa. Nuevo archivo: %s", pathDestino));
    }
}
