package com.example.textmapl.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/*import com.example.loteriemaplcomm.Aktivite.Service.Senkronizasyon;
import com.example.loteriemaplcomm.Aktivite.data.model.DoneEksepsyon;
import com.example.loteriemaplcomm.KontroleYo.DoneServer;
import com.example.loteriemaplcomm.KontroleYo.GlobalLotri;
import com.example.loteriemaplcomm.KontroleYo.PranEreKodYo; */
import com.example.textmapl.File.FileText;
import com.example.textmapl.modal.MessageText;
import com.example.textmapl.server.ListeMessageId;
import com.example.textmapl.server.TextMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
/*
    Classe permettant de faire des requetes HTTP via le serveur
 */
public class KliyanHTTP {

    private static final String TAG = "TEXT_MAPL";

    AsyncHttpClient asyncHttpClient;
    SyncHttpClient syncHttpClient;
    RequestParams requestParams;
    OnKliyanHTTPFini listener;

    Context context;

    public static String BASE_URL = "http://bancoregtest.com";
    String jsonResponse;

    public KliyanHTTP(Context context, OnKliyanHTTPFini listener, String baseUrl) {
        asyncHttpClient = new AsyncHttpClient(true, 80, 443);
        syncHttpClient = new SyncHttpClient(true, 80, 443);
        asyncHttpClient.setTimeout(10000);
        syncHttpClient.setTimeout(10000);
        requestParams = new RequestParams();
        this.context = context;
        //listener est la classe qui a instantié le client et qui recevra les messages de
        //notifications
        this.listener = listener;
        if (baseUrl != null) {
            this.BASE_URL = baseUrl;
        }
    }

