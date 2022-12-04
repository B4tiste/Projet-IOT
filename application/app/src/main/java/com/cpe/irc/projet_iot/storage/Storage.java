package com.cpe.irc.projet_iot.storage;

import android.content.Context;

import com.cpe.irc.projet_iot.data.Crypter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Classe pour gérer le stockage des données.
 */
public class Storage {
    private final Context context;

    /**
     * Constructeur de la classe de stockage.
     * @param context le contexte de l'application.
     */
    public Storage(Context context) {
        this.context = context;
    }

    /**
     * Récupere le contenu d'un fichier.
     * @param filename le nom du fichier.
     * @return le contenu du fichier.
     */
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
        content = Crypter.decode(content);
        return content;
    }

    /**
     * Sauvegarde un fichier.
     * @param filename le nom du fichier.
     * @param content le contenu du fichier.
     */
    public void saveFile(String filename, String content){
        content = Crypter.encode(content);
        try {
            FileOutputStream fos = this.context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(content.getBytes(StandardCharsets.UTF_8));
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Vérifie si un fichier existe.
     * @param filename le nom du fichier.
     * @return true si le fichier existe, false sinon.
     */
    public boolean hasFile(String filename) {
        return this.context.getFileStreamPath(filename).exists();
    }
}
