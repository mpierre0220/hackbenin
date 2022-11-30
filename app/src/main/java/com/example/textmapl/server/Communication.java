package com.example.textmapl.server;

import android.content.Context;

import com.example.textmapl.Activity.GlobalVar;
import com.example.textmapl.Activity.KliyanHTTP;
import com.example.textmapl.Activity.OnKliyanHTTPFini;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class Communication  implements OnKliyanHTTPFini {

    KliyanHTTP client;
    public Communication(Context context){
        //Créer un client HTTP qui peut communiquer avec le serveur
        client = new KliyanHTTP(context, this, null);
    }
     //Methode qui reçoit la notification que l'appel REST avec le serveur a abouti
    public void apelRESTKliyanHTTPFini(String json) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        switch(GlobalVar.requeteType) {
            case GlobalVar.REQUETE_LISTER_ID_MESSAGES:
                List<Integer> l = gson.fromJson(json, new TypeToken<List<ListeMessageId>>(){}.getType());
                GlobalVar.listMessages = l;
                break;
            case GlobalVar.REQUETE_DEMANDER_MESSAGE:
                TextMessage t = gson.fromJson(json, TextMessage.class);
                GlobalVar.textMessage = t;
                break;
        }


    };

    //Methode qui reçoit la notification que l'appel REST avec le serveur a échoué
    public void apelRESTKliyanHTTPEchwe(int statusCode, String message) {
        GlobalVar.httpComplete = true;
        GlobalVar.httpSucces   = false;
    };

    public void posterMessage(String identifiant, String passe, String texte, int idMsg){
        GlobalVar.requeteType = GlobalVar.REQUETE_AJOUTER_MSG;
        client.KliyanHTTPPosterMessage(texte,null,identifiant, passe,idMsg, null);
    }

    public void changerMotDePasse(String identifiant, String passe, String nouveauPasse){
        GlobalVar.requeteType = GlobalVar.REQUETE_CHANGER_MOT_DE_PASSE;
        client.KliyanHTTPPosterMessage(null,null,identifiant, passe,-1, nouveauPasse);
    }
    public void demanderMessage(String identifiant, String passe, int idMsg){
        GlobalVar.requeteType = GlobalVar.REQUETE_DEMANDER_MESSAGE;
        client.KliyanHTTPPosterMessage("/textmsg/liretexte",null,identifiant, passe,idMsg, null);
    }
    public void demanderIdsMessage(String identifiant, String passe){
        GlobalVar.requeteType = GlobalVar.REQUETE_LISTER_ID_MESSAGES;
        client.KliyanHTTPPosterMessage("/textmsg/listeridtextes",null,identifiant, passe,-1, null);
    }
}
