package getRaw.sms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import getRaw.movies.Utils;

public class MainSMS {

	public static void main(String[] args) throws Exception {

		String inputPath = null;
		String outputPath = null;
		try {
			//inputPath = args[0];
			//outputPath = args[1];
			inputPath="/home/julen/Descargas/Datos/sms_spam/SMS_SpamCollection.train.txt";
			outputPath="/home/julen/Descargas/trainsms.arff";
		} catch (IndexOutOfBoundsException e) {
			Utils.printlnWarning("Dos argumetos esperados:\n" + "\t1 - Ruta del archivo raw a leer\n"
					+ "\t2 - Ruta del archivo .arff a crear");
			System.exit(1);
		}
		convertir(inputPath, outputPath);
	}

	public static void convertir(String pathOrigen, String pathDestino) throws IOException {

		FileReader fr = new FileReader(new File(pathOrigen));
		BufferedReader br = new BufferedReader(fr);
		String linea;
		File fi = new File(pathDestino);
		BufferedWriter bw = new BufferedWriter(new FileWriter(fi));
		PrintWriter pw = new PrintWriter(bw);
		pw.println("@RELATION SMS");
		pw.println();
		pw.println("@ATTRIBUTE clase {ham, spam}");
		pw.println("@ATTRIBUTE Text STRING");
		pw.println();
		pw.println("@DATA");
		try {
			while ((linea = br.readLine()) != null) {
				String[] argu = linea.split("\\t");
				String clase = argu[0];
				String texto = argu[1];
				String texto2;
				if (clase.equals("ham") || clase.equals("spam")) {
					// texto=texto.replace(",", "")
					texto2 = texto.replaceAll(";[-))(/''_,??+.^:\t,‘]", "");
					pw.println(clase + ",'" + texto2 + "'");
					pw.flush();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(String.format("Conversión completa. Nuevo archivo: %s", pathDestino));
	}
}
