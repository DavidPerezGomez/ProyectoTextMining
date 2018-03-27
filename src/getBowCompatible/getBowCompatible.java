package getBOWCompatible;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class getBowCompatible {
	public static void main(String[] args) throws IOException {
		String inputTrain = null;
        String inputDev = null;
        String outputDev = null;
        inputTrain = "/home/julen/Descargas/trainbow.arff";
        inputDev = "/home/julen/Descargas/dev.arff";
        outputDev = "/home/julen/Descargas/devbow.arff";
		makeCompatible(inputTrain, inputDev, outputDev);

        /*try {
        	inputTrain = args[0];
        	imputDev = args[1];
        	outputDev = args[2];
            if(args.length!=3)
                throw new IndexOutOfBoundsException();
        } catch (IndexOutOfBoundsException e) {
            utils.Utils.printlnWarning("Cuarto argumetos esperados:\n" +
                                               "\t1 - Ruta del archivo trainBow.arff a leer\n" +
                                               "\t2 - Ruta del archivo dev.arff a leer\n" +
                                               "\t3 - Ruta del archivo devBow.arff generado");
            System.exit(1);

        try {
			makeCompatible(inputTrain, imputDev, outputDev);
		} catch (IOException e1) {
		
		System.out.println("no existe el fichero output");
		}
	}*/
	}

	private static void makeCompatible(String inputTrain, String inputDev, String outputDev) throws IOException {
		Instances trainBow=utils.Utils.loadInstances(inputTrain, 0);
		Instances dev=utils.Utils.loadInstances(inputDev, 0);		
		Instances devBow=trainBow;

		// Igualamos el numero de instancias del dev y devBow
		for (int i=0; i >= (devBow.numInstances()-dev.numInstances() ); i++){
			devBow.remove(1);
		}
		
		//Ponemos todos los valores de los atributos de cada instancia a 0
		for(int i=0; i<=devBow.numInstances();i++) {
			for(int j=0; i<=devBow.numAttributes();j++) {
			devBow.get(i).setValue(j, 0.00);
		}
		}
		File fichero=new File(outputDev);

		//creamos el arffsaver el cual, guarda en el destino en formato arff
		ArffSaver saver=new ArffSaver();
		saver.setInstances(devBow);
		saver.setFile(fichero);
		saver.writeBatch();
		System.out.println("se termino");
	}
	
}
