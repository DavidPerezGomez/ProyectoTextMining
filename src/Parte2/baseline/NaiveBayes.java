package baseline;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

public class GetBaselineModel {

	public static void main(String[] args) {
		String trainBow = null;
		String devBow = null;
		try {
			trainBow = args[0];
			devBow = args[1];

		} catch (IndexOutOfBoundsException e) {
			String documentacion = "Este ejecutable se utiliza para obtener el modelo Baselune y su calidad estimada\n"
					+ "El archivo .arff original debe tener por lo menos un atributo de tipo string.\n"
					+ "Dos argumentos esperados:\n" + "\t1 - Ruta del archivo .arff de train\n"
					+ "\t2 - Ruta del archivo .arff de dev\n"
					+ "\nEjemplo: java -jar FSS.jar /path/to/input/arff /path/to/output/arff";
			System.out.println(documentacion);
			System.exit(1);
		} catch (IllegalArgumentException e) {
			utils.Utils.printlnWarning(String.format("Argumento %s incorrecto."));
			System.exit(1);
		}
		try {
			getBaselineModel(trainBow, devBow);
		} catch (Exception e) {
			
			System.out.println("No se ha podido completar el proceso ");
			e.printStackTrace();
		}

	}
	private static void getBaselineModel(String inputDev,String inputTrain) {
    	Instances train = utils.Utils.loadInstances(inputTrain, 0);
    	Instances dev=utils.Utils.loadInstances(inputDev, 0);
    	int clasMinId=getMinClass(train);
    	boolean kernelEstimator=false;
    	boolean supervisedDiscretization=false;
    	boolean kernelEstimatorMax=false;
    	boolean supervisedDiscretizationMax=false;
    	Double FMeasureMax=0.00;
    	
    	
    	NaiveBayes nb = new NaiveBayes();
    	nb.setUseKernelEstimator(kernelEstimator);
    	
    	
    	for(int i=1; i<3;i++) {
    		
    		nb.setUseSupervisedDiscretization(false);
    		supervisedDiscretization=false;
    		for(int j=1; j<3;j++) {
    			try {
					nb.buildClassifier(train);
					Evaluation ev = new Evaluation(train);
					ev.evaluateModel(nb, dev);
					if(FMeasureMax<ev.fMeasure(clasMinId)) {
						
						kernelEstimatorMax=kernelEstimator;
				    	supervisedDiscretizationMax=supervisedDiscretization;
						FMeasureMax=ev.fMeasure(clasMinId);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
    			
    			nb.setUseSupervisedDiscretization(true);
    			supervisedDiscretization=true;
    		}
    		
        	nb.setUseKernelEstimator(true);
        	kernelEstimator=true;

    	}
	}
	
	
	private static int getMinClass(Instances data) {
		int[] classCounts = data.attributeStats(data.classIndex()).nominalCounts;
		int classminid=0;
		int classmin=Integer.MAX_VALUE;
		for(int i=0; i<classCounts.length;i++) {
			if(classCounts[i]<classmin) {
				classmin=classCounts[i];
				classminid=i;
			}
		}

		return classminid;
	}
}
