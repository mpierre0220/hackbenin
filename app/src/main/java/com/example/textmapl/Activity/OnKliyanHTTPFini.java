package com.example.textmapl.Activity;

import org.json.JSONObject;

public interface OnKliyanHTTPFini {
    public void apelRESTKliyanHTTPFini(JSONObject json);
    public void apelRESTKliyanHTTPEchwe(int statusCode, String message);
}

