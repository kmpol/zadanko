package pl.karol;

import com.google.common.io.Files;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class Main {
   static File file;
   private static final FileCounter fileCounter = new FileCounter();
   private static final String fileName = "counter.txt";

    public static void main(String[] args) {
        String[] folderNames = {"HOME", "DEV", "TEST"};
        initializeFolders(folderNames);
        List<File> files = getFileNamesFromDirectory("HOME");

        for (int i = 0; i < files.size(); i++) {
            Long fileCreationTimeMillis = getFileCreationTime(files.get(i).getName());
            int hours   = (int) ((fileCreationTimeMillis / (1000*60*60)) % 24);
            moveFileBasedExtensionAndCreationTime(files.get(i).getName(), hours, files.get(i));
        }

        createCounterFile(fileName, fileCounter);
        printCounter();
    }

    private static void initializeFolders(String [] folderNames){
        for (String folderName : folderNames) {
            file = new File("./" + folderName);
            boolean fileExists = file.exists();
            writeFolder(fileExists);
        }
    }

    private static void writeFolder(boolean fileExists) {
        if (!fileExists) {
            fileExists = file.mkdir();
        }
    }

    public static String getFileExtension(String filename) {
        return com.google.common.io.Files.getFileExtension(filename);
    }

    public static List<File> getFileNamesFromDirectory(String directory){
        List<File> results = new ArrayList<>();


        File[] files = new File("./" + directory).listFiles();

        for (File file : files) {
            if (file.isFile()) {
                results.add(file);
            }
        }

        return results;
    }

    private static Long getFileCreationTime(String fileName){
        Path currentRelativePath = Paths.get("HOME/" + fileName).toAbsolutePath();
        System.out.println(currentRelativePath);

        try {
            BasicFileAttributes attr = java.nio.file.Files.readAttributes(Path.of(currentRelativePath.toUri()), BasicFileAttributes.class);
            Long fileTime = attr.creationTime().toMillis();
            return fileTime;

        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private static void moveFileBasedExtensionAndCreationTime(String fileName, int creationDate, File file){
        String fileExtension = getFileExtension(fileName);
        Path pathFileWithNameToDev = Paths.get("DEV/" + fileName).toAbsolutePath();
        Path pathFileWithNameToTest = Paths.get("TEST/" + fileName).toAbsolutePath();

        if(fileExtension.equals("xml") || (fileExtension.equals("jar") && creationDate % 2 == 0)){
            file.renameTo(new File(pathFileWithNameToDev.toUri()));
            fileCounter.setNumberOfFilesInDev(fileCounter.getNumberOfFilesInDev() + 1);
            fileCounter.setNumberOfFilesMoved(fileCounter.getNumberOfFilesMoved() + 1);

        } else if (fileExtension.equals("jar") && creationDate % 2 == 1){
            file.renameTo(new File(pathFileWithNameToTest.toUri()));
            fileCounter.setNumberOfFilesInTest(fileCounter.getNumberOfFilesInTest() + 1);
            fileCounter.setNumberOfFilesMoved(fileCounter.getNumberOfFilesMoved() + 1);
        }
//        fileCounter.setNumberOfFilesMoved(fileCounter.getNumberOfFilesMoved() + 1); not a good idea when extension is different than xml/jar

    }

    private static void createCounterFile(String fileName, FileCounter fileCounter){
        try(
                var fs = new FileOutputStream(fileName);
                var os = new ObjectOutputStream(fs);
        ) {
            os.writeObject(fileCounter);
            System.out.println("Zapisano obiekt do pliku");

        } catch (IOException e) {
            System.err.println("Bład zapisu pliku " + fileName);
            e.printStackTrace();
        }
    }

    private static void printCounter(){
        try (
                var fis = new FileInputStream(fileName);
                var ois = new ObjectInputStream(fis);
        ) {
            FileCounter fileCounter = (FileCounter) ois.readObject();

            if (fileCounter != null) {
                System.out.println("Counter");
                System.out.println("Liczba plików przesuniętych w ogóle: " + fileCounter.getNumberOfFilesMoved());
                System.out.println("Liczba plików przesuniętych do folderu DEV: " + fileCounter.getNumberOfFilesInDev());
                System.out.println("Liczba plików przesuniętych do folderu TEST: " + fileCounter.getNumberOfFilesInTest());
            }

        } catch (ClassNotFoundException | IOException e) {
            System.err.println("Nie udało się odczytać pliku");
            e.printStackTrace();
        }
    }
}