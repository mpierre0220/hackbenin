package com.example.textmapl.server;

import android.content.Context;

import com.example.textmapl.Activity.GlobalVar;
import com.example.textmapl.Activity.KliyanHTTP;
import com.example.textmapl.Activity.OnKliyanHTTPFini;

import org.json.JSONObject;

public class Communication  implements OnKliyanHTTPFini {
    KliyanHTTP client;
    public Communication(Context context){
        //Créer un client HTTP qui peut communiquer avec le serveur
        client = new KliyanHTTP(context, this, null);
    }
    //Methode qui reçoit la notification que l'appel REST avec le serveur a abouti
    public void apelRESTKliyanHTTPFini(JSONObject json) {};

    //Methode qui reçoit la notification que l'appel REST avec le serveur a échoué
    public void apelRESTKliyanHTTPEchwe(int statusCode, String message) {};

    public void posterMessage(String identifiant, String passe, String texte, int idMsg){
        GlobalVar.requeteType = GlobalVar.REQUETE_AJOUTER_MSG;
        client.KliyanHTTPPosterMessage(texte,null,identifiant, passe,idMsg, null);
    }

    public void changerMotDePasse(String identifiant, String passe, String nouveauPasse){
        GlobalVar.requeteType = GlobalVar.REQUETE_CHANGER_MOT_DE_PASSE;
        client.KliyanHTTPPosterMessage(null,null,identifiant, passe,-1, nouveauPasse);
    }
}
