package com.example.textmapl.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.textmapl.File.FileText;
import com.example.textmapl.R;
import com.example.textmapl.server.Communication;
/*
    Classe d'activité permettant de faire des modifications de textes messages
 */
public class EdithTextMessage extends AppCompatActivity {
    EditText textMessage;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edith_text_message);
        setTitle("Modifier Texte Message");
        textMessage = findViewById(R.id.text_message_edith);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String teksMes = intent.getStringExtra("teksMesaj");
        textMessage.setText(teksMes.replace("\t", "\n"));
        id = intent.getIntExtra("id", 0);
    }

    public void onClickBtnEdith(View view) {
        if(textMessage.getText().toString().isEmpty()){
            textMessage.setError("Champs vide!");
        }else {
            boolean res = FileText.opdetTeks(this, id, textMessage.getText().toString());
            if (res){
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Succès")
                        .setMessage("Message Modifié")
                        .show();

                new CountDownTimer(2000, 1000) {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        setResult(RESULT_OK);
                        finish();
                    }
                }.start();
            }else{
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Attention!")
                        .setMessage("Enregistrement échoué")
                        .show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}