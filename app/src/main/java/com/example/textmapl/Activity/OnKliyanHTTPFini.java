package com.example.textmapl.Activity;

import org.json.JSONObject;
/*
    Interface pour les requetes HTTP
 */
public interface OnKliyanHTTPFini {
    public void apelRESTKliyanHTTPFini(String json);
    public void apelRESTKliyanHTTPEchwe(int statusCode, String message);
}

