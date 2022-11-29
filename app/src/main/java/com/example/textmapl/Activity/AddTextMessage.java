package com.example.textmapl.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.textmapl.File.FileText;
import com.example.textmapl.R;

public class AddTextMessage extends AppCompatActivity {
    EditText textMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_text_message);
        setTitle("Texte Message");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textMessage = findViewById(R.id.text_message);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickBtnSave(View view) {
        if(textMessage.getText().toString().isEmpty()){
            textMessage.setError("Champs vide!");
        }else {
            boolean res = FileText.saveText(this, textMessage.getText().toString());
            if (res){
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Succès")
                        .setMessage("Message sauvegardé")
                        .show();

                new CountDownTimer(1500, 1000) {
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
                        .setMessage("Enregistrement échoué. Problème technique")
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