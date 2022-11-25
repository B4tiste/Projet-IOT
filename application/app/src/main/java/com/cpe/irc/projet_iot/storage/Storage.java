package com.cpe.irc.projet_iot.storage;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Storage {
    private static final String FILENAME = "sensors.json";
    private final Context context;

    public Storage(Context context) {
        this.context = context;
    }

    public void getStorage(String filename){
        File directory;
        if (filename.isEmpty()) {
            directory = this.context.getFilesDir();
        }
        else {
            directory = this.context.getDir(filename, Context.MODE_PRIVATE);
        }
        File[] files = directory.listFiles();
    }

    public String getFile(String filename){
        FileInputStream file;
        String content = null;
        try {
            file = this.context.openFileInput(filename);
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter("\\Z");
            content = scanner.next();
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return content;
    }

    public void saveFile(String filename, String content){
        try {
            FileOutputStream fos = this.context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(content.getBytes(StandardCharsets.UTF_8));
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasFile(String filename) {
        return this.context.getFileStreamPath(filename).exists();
    }
}
