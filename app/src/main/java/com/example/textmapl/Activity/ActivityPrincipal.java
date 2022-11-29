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

import java.util.ArrayList;
import java.util.List;

public class ActivityPrincipal extends AppCompatActivity implements SearchView.OnQueryTextListener, RecyclerViewClickInterface {
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<MessageText> arrayListMessage;
    TextView teksTit, teksMesaj, boutonNo, boutonYes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        setTitle("Texte Message");

        recyclerView = (RecyclerView) findViewById(R.id.id_recyclerTxt);
        arrayListMessage = FileText.teksEnfoList(this);
        //----------------------------------------------------------------
        for (MessageText m : arrayListMessage){
            Log.e("info " + arrayListMessage.indexOf(m)+1, " " + m.getTeks());
            //Toast.makeText(getApplicationContext(),"tèks " + (arrayListMessage.indexOf(m)+1) + " : " + m.getTeks(), Toast.LENGTH_LONG).show();
        }
        //----------------------------------------------------------------


        //===================================== Swipe recyclerView ====================================
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

    // Metòd pou modifye yon tèks
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
        openLayout_mesaj_dyalog();
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
        return super.onOptionsItemSelected(item);
    }

    public void openLayout_mesaj_dyalog() {
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
                // envoie du message
                //LotteryNumber.arrayListGame.clear();
                //GlobalLotri.addBoulLis = false;
                alertDialog.dismiss();
                finish();
                startActivity(getIntent());
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}