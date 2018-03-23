package practicas.proyectoTextMinig.getRaw;

import java.io.*;
import weka.*;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NominalToString;
import weka.filters.unsupervised.attribute.Remove;

public class CSVManager {

    public static Instances loadCSV(String pPath) {
        CSVLoader csvLoader = new CSVLoader();
        Instances instances;
        try {
            csvLoader.setSource(new File(pPath));
            instances = csvLoader.getDataSet();
        } catch (IOException e) {
            e.printStackTrace();
            instances = null;
        }
        return instances;
    }

    public static void cleanCSV(String pInputPath, String pOutputPath) {
        BufferedReader br = null;
        StringBuilder newLines = null;
        BufferedWriter bw;

        try {
            br = new BufferedReader(new FileReader(pInputPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            newLines = new StringBuilder();
            // asumimos que la primera línea es la cabecera
            String header = br.readLine();
            newLines.append(String.format("%s\n", header));
            String[] headerFields = separateCSVValue(header);
            int numValues = headerFields.length;
            System.out.println("Campos:");
            for (String field : headerFields) {
                System.out.println(String.format("\t%s", field));
            }

            String line;
            // readLine no devuelve line-terminators
            while((line = br.readLine()) != null) {
                // si tras quitar el salto de línea el String queda vacío
                // se pasa a analizar la siguiente línea
                // (omitimos líneas vacías)
                if (!line.equals("")) {
                    String[] values = separateCSVValue(line);
                    // si el número correcto de valores no es correcto
                    // se pasa a analizar la siguiente línea
                    if (values.length == numValues) {
                        String[] newValues = new String[values.length];
                        for(int i = 0; i < values.length; i++) {
                            newValues[i] = escapeChar(values[i], '\"');
                        }
                        String newLine = String.join("\",\"", newValues);
                        newLine = "\"" + newLine + "\"";
                        newLines.append(String.format("%s\n", newLine));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }


        try {
            bw = new BufferedWriter(new FileWriter(pOutputPath));
            bw.write(newLines.toString());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    private static String[] separateCSVValue(String pLine) {
        String line = pLine;
        if(line.charAt(0) == '\"')
            line = line.substring(1, line.length());
        if(line.charAt(line.length()-1) == '\"')
            line = line.substring(0, line.length()-1);
        return line.split("\",\"");
    }

    private static String escapeChar(String line, char pChar){
        String newLine = line;
        int index = newLine.indexOf("" + pChar);
        while(index != -1) {
            newLine = insertSubstring(newLine, "\\", index);
            index = newLine.indexOf("" + pChar, index+2);
        }
        return newLine;
    }

    private static String insertSubstring(String pString, String pSubstring, int pIndex) {
        return pString.substring(0, pIndex) + pSubstring + pString.substring(pIndex, pString.length());
    }
}
