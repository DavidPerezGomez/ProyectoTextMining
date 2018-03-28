package getBowCompatible;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.FixedDictionaryStringToWordVector;
import weka.filters.unsupervised.attribute.Standardize;

public class getBowCompatible {
	public static void main(String[] args) throws IOException {
		String inputTrain = null;
        String inputDev = null;
        String inputDicc=null;
        String outputDev = null;
        inputTrain = null;
        inputDev = null;
        inputDicc= null;
        outputDev = null;
		makeCompatible(inputTrain, inputDev,inputDicc, outputDev);

        try {
        	inputTrain = args[0];
        	inputDev = args[1];
        	inputDicc= args[2];
        	outputDev = args[3];
            if(args.length!=3)
                throw new IndexOutOfBoundsException();
        } catch (IndexOutOfBoundsException e) {
            utils.Utils.printlnWarning("Cuarto argumetos esperados:\n" +
                                               "\t1 - Ruta del archivo trainBow.arff a leer\n" +
                                               "\t2 - Ruta del archivo dev.arff a leer\n" +
                                               "\t3 - Ruta del diccionario de train.arff"+
                                               "\t4 - Ruta del archivo devBow.arff generado");
            System.exit(1);

        try {
			makeCompatible(inputTrain, inputDev, inputDicc, outputDev);
		} catch (IOException e1) {
		
		System.out.println("no existe el fichero output");
		}
	}
	}

	private static void makeCompatible(String inputTrain, String inputDev, String pInputDicc, String outputDev) throws IOException {
		Instances trainBow=utils.Utils.loadInstances(inputTrain, 0);
		Instances dev=utils.Utils.loadInstances(inputDev, 0);		
		Instances devBow=null;
		//Standardize filtro2= new Standardize();
		//preparamos el filtro con el diccionario de train y lo aplicamos a dev
		FixedDictionaryStringToWordVector filtro = new FixedDictionaryStringToWordVector();
		filtro.setDictionaryFile(new File(pInputDicc));
		
		
		try {
			//no se si habria que meterle el formato de dev tal vez
			filtro.setInputFormat(trainBow);
			devBow=Filter.useFilter(dev, filtro);
			System.out.println(trainBow.numAttributes());
			System.out.println(devBow.numAttributes());
			//filtro2.setInputFormat(dev);
			//devBow= Filter.useFilter(trainBow, filtro2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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