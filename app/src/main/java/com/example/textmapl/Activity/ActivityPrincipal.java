package com.example.textmapl.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.textmapl.Adapter.MySwipeHelper;
import com.example.textmapl.Adapter.RecyclerViewAdapter;
import com.example.textmapl.File.FileText;
import com.example.textmapl.Interface.MyButtonClickListener;
import com.example.textmapl.Interface.RecyclerViewClickInterface;
import com.example.textmapl.R;
import com.example.textmapl.modal.MessageText;
import com.example.textmapl.server.Communication;

import java.util.ArrayList;
import java.util.List;

/*
    (Classe d'activité) Classe principale du projet permettant l'affichage de tout les messages texte dans les fichier interne
    et d'autres activités comme le menu ainsi que le bouton d'ajout de texte
 */
public class ActivityPrincipal extends AppCompatActivity implements SearchView.OnQueryTextListener, RecyclerViewClickInterface {
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<MessageText> arrayListMessage;
    TextView teksTit, teksMesaj, boutonNo, boutonYes;
    Communication communication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        setTitle("Texte Message");
        //--------------------------------------------------
        boolean reponse = FileText.verifyIfExisteFileUser(this);
        if (!reponse){
            GlobalVar.firstConnexion = true;
            Intent it = new Intent(this, Login.class);
            startActivity(it);
            finish();
        }else {
            FileText.GetUserConnected(this);
        }
        //--------------------------------------------------

        setResult(RESULT_OK);
        GlobalVar.ActivityPrincipalThis = this;
        communication = new Communication(this);

        recyclerView = (RecyclerView) findViewById(R.id.id_recyclerTxt);
        arrayListMessage = FileText.teksEnfoList(this);
        //===================================== Swipe recyclerView ====================================
        //Appl de la classe MySwipeHelper pour la gestion de gestures de gauche à droite sur l'écran
        MySwipeHelper swipeHelper = new MySwipeHelper(this, recyclerView, 200) {
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buffer) {
                buffer.add(new MyButton(ActivityPrincipal.this,
                        "Efase",
                        35,
                        R.drawable.ic_delete_while_24,
                        Color.parseColor("#FF3c30"),
                        new MyButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                boolean res = FileText.deleteTeks(ActivityPrincipal.this, pos);

                                if (res){
                                    new AlertDialog.Builder(ActivityPrincipal.this)
                                            .setTitle("Suppression")
                                            .setMessage("Texte Message Suprimé")
                                            .show();

                                    new CountDownTimer(1500, 1000) {
                                        @RequiresApi(api = Build.VERSION_CODES.N)
                                        @Override
                                        public void onTick(long millisUntilFinished) {

                                        }

                                        @Override
                                        public void onFinish() {
                                            finish();
                                            startActivity(getIntent());
                                        }
                                    }.start();
                                }else{
                                    new AlertDialog.Builder(ActivityPrincipal.this)
                                            .setTitle("Attention!")
                                            .setMessage("Suppression échoué. Problème technique")
                                            .show();
                                }
                            }
                        }));

                buffer.add(new MyButton(ActivityPrincipal.this,
                        "Òpdet",
                        35,
                        R.drawable.ic_edit_white_24,
                        Color.parseColor("#FF9502"),
                        new MyButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                //=====================================================
                                String textMes = arrayListMessage.get(pos).getTeks();
                                updateText(pos, textMes); // apèl methode pou òpdet la
                                //============================================================
                            }
                        }));
            }
        };
        //=============================================================================================
        this.setRecyclerViewMessage(arrayListMessage);
    }

    public void setRecyclerViewMessage(ArrayList<MessageText> arrayList) {
        if (arrayList == null) {
            Toast.makeText(this, "La liste est est vide...", Toast.LENGTH_LONG).show();
        } else {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerViewAdapter = new RecyclerViewAdapter(this, arrayList, this);
            recyclerView.setAdapter(recyclerViewAdapter);
        }
    }

    // Methodes pour la modification des textes
    public void updateText(int id, String teksMesaj){
        Intent intent = new Intent(this, EdithTextMessage.class);
        intent.putExtra("id", id);
        intent.putExtra("teksMesaj", teksMesaj);
        startActivityForResult(intent, 1);
    }

    public void AddText_click(View view) {
        startActivityForResult(new Intent(this, AddTextMessage.class), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                arrayListMessage = FileText.teksEnfoList(this);
                this.setRecyclerViewMessage(arrayListMessage);
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<MessageText> newList = new ArrayList<MessageText>();

        for (MessageText mst : arrayListMessage){
            String infoSearch = mst.getTeks().toLowerCase();
            if (infoSearch.contains(newText)){
                newList.add(mst);
            }
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(this, newList, this);
        recyclerView.setAdapter(recyclerViewAdapter);
        return true;
    }

    @Override
    public void onItemClick(int position) {
        String texte = arrayListMessage.get(position).getTeks();
        openLayout_mesaj_dyalog(texte);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_text_search, menu);
        MenuItem menuItem = menu.findItem(R.id.id_text_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home: // idantifyan ti flèch la ki nan meni an

                return true;
            case R.id.id_messages_server:
                GlobalVar.firstConnexion = false;
                // Fonction ci-dessous permettant de lister tout les messages venant du serveur
                // dans cet arrayList : GlobalVar.arrayListMessageSever
                communication.listerToutMessage(GlobalVar.userConnected,GlobalVar.passwordConnected);

                new CountDownTimer(2500, 1000) { // Ici, un compter de 2 secondes et demie avant le lancement de cette classe : ActivityAllMessageServer.class
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        Intent it = new Intent(getApplicationContext(), ActivityAllMessageServer.class);
                        startActivity(it);
                    }
                }.start();
                return true;
            case R.id.id_update_user:
                GlobalVar.firstConnexion = false;
                intent = new Intent(this, Login.class);
                startActivityForResult(intent, 1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openLayout_mesaj_dyalog(String texteMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityPrincipal.this, androidx.appcompat.R.style.Theme_AppCompat_Dialog_Alert);
        View view = LayoutInflater.from(ActivityPrincipal.this).inflate(
                R.layout.layout_mesaj_dialog,
                (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        teksTit = (TextView) view.findViewById(R.id.teksTit);
        teksMesaj = (TextView) view.findViewById(R.id.teksMesaj);
        boutonNo = (TextView) view.findViewById(R.id.boutonNo);
        boutonYes = (TextView) view.findViewById(R.id.boutonYes);

        boutonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        boutonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int posNewText = arrayListMessage.size();
                communication.ajouterMessage(GlobalVar.userConnected, GlobalVar.passwordConnected, texteMessage, posNewText);
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(getIntent());
    }
}