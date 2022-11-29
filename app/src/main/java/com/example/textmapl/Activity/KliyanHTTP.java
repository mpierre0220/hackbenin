package com.example.textmapl.Activity;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/*import com.example.loteriemaplcomm.Aktivite.Service.Senkronizasyon;
import com.example.loteriemaplcomm.Aktivite.data.model.DoneEksepsyon;
import com.example.loteriemaplcomm.KontroleYo.DoneServer;
import com.example.loteriemaplcomm.KontroleYo.GlobalLotri;
import com.example.loteriemaplcomm.KontroleYo.PranEreKodYo; */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class KliyanHTTP {

    private static final String TAG = "TEXT_MAPL";

    AsyncHttpClient asyncHttpClient;
    SyncHttpClient syncHttpClient;
    RequestParams requestParams;
    OnKliyanHTTPFini listener;

    Context context;

    //public static String BASE_URL               = "https://bancoregalohaiti.com";
    public static String BASE_URL             = "http://bancoregtest.com";
    String jsonResponse;

    public KliyanHTTP(Context context, OnKliyanHTTPFini listener, String baseUrl) {
        asyncHttpClient = new AsyncHttpClient(true, 80, 443);
        syncHttpClient = new SyncHttpClient(true, 80, 443);
        asyncHttpClient.setTimeout(10000);
        syncHttpClient.setTimeout(10000);
        requestParams = new RequestParams();
        this.context = context;
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
                listener.apelRESTKliyanHTTPFini(response);
                //Log.i(TAG, "onSuccess: " + jsonResponse);
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                super.onFailure(statusCode, headers,  throwable, response);
                try {
                    listener.apelRESTKliyanHTTPEchwe(statusCode, null);
                } catch (Exception e) {
                }
            }
        });
    }

    public void KliyanHTTPPost(String queryTerm, String baseUrl) {
        //requestParams.put("s", queryTerm);
        String URL;
        if (!EstatiKoneksyon.isConnected(context)) {
            listener.apelRESTKliyanHTTPEchwe(-1, null);
        }
        if (baseUrl != null) {
            URL = String.format("%s%s", baseUrl, queryTerm);
        } else {
            URL = String.format("%s%s", GlobalVar.servers[GlobalVar.SERVER], queryTerm);
        }
        asyncHttpClient.post(URL, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                jsonResponse = response.toString();
                listener.apelRESTKliyanHTTPFini(response);
                //Log.i(TAG, "onSuccess: " + jsonResponse);
            }
           @Override
           public void onFailure(int statusCode, Header[] headers,  Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers,  throwable, errorResponse);
                try {
                    listener.apelRESTKliyanHTTPEchwe(statusCode, errorResponse.toString());
                } catch (Exception e) {
                }
                //Log.e(TAG, "onFailure: " + errorResponse);
            }
        });
    }
}

