package getRaw.tweets;

import java.io.*;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.CSVLoader;

public class CSVManager {

    /**
     * Carga los datos de un archivo .csv en isntancias.
     * @param pPath
     * @return
     */
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

    /**
     * Limpia un archivo .csv comprobando que no haya delimitadores mal abiertos/cerrados y
     * escapando caracteres problemáticos.
     * @param pInputPath
     * @param pOutputPath
     */
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
                            // utilizamos weka.core.Utils para preparar las String para weka
                            newValues[i] = Utils.quote(values[i]);
                            // si las String son correctas, Utils.quote() no añade comillas
                            // hay que asegurarse de que todos los valores van entrecomillados
                            if (!newValues[i].startsWith("\'"))
                                newValues[i] = "\'" + newValues[i];
                            if (!newValues[i].endsWith("\'"))
                                newValues[i] = newValues[i] + "\'";
                        }
                        String newLine = String.join(",", newValues);
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

    /**
     * Dada una línea estilo .csv devuelve un array con cada uno de los valores.
     * Ej.:
     * "valor1","valor2","valor3" -> {valor1, valor2, valor3}
     * @param pLine
     * @return
     */
    private static String[] separateCSVValue(String pLine) {
        String line = pLine;
        if(line.charAt(0) == '\"')
            line = line.substring(1, line.length());
        if(line.charAt(line.length()-1) == '\"')
            line = line.substring(0, line.length()-1);
        return line.split("\",\"");
    }

    /**
     * Escapa (pone \ delante) los todas las apariciones de pChar en pLine.
     * @param pLine
     * @param pChar
     * @return
     */
    private static String escapeChar(String pLine, char pChar){
        String newLine = pLine;
        int index = newLine.indexOf("" + pChar);
        while(index != -1) {
            newLine = insertSubstring(newLine, "\\", index);
            index = newLine.indexOf("" + pChar, index+2);
        }
        return newLine;
    }

    /**
     * Inserta pSubstring en pString en la posición pIndex.
     * Ej.:
     * insertSubstring("abcdefg", "123", 4) -> "abcd123efg"
     * @param pString
     * @param pSubstring
     * @param pIndex
     * @return
     */
    private static String insertSubstring(String pString, String pSubstring, int pIndex) {
        return pString.substring(0, pIndex) + pSubstring + pString.substring(pIndex, pString.length());
    }
}