    public void KliyanHTTPGet(String queryTerm, String baseUrl) {
        if (!EstatiKoneksyon.isConnected(context)) {
            listener.apelRESTKliyanHTTPEchwe(-1, null);
        }
        String URL;
        if (baseUrl != null) {
            URL = String.format("%s%s", baseUrl, queryTerm);
        } else {
            URL = String.format("%s%s", GlobalVar.servers[GlobalVar.SERVER], queryTerm);
        }
        asyncHttpClient.get(URL, requestParams, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                String jsonResponse = response.toString();
                listener.apelRESTKliyanHTTPFini(jsonResponse);
                GlobalVar.httpSucces = true;
                //Log.i(TAG, "onSuccess: " + jsonResponse);
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                super.onFailure(statusCode, headers, throwable, response);
                try {
                    listener.apelRESTKliyanHTTPEchwe(statusCode, null);
                    GlobalVar.httpSucces = false;
                } catch (Exception e) {
                }
            }
        });
    }

    public String KliyanHTTPPosterMessage(String chaineRequete,
                                          String URLDeBase,
                                          String identifiant,
                                          String passe,
                                          String textMsg,
                                          int idMsg,
                                          String nouveauPasse) {
        RequestParams params = new RequestParams();
        String URL;
        final String[] reponse = {"UNKNOW"};
        //verifier qu'on est connecté au réseau
        if (!EstatiKoneksyon.isConnected(context)) {
            //sinon la communication n'est pas possible
            listener.apelRESTKliyanHTTPEchwe(-1, null);
        }
        //
        if (URLDeBase != null) {
            URL = String.format("%s%s", URLDeBase, chaineRequete);
        } else {
            URL = String.format("%s%s", GlobalVar.servers[GlobalVar.SERVER], chaineRequete);
        }

        //GsonBuilder builder = new GsonBuilder();
        //Gson gson = builder.create();
        try {
            /*
            http://bancoregtest.com/textmsg/ajoutertexte
            Data: {"identifiant":"Rodney", "passe":"moderable2020", "texte":"Un petit message tout tranquille","id":1}
             */
            params.put("identifiant", identifiant);
            params.put("passe", passe);
            //Les paramètres facultatives
            if (idMsg > 0) {
                params.put("id", idMsg);
            }
            if (textMsg != null) {
                params.put("texte", textMsg);
            }
            if (chaineRequete != null) {
                params.put("chaineRequete", chaineRequete);
            }
            if (nouveauPasse != null) {
                params.put("nouveaupasse", nouveauPasse);
            }
        } catch (Exception e) {
            return reponse[0];
        }
        //lancer une requête post asynchrone
        asyncHttpClient.post(URL, params, new JsonHttpResponseHandler() {
            @Override
            //Si la requête réussit, onSuccess sera invoqué
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                jsonResponse = response.toString();
                eventSuccessKliyanHTTPPosterMessage(identifiant, passe, nouveauPasse, jsonResponse);
                //listener dans ce cas est la classe Communication qui a implémenté l'interface
                //OnKliyanHTTPFini
                listener.apelRESTKliyanHTTPFini(jsonResponse);
            }

            @Override
            //Si la requête réussit, onSuccess sera invoqué
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                jsonResponse = response.toString();
                eventSuccessKliyanHTTPPosterMessage(identifiant, passe, nouveauPasse, jsonResponse);
                GlobalVar.httpSucces = true;
                //listener dans ce cas est la classe Communication qui a implémenté l'interface
                //OnKliyanHTTPFini
                listener.apelRESTKliyanHTTPFini(jsonResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                super.onSuccess(statusCode, headers, response);
                jsonResponse = response.toString();
                eventSuccessKliyanHTTPPosterMessage(identifiant, passe, nouveauPasse, jsonResponse);
                //listener dans ce cas est la classe Communication qui a implémenté l'interface
                //OnKliyanHTTPFini
                listener.apelRESTKliyanHTTPFini(response);
            }

            @Override
            //Si la requête échoue, onFailure sera invoqué
            public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable throwable) {
                super.onFailure(statusCode, headers, errorResponse, throwable);
                try {
                    if (GlobalVar.requeteType == GlobalVar.REQUETE_CHANGER_MOT_DE_PASSE) {
                        new AlertDialog.Builder(context)
                                .setTitle("Communication Serveur")
                                .setMessage("Utilisateur ou mot de passe incorrecte.")
                                .show();
                    } else if (GlobalVar.requeteType == GlobalVar.REQUETE_AJOUTER_MSG) {
                        new AlertDialog.Builder(context)
                                .setTitle("Communication Serveur")
                                .setMessage("Message non sauvegardé")
                                .show();
                    } else if (GlobalVar.requeteType == GlobalVar.REQUETE_MODIFIER_MESSAGE) {
                        new AlertDialog.Builder(context)
                                .setTitle("Communication Serveur")
                                .setMessage("Message non modifié")
                                .show();
                    } else if (GlobalVar.requeteType == GlobalVar.REQUETE_SUPPRIMER_MESSAGE) {
                        new AlertDialog.Builder(context)
                                .setTitle("Communication Serveur")
                                .setMessage("Message non effacé")
                                .show();
                    }
                    //listener dans ce cas est la classe Communication qui a implémenté l'interface
                    //OnKliyanHTTPFini
                    listener.apelRESTKliyanHTTPEchwe(statusCode, null);
                    GlobalVar.httpSucces = false;
                } catch (Exception e) {
                }
            }
        });
        return reponse[0];
    }

    public void eventSuccessKliyanHTTPPosterMessage(String username, String password, String newPassword, String response) {
        if (GlobalVar.requeteType == GlobalVar.REQUETE_CHANGER_MOT_DE_PASSE) {
            if (GlobalVar.firstConnexion) {
                FileText.createUser(context, username, password);
                Intent intention = new Intent(context, ActivityPrincipal.class);
                context.startActivity(intention);
                GlobalVar.ActivityLoginThis.finish();
            }else {
                FileText.UpdateUser(GlobalVar.ActivityLoginThis, username, password, password);
                GlobalVar.ActivityLoginThis.finish();
            }
        } else if (GlobalVar.requeteType == GlobalVar.REQUETE_AJOUTER_MSG) {
            new AlertDialog.Builder(context)
                    .setTitle("Communication Serveur")
                    .setMessage("Message sauvegardé")
                    .show();
        } else if (GlobalVar.requeteType == GlobalVar.REQUETE_MODIFIER_MESSAGE) {
            new AlertDialog.Builder(context)
                    .setTitle("Communication Serveur")
                    .setMessage("Message modifié")
                    .show();
        } else if (GlobalVar.requeteType == GlobalVar.REQUETE_SUPPRIMER_MESSAGE) {
            new AlertDialog.Builder(context)
                    .setTitle("Communication Serveur")
                    .setMessage("Message effacé")
                    .show();
        } else if (GlobalVar.requeteType == GlobalVar.REQUETE_LISTER_ID_MESSAGES){
            //----------------------------------------------------
            try {
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<TextMessage>>() {
                }.getType();
                ArrayList<TextMessage> arrayTexte = gson.fromJson(jsonResponse, type);

                for (TextMessage t : arrayTexte) {
                        GlobalVar.identMessage = t.msgid;
                }
            } catch (Exception e) { }
            //----------------------------------------------------
        }
        else if (GlobalVar.requeteType == GlobalVar.REQUETE_LISTER_TOUT_MESSAGE){
            //----------------------------------------------------
            try {
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<TextMessage>>() {
                }.getType();
                GlobalVar.arrayListMessageSever = gson.fromJson(jsonResponse, type);

               /* for (TextMessage t : GlobalVar.arrayListMessageSever) {
                        System.out.print(t.message);
                } */
            } catch (Exception e) { }
            //----------------------------------------------------
        }
    }
}

