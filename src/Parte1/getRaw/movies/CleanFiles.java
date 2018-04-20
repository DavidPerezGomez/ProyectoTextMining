package Parte1.getRaw.movies;

import org.apache.commons.io.FileUtils;

import java.io.*;

public class CleanFiles {

    /**
     * @param pDirRoot
     * @param pTempRoot
     */
    public static void cleanFiles(String pDirRoot, String pTempRoot) {
        // https://stackoverflow.com/questions/6214703/copy-entire-directory-contents-to-another-directory
        // hace falta la librería Apache Commons IO
        // http://commons.apache.org/proper/commons-io/
        try {
            File srcFile = new File(pDirRoot);
            File newFile = new File(pTempRoot);
            FileUtils.copyDirectory(srcFile, newFile);
            cleanFiles(newFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * @param pDirRoot
     */
    private static void cleanFiles(String pDirRoot) {
        // https://stackoverflow.com/questions/5694385/getting-the-filenames-of-all-files-in-a-folder

        File rootFolder = new File(pDirRoot);
        File[] listOfFiles = rootFolder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    cleanFile(file);
                } else if (file.isDirectory()) {
                    cleanFiles(file.getAbsolutePath());
                }
            }
        }
    }

    /**
     * @param pFile
     */
    private static void cleanFile(File pFile) {
        // http://www.java2novice.com/java_string_examples/remove-non-ascii-chars/
        try {
            BufferedReader br = new BufferedReader(new FileReader(pFile));
            String line;
            StringBuilder text = new StringBuilder();
            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            String newText = text.toString().replaceAll("[^\\p{Print}]", "");
            BufferedWriter bw = new BufferedWriter(new FileWriter(pFile));
            bw.write(newText, 0, newText.length());
            bw.newLine();
            br.close();
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}