package com.example.textmapl.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import com.example.textmapl.File.FileText;
import com.example.textmapl.R;
import com.example.textmapl.server.Communication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.StringTokenizer;
/*
    Classe d'activité pour la gestion de connexion d'utilisateur
 */
public class Login extends AppCompatActivity {
    private EditText userName, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (GlobalVar.firstConnexion){
            setTitle("");
        }else {
            setTitle("Changement de connexion");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        GlobalVar.ActivityLoginThis = this;
        userName = (EditText) findViewById(R.id.id_username);
        password = findViewById(R.id.id_password);
    }

    //methode action sur le bouton connecter
    public void btn_login(View view) {
        if (userName.getText().toString().isEmpty())
            userName.setError("Veuillez saisir le mom d'utilisateur");
        else if (password.getText().toString().isEmpty())
            password.setError("Veuillez saisir le mot de passe");
        else {
            GlobalVar.userConnected = userName.getText().toString();
            GlobalVar.passwordConnected = password.getText().toString();
            Communication communication = new Communication(this);
            communication.changerMotDePasse(userName.getText().toString(),password.getText().toString(), password.getText().toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                password.setText(null);
                if (GlobalVar.requeteType == GlobalVar.REQUETE_CHANGER_MOT_DE_PASSE){
                    new AlertDialog.Builder(this)
                            .setTitle("Communication Serveur")
                            .setMessage("Mot de passe modifié")
                            .show();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // idantifyan ti flèch la ki nan meni an
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (GlobalVar.firstConnexion) {
            finish();
            startActivity(getIntent());
        }else {
            finish();
        }
    }
}