package com.example.textmapl.Activity;

import org.json.JSONObject;

public interface OnKliyanHTTPFini {
    public void apelRESTKliyanHTTPFini(JSONObject json);

    public void apelRESTKliyanHTTPEchwe(int statusCode, String message);

    public void apelRESTKliyanHTTPFini(JSONObject message, boolean tiraj);

    public void KliyanHTTPEchweNanMandeTiraj(int statusCode, String response);

    public void KliyanHTTPFiniFemen(JSONObject json);

    public void KliyanHTTPEchweNanFemenTiraj(int statusCode, String response);
}

