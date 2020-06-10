package com.company.editors;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileEditor {

    private static FileEditor instance;

    /*public static FileEditor getInstance() {

        if (instance == null) {
            instance = new FileEditor();
        }
        return instance;
    }*/

    public static void createAndUpdateFile(String directory, String className, String text) {
        File file = new File(directory, className + ".txt");
        FileWriter fileWriter = null;
        try {
            file.createNewFile();
            fileWriter = new FileWriter(file);
            fileWriter.write(text);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


// создаем объекты для файлов, которые находятся в каталоге

    }


}
