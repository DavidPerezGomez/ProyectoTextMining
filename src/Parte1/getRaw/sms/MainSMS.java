package Parte1.getRaw.sms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MainSMS {

	public static void main(String[] args) throws Exception {
		String inputPath = null;
		String outputPath = null;
		try {
			inputPath = args[0];
			outputPath = args[1];
		} catch (IndexOutOfBoundsException e) {
			 String documentacion = "Este ejecutable convierte los sets de train y dev de clasificación de SMS en archivos .arff crudos.\n" +
                                    "Los archivos de texto deben contener en cada línea la clasificación del mensaje separada del propio mensaje por un tabulador.\n" +
                                    "Dos argumetos esperados:\n" +
                                        "\t1 - Ruta del archivo de texto a leer.\n" +
                                        "\t2 - Ruta del archivo .arff a crear\n" +
                                    "\nEjemplo: java -jar getRawSMS.jar /path/to/sms/file /path/to/output/arff";
            System.out.println(documentacion);
			System.exit(1);
		}
		convertir(inputPath, outputPath);
	}

    /**
     * Convierte un archivo de texto con atributos separados por tabuladores a formato .arff
     *
     * @param pathOrigen ruta del archivo de texto a transformar
     * @param pathDestino ruta del archivo .arff que se quiere generar
     * @throws IOException
     */
	private static void convertir(String pathOrigen, String pathDestino) throws IOException {
		FileReader fr = new FileReader(new File(pathOrigen));
		BufferedReader br = new BufferedReader(fr);
		String linea;
		File fi = new File(pathDestino);
		BufferedWriter bw = new BufferedWriter(new FileWriter(fi));
		PrintWriter pw = new PrintWriter(bw);
		pw.println("@RELATION smsSpam");
		pw.println();
		pw.println("@ATTRIBUTE text STRING");
		pw.println("@ATTRIBUTE @@class@@ {ham, spam}");
		pw.println();
		pw.println("@DATA");
		try {
			while ((linea = br.readLine()) != null) {
				String[] argu = linea.split("\\t");
				String clase = argu[0];
				String texto = argu[1];
				String texto2;
				if (clase.equals("ham") || clase.equals("spam")) {
                    texto2 = weka.core.Utils.quote(texto);
					pw.println(texto2 + "," + clase);
					pw.flush();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println(String.format("Conversión completa. Nuevo archivo: %s", pathDestino));
	}
}
