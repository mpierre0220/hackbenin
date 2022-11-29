package com.example.textmapl.File;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.textmapl.modal.MessageText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class FileText {


    @RequiresApi(api = Build.VERSION_CODES.O)
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
        // mete teks lan nan fichye a
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            String info = text.replace("\n", "\t") + "\n";
            fileWriter.write(info);
            fileWriter.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean opdetTeks(Context konteks,int pos, String newTeks) {
        String FILE_NAME = "infoTexts.txt";
        File file = new File(konteks.getExternalFilesDir("external"), FILE_NAME);
        String ligneInfo = null;
        ArrayList<MessageText> arList = teksEnfoList(konteks);
        boolean res = false;

        file.delete();
        for (MessageText tx : arList) {
            try {
                FileWriter fileWriter = new FileWriter(file, true);
                if (arList.indexOf(tx) == pos) {
                    ligneInfo =newTeks.replace("\n", "\t") + "\n";
                } else {
                    ligneInfo = tx.getTeks() + "\n";
                }
                fileWriter.write(ligneInfo);
                fileWriter.close();
                res =  true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public static boolean deleteTeks(Context konteks, int pos) {
        String FILE_NAME = "infoTexts.txt";
        File file = new File(konteks.getExternalFilesDir("external"), FILE_NAME);
        String ligneInfo = null;
        ArrayList<MessageText> arList = teksEnfoList(konteks);
        boolean res = false;

        file.delete();
        for (MessageText tx : arList) {
            try {
                FileWriter fileWriter = new FileWriter(file, true);
                if (arList.indexOf(tx) != pos) {
                    ligneInfo = tx.getTeks() + "\n";
                    fileWriter.write(ligneInfo);
                }
                fileWriter.close();
                res = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    //metòd k ap tounnen tout enfòmasyon sou fichye ki kenben enfo tèks yo
    public static ArrayList<MessageText> teksEnfoList(Context konteks) {
        ArrayList<MessageText> arrayList = new ArrayList<>();
        StringTokenizer kenizer = null;
        arrayList.clear();
        String liyTeks = null;
        BufferedReader br = null;
        String FILE_NAME = "infoTexts.txt";
        File filePath = new File(konteks.getExternalFilesDir("external"), FILE_NAME);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                for (String s : Files.readAllLines(filePath.toPath())) {
                    //kenizer = new StringTokenizer(s);
                    //liyTeks = kenizer.nextToken();
                    arrayList.add(new MessageText(s));
                }
            } else {
                FileReader fr = new FileReader(filePath);
                br = new BufferedReader(fr);
                String infoLine;

                while ((infoLine = br.readLine()) != null) {
                    //kenizer = new StringTokenizer(infoLine);
                    //liyTeks = kenizer.nextToken();
                    arrayList.add(new MessageText(infoLine));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}
