package com.example.textmapl.File;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.textmapl.Activity.GlobalVar;
import com.example.textmapl.modal.MessageText;
import com.example.textmapl.modal.User;
import com.example.textmapl.server.Communication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/*
    Classe pour la gestion des fichiers de textes
 */
public class FileText {
    public static Communication communication;
    @RequiresApi(api = Build.VERSION_CODES.O)
    // Fonction pour enregistrer les textes messages dans le fichier "infoTexts.txt" en mode d'ouverture pour l'écriture
    public static boolean saveText(Context konteks, String text) {
        String FILE_NAME = "infoTexts.txt";
        File file = new File(konteks.getExternalFilesDir("external"), FILE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Mettre le texte dans le fichier
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            String info = text.replace("\n", "\t") + "\n";
            fileWriter.write(info); // faire l'écriture
            fileWriter.close(); // fermeture du fichier
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fonction pour la création de l'utilisateur dans le fichier "user.txt"
    public static boolean createUser(Context konteks, String username, String password) {
        String FILE_NAME = "user.txt";
        boolean result = false;
        File file = new File(konteks.getExternalFilesDir("external"), FILE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Mettre le texte dans le fichier
            try {
                FileWriter fileWriter = new FileWriter(file, true);
                String info = username + "\t" + password + "\n";
                fileWriter.write(info); // faire l'écriture
                fileWriter.close(); // fermeture du fichier
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }
        }
        return result;
    }
    // Vérification existence du fichier user.txt
    public  static boolean verifyIfExisteFileUser(Context contexte){
        String FILE_NAME = "user.txt";
        File file = new File(contexte.getExternalFilesDir("external"), FILE_NAME);
        if (file.exists()){
            return true;
        }else {
            return false;
        }
    }

    // Fonction permettant de retourner toutes les infos sur l'utilisateur existant dans le fichier texte user
    public static User userInfo(Context konteks) {
        ArrayList<User> arrayList = new ArrayList<>();
        arrayList.clear();
        BufferedReader br = null;
        String FILE_NAME = "user.txt";
        File filePath = new File(konteks.getExternalFilesDir("external"), FILE_NAME);
        String liy = null;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // si la version SDK est supérieur à la version code
                // faire la lecture du fichier avec la fonction readAllLines(...)
                List<String> liy_yo = Files.readAllLines(filePath.toPath());
                liy = liy_yo.get(0);

            } else {
                // ici, faire la lecture du fichier avec le constructeur FileReader(...)
                FileReader fr = new FileReader(filePath);
                br = new BufferedReader(fr);
                liy = br.readLine();
            }
            String[] valeyo = liy.split("\t");
            return new User(valeyo[0], valeyo[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    // Fonction pour faire la mise à jour pour un utilisateur
    public static void UpdateUser(Context contexte, String username, String password, String newPassword) {
        User info = userInfo(contexte); // Lister les infos sur le fichier d'utilisateur user.txt
        String ligneInfo = null;
        String FILE_NAME = "user.txt";
        File filePath = new File(contexte.getExternalFilesDir("external"), FILE_NAME); // path du fichier
        filePath.delete(); // La suppression du fichier user.txt
        try {
            // Ecrire à nouveau la lignee d'information du variable ligneInfo
            FileWriter fileWriter = new FileWriter(filePath, true);
                ligneInfo = username + "\t" + newPassword + "\n";
            fileWriter.write(ligneInfo); // faire l'écriture
            fileWriter.close(); // fermeture du fichier
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Fonction pour prendre le nom et le mot de passe de l'utilisateur connecté dans des variables statiques de la classe GlobalVar.java
    public static void GetUserConnected(Context contexte) {
        User info = userInfo(contexte); // Lister les infos sur le fichier d'utilisateur user.txt
        try {
                if (!info.username.isEmpty()){
                    GlobalVar.userConnected = info.username;
                    GlobalVar.passwordConnected = info.password;
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Fonction pour faire la mise à jour d'un message texte dans le fichier infoTexts.txt
    public static boolean opdetTeks(Context contexte, int pos, String newTeks) {
        String FILE_NAME = "infoTexts.txt";
        File file = new File(contexte.getExternalFilesDir("external"), FILE_NAME);
        String ligneInfo = null;
        ArrayList<MessageText> arList = teksEnfoList(contexte); // Lister toutes les messages contenant dans le fichier texte
        boolean res = false;
        communication = new Communication(contexte);
        int identServer = -1;

        file.delete(); // la suppression du fichier "infoTexts.txt"
        for (MessageText tx : arList) { // parcourir la liste
            try {
                FileWriter fileWriter = new FileWriter(file, true);
                if (arList.indexOf(tx) == pos) { // si la position du message texte (pos) séléctionné est égale à la position de la liste arList
                    ligneInfo = newTeks.replace("\n", "\t") + "\n"; // mettre le nouveau texte dans le variable ligneInfo en remplaçant les entrées par une tabulation
                    identServer = pos + 1;
                } else { // sinon
                    ligneInfo = tx.getTeks() + "\n"; // Mettre l'ancien texte dans le variable ligneInfo
                }
                fileWriter.write(ligneInfo); // ecrire la ligne
                fileWriter.close(); // fermeture du fichier
                res = true;
                // Fonction pour faire la modification du texte dans le serveur
                communication.modifierMessage(GlobalVar.userConnected, GlobalVar.passwordConnected, newTeks, identServer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    // Fonction pour faire la suppression d'un message texte dans le fichier infoTexts.txt
    public static boolean deleteTeks(Context contexte, int pos) {
        String FILE_NAME = "infoTexts.txt";
        File file = new File(contexte.getExternalFilesDir("external"), FILE_NAME);
        String ligneInfo = null;
        ArrayList<MessageText> arList = teksEnfoList(contexte);// Lister toutes les messages contenant dans le fichier texte
        boolean res = false;
        int identServer = -1;
        communication = new Communication(contexte);

        file.delete(); // la suppression du fichier "infoTexts.txt"
        for (MessageText tx : arList) { // parcourir la liste
            try {
                FileWriter fileWriter = new FileWriter(file, true);
                if (arList.indexOf(tx) != pos) { // si la position du message texte (pos) séléctionné est égale à la position de la liste arList
                    ligneInfo = tx.getTeks() + "\n";// Mettre l'ancien texte dans le variable ligneInfo
                    fileWriter.write(ligneInfo); // faire l'écriture
                }else {
                    identServer = pos + 1;
                }
                fileWriter.close(); // fermeture du fichier
                res = true;
                // Methode permettant la suppression du message texte dans le serveur
                communication.supprimerMessage(GlobalVar.userConnected, GlobalVar.passwordConnected, identServer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    // Fonction qui renvoie toutes les informations contenant dans le fichier "infoTexts.txt"
    public static ArrayList<MessageText> teksEnfoList(Context konteks) {
        ArrayList<MessageText> arrayList = new ArrayList<>();
        arrayList.clear();
        BufferedReader br = null;
        String FILE_NAME = "infoTexts.txt";
        File filePath = new File(konteks.getExternalFilesDir("external"), FILE_NAME);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Ici, si la version SDK est supérieur à la version code
                // faire la lecture avec la fonction readAllLines(...)
                for (String s : Files.readAllLines(filePath.toPath())) { // parcourir la boucle
                    arrayList.add(new MessageText(s)); // ajouter la ligne d'information dans la liste "arrayList"
                }
            } else { // sinon
                // faire la lecture avec le constructeur FileReader(...)
                FileReader fr = new FileReader(filePath);
                br = new BufferedReader(fr);
                String infoLine;

                while ((infoLine = br.readLine()) != null) { // parcourir la boucle
                    arrayList.add(new MessageText(infoLine)); // ajouter la ligne d'information dans la liste "arrayList"
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList; // retourner la liste
    }
}
