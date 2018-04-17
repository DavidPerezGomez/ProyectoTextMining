package Parte2.baseline;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

public class GetBaselineModel {

	public static void main(String[] args) {
		String trainBow = null;
		String devBow = null;
		String path = null;
		try {
			trainBow = args[0];
			path=args[1];

		} catch (IndexOutOfBoundsException e) {
			String documentacion = "Este ejecutable se utiliza para obtener el modelo Baselune y su calidad estimada\n"
					+ "El archivo .arff original debe tener por lo menos un atributo de tipo string.\n"
					+ "Dos argumentos esperados:\n" + "\t1 - Ruta del archivo .arff de train\n"
					+ "\t2 - Ruta donde guardar el modelo y la calidad\n"
					+ "\nEjemplo: java -jar FSS.jar /path/to/input/arff /path/to/output/arff";
			System.out.println(documentacion);
			System.exit(1);
		} catch (IllegalArgumentException e) {
			utils.Utils.printlnWarning(String.format("Argumento %s incorrecto."));
			System.exit(1);
		}
		try {
			getBaselineModel(trainBow, devBow, path);
		} catch (Exception e) {
			
			System.out.println("No se ha podido completar el proceso ");
			e.printStackTrace();
		}

	}
	private static void getBaselineModel(String inputTrain, String inputDev, String path) {
    	Instances train = utils.Utils.loadInstances(inputTrain);
    	Instances dev=utils.Utils.loadInstances(inputDev);
    	int clasMinId=getMinClass(train);
    	
    	boolean kernelEstimator=false;
    	boolean supervisedDiscretization=false;
    	
    	boolean kernelEstimatorMax=false;
    	boolean supervisedDiscretizationMax=false;
    	Double fMeasureMax=0.00;
    	
    	
    	NaiveBayes nb = new NaiveBayes();
    	nb.setUseKernelEstimator(kernelEstimator);
    	
    	
    	for(int i=1; i<3;i++) {   		
    		nb.setUseSupervisedDiscretization(false);
    		supervisedDiscretization=false;
    		for(int j=1; j<3;j++) {
    			try {
    				Evaluation ev= utils.Utils.evalKFoldCrossValidation(nb, train, 10, 1);	
					if(fMeasureMax<ev.fMeasure(clasMinId)) {						
						kernelEstimatorMax=kernelEstimator;
				    	supervisedDiscretizationMax=supervisedDiscretization;
						fMeasureMax=ev.fMeasure(clasMinId);
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
    	System.out.println("==========================================================");
    	System.out.println("PARAMETROS OPTIMOS NAIVE BAYES: ");
    	System.out.println("MEJOR KERNEL ESTIMATOR: "+kernelEstimatorMax);
    	System.out.println("MEJOR SUPERVISE DISCRETIZATION: "+supervisedDiscretizationMax);
    	System.out.println("==========================================================");
    	System.out.println("");
    	System.out.println("");
    	System.out.println("");
    	
    	NaiveBayes nbOpt = new NaiveBayes();
    	try {  		
    		nbOpt.setUseKernelEstimator(kernelEstimatorMax);
        	nbOpt.setUseSupervisedDiscretization(supervisedDiscretizationMax);
			
			
		} catch (Exception e) {
			System.out.println("No se ha podido construir el clasificador");
			e.printStackTrace();
		}
    	
    	String pathModel=path+"/NaiveBayes.model";
    	utils.Utils.saveModel(nbOpt, pathModel);
    	
    	
    	String pathCalidad=path+"/NaiveBayes_CalidadEstimada.txt";
    	try {
    		Evaluation nh=utils.Utils.evalWithTrainSet(nbOpt, train);
        	System.out.println("==========================================================");
        	System.out.println("RESULTADOS CON ESQUEMA DE EVALUACION NO-HONESTO: ");
        	System.out.println(nh.toSummaryString());
			System.out.println(nh.toClassDetailsString());
			System.out.println(nh.toMatrixString());
			System.out.println("==========================================================");
		   	System.out.println("");
	    	System.out.println("");
	    	System.out.println("");
	    	
	    	utils.Utils.writeToFile("==========================================================\n", pathCalidad);
	    	utils.Utils.writeToFile("RESULTADOS CON ESQUEMA DE EVALUACION NO-HONESTO: \n", pathCalidad);
	    	utils.Utils.writeToFile(nh.toSummaryString(), pathCalidad);
	    	utils.Utils.writeToFile(nh.toClassDetailsString(), pathCalidad);
	    	utils.Utils.writeToFile(nh.toMatrixString(), pathCalidad);
	    	utils.Utils.writeToFile("==========================================================\n", pathCalidad);
	    	utils.Utils.writeToFile("\n", pathCalidad);
	    	utils.Utils.writeToFile("\n", pathCalidad);
	    	utils.Utils.writeToFile("\n", pathCalidad);
	    	
		} catch (Exception e1) {
			System.out.println("NO SE HA PODIDO IMPRIMIR LOS DETALLES DE LA CLASE");
			e1.printStackTrace();
		}

 
    	
    	
    	
    	try {
    		Evaluation ho=utils.Utils.evalHoldOut(nbOpt, train, 66.00);
        	System.out.println("==========================================================");
        	System.out.println("RESULTADOS CON ESQUEMA DE EVALUACION HOLD-OUT : ");
        	System.out.println(ho.toSummaryString());
			System.out.println(ho.toClassDetailsString());
			System.out.println(ho.toMatrixString());
			System.out.println("==========================================================");
		   	System.out.println("");
	    	System.out.println("");
	    	System.out.println("");
	    	
	    	utils.Utils.writeToFile("==========================================================\n", pathCalidad);
	    	utils.Utils.writeToFile("RESULTADOS CON ESQUEMA DE EVALUACION HOLD-OUT : \n", pathCalidad);
	    	utils.Utils.writeToFile(ho.toSummaryString(), pathCalidad);
	    	utils.Utils.writeToFile(ho.toClassDetailsString(), pathCalidad);
	    	utils.Utils.writeToFile(ho.toMatrixString(), pathCalidad);
	    	utils.Utils.writeToFile("==========================================================\n", pathCalidad);
	    	utils.Utils.writeToFile("\n", pathCalidad);
	    	utils.Utils.writeToFile("\n", pathCalidad);
	    	utils.Utils.writeToFile("\n", pathCalidad);
		} catch (Exception e1) {
			System.out.println("NO SE HA PODIDO IMPRIMIR LOS DETALLES DE LA CLASE");
			e1.printStackTrace();
		}
    	
    	try {
    		Evaluation kf=utils.Utils.evalKFoldCrossValidation(nbOpt, train, 10, 1);
        	System.out.println("==========================================================");
        	System.out.println("RESULTADOS CON ESQUEMA DE EVALUACION 10-fold cross-validation : ");
        	System.out.println(kf.toSummaryString());
			System.out.println(kf.toClassDetailsString());
			System.out.println(kf.toMatrixString());
			System.out.println("==========================================================");
		   	System.out.println("");
	    	System.out.println("");
	    	System.out.println("");
	    	utils.Utils.writeToFile("==========================================================\n", pathCalidad);
	    	utils.Utils.writeToFile("RESULTADOS CON ESQUEMA DE EVALUACION 10-fold cross-validation :\n", pathCalidad);
	    	utils.Utils.writeToFile(kf.toSummaryString(), pathCalidad);
	    	utils.Utils.writeToFile(kf.toClassDetailsString(), pathCalidad);
	    	utils.Utils.writeToFile(kf.toMatrixString(), pathCalidad);
	    	utils.Utils.writeToFile("==========================================================\n", pathCalidad);
	    	utils.Utils.writeToFile("\n", pathCalidad);
	    	utils.Utils.writeToFile("\n", pathCalidad);
	    	utils.Utils.writeToFile("\n", pathCalidad);
		} catch (Exception e1) {
			System.out.println("NO SE HA PODIDO IMPRIMIR LOS DETALLES DE LA CLASE");
			e1.printStackTrace();
		}
    	
    	try {
    		Evaluation lo= utils.Utils.evalLeaveOneOut(nbOpt, train, 1);
        	System.out.println("==========================================================");
        	System.out.println("RESULTADOS CON ESQUEMA DE EVALUACION LEAVE-ONE-OUT : ");
        	System.out.println(lo.toSummaryString());
			System.out.println(lo.toClassDetailsString());
			System.out.println(lo.toMatrixString());
			System.out.println("==========================================================");
		   	System.out.println("");
	    	System.out.println("");
	    	System.out.println("");
	    	utils.Utils.writeToFile("==========================================================\n", pathCalidad);
	    	utils.Utils.writeToFile("RESULTADOS CON ESQUEMA DE EVALUACION LEAVE-ONE-OUT :\n", pathCalidad);
	    	utils.Utils.writeToFile(lo.toSummaryString(), pathCalidad);
	    	utils.Utils.writeToFile(lo.toClassDetailsString(), pathCalidad);
	    	utils.Utils.writeToFile(lo.toMatrixString(), pathCalidad);
	    	utils.Utils.writeToFile("==========================================================\n", pathCalidad);
	    	utils.Utils.writeToFile("\n", pathCalidad);
	    	utils.Utils.writeToFile("\n", pathCalidad);
	    	utils.Utils.writeToFile("\n", pathCalidad);
	    	
		} catch (Exception e1) {
			System.out.println("NO SE HA PODIDO IMPRIMIR LOS DETALLES DE LA CLASE");
			e1.printStackTrace();
		}
    	
	}
	
	
	private static int getMinClass(Instances data) {
		int[] classCounts = data.attributeStats(data.classIndex()).nominalCounts;
		int classminid=0;
		int classmin=classCounts[0];
		for(int i=0; i<classCounts.length;i++) {
			if(classCounts[i]<classmin) {
				classmin=classCounts[i];
				classminid=i;
			}
		}

		return classminid;
	}
}
