package com.example.textmapl.server;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;

import androidx.annotation.RequiresApi;

import com.example.textmapl.Activity.GlobalVar;
import com.example.textmapl.Activity.KliyanHTTP;
import com.example.textmapl.Activity.OnKliyanHTTPFini;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import java.util.List;
/*
    Classe pour la communiation avec le serveur comprenant des requêtes API
 */
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
            case GlobalVar.REQUETE_LIRE_MESSAGE:
                TextMessage t = gson.fromJson(json, TextMessage.class);
                GlobalVar.textMessage = t;
                break;
            case GlobalVar.REQUETE_SUPPRIMER_MESSAGE:
                DeleteStatus s = gson.fromJson(json, DeleteStatus.class);
                GlobalVar.deleteStatus = s;
                break;
        }


    };

    //Methode qui reçoit la notification que l'appel REST avec le serveur a échoué
    public void apelRESTKliyanHTTPEchwe(int statusCode, String message) {
        GlobalVar.httpComplete = true;
        GlobalVar.httpSucces   = false;
    };

    public void ajouterMessage(String identifiant, String passe, String texte, int idMsg){
        GlobalVar.requeteType = GlobalVar.REQUETE_AJOUTER_MSG;
        client.KliyanHTTPPosterMessage("/textmsg/ajoutertexte",null,identifiant, passe, texte,idMsg, null);
    }

    public void changerMotDePasse(String identifiant, String passe, String nouveauPasse){
        GlobalVar.requeteType = GlobalVar.REQUETE_CHANGER_MOT_DE_PASSE;
        client.KliyanHTTPPosterMessage("/textmsg/nouveaumotdepasse",null,identifiant, passe, null,-1, nouveauPasse);
    }
    public void lireMessage(String identifiant, String passe, int idMsg){
        GlobalVar.requeteType = GlobalVar.REQUETE_LIRE_MESSAGE;
        client.KliyanHTTPPosterMessage("/textmsg/liretexte",null,identifiant, passe, null,idMsg, null);
    }
    public void listerIdsMessage(String identifiant, String passe){
        GlobalVar.requeteType = GlobalVar.REQUETE_LISTER_ID_MESSAGES;
        client.KliyanHTTPPosterMessage("/textmsg/listeridtextes",null,identifiant, passe, null,-1, null);

    }

    public void modifierMessage(String identifiant, String passe, String texte, int idMsg){
        GlobalVar.requeteType = GlobalVar.REQUETE_MODIFIER_MESSAGE;
        client.KliyanHTTPPosterMessage("/textmsg/modifiertexte",null,identifiant, passe, texte,idMsg, null);
    }

    public void supprimerMessage(String identifiant, String passe, int idMsg){
        GlobalVar.requeteType = GlobalVar.REQUETE_SUPPRIMER_MESSAGE;
        client.KliyanHTTPPosterMessage("/textmsg/supprimertexte",null,identifiant, passe, null,idMsg, null);
    }

    public void listerToutMessage(String identifiant, String passe){
        GlobalVar.requeteType = GlobalVar.REQUETE_LISTER_TOUT_MESSAGE;
        client.KliyanHTTPPosterMessage("/textmsg/listertextes",null,identifiant, passe, null,-1, null);
    }
}
