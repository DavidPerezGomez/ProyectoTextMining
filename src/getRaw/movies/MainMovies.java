package getRaw.movies;

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
            	//inputPath="/home/julen/Descargas/Conjuntos de Datos-20180322/movies_reviews/train";
            	//outputPath="/home/julen/Descargas/train.arff";
            } catch (IndexOutOfBoundsException e) {
                utils.Utils.printlnWarning("Dos argumetos esperados:\n" +
                                             "\t1 - Ruta de la raíz del árbol de directorios.\n" +
                                             "\t2 - Ruta del archivo .arff a crear");
                System.exit(1);
            }

            convertirAArff(inputPath, outputPath);
        
    }

	public static void convertirAArff(String pathOrigen, String pathDestino) throws Exception{
		//Coge el path de origen y lo convierte en un dataset
		TextDirectoryLoader loader = new TextDirectoryLoader();
		File fichero=new File(pathOrigen);
		loader.setDirectory(fichero);	
		Instances dataRaw = loader.getDataSet();
		dataRaw.renameAttribute(dataRaw.classIndex(), "class");
		dataRaw.setRelationName("review");
		
		//cargamos el path destino
		fichero=new File(pathDestino);

		//creamos el arffsaver el cual, guarda en el destino en formato arff
		ArffSaver saver=new ArffSaver();
		saver.setInstances(dataRaw);
		saver.setFile(fichero);
		saver.writeBatch();
		System.out.println(String.format("Conversión completa. Nuevo archivo: %s", pathDestino));	}
}
