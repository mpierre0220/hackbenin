package com.example.textmapl.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.textmapl.Adapter.RecyclerViewAdapter;
import com.example.textmapl.Adapter.RecyclerViewAdapterServer;
import com.example.textmapl.R;
import com.example.textmapl.modal.MessageText;
import com.example.textmapl.server.Communication;
import com.example.textmapl.server.TextMessage;

import java.util.ArrayList;
 /*
    Classe permettant d'afficher les messages textes venant du serveur
  */
public class ActivityAllMessageServer extends AppCompatActivity implements SearchView.OnQueryTextListener {
    RecyclerView recyclerViewServer;
    Communication communication;
    RecyclerViewAdapterServer recyclerViewAdapterServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_message_server);
        setTitle("Messages du Serveur");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        communication = new Communication(this);

        recyclerViewServer = (RecyclerView) findViewById(R.id.id_recyclerTxt_server);
        // Appel de la methode pour afficher les messages du serveur
        this.setRecyclerViewServerMessage(GlobalVar.arrayListMessageSever);
    }

     // Methode pour afficher les messages du serveur dans le recyclerView
    public void setRecyclerViewServerMessage(ArrayList<TextMessage> arrayList) {
        if (arrayList == null) {
            Toast.makeText(this, "La liste est est vide...", Toast.LENGTH_LONG).show();
        } else {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
            recyclerViewServer.setLayoutManager(layoutManager);
            recyclerViewAdapterServer = new RecyclerViewAdapterServer(this, arrayList);
            recyclerViewServer.setAdapter(recyclerViewAdapterServer);
        }
    }

    // Gestion de menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_text_server_search, menu);
        MenuItem menuItem = menu.findItem(R.id.id_text_server_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    // gestion selection de menu
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
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

     // gestion de  des textes dans le menu
    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<TextMessage> newList = new ArrayList<TextMessage>();

        for (TextMessage mst : GlobalVar.arrayListMessageSever){
            String infoSearch = mst.message.toLowerCase();
            if (infoSearch.contains(newText)){
                newList.add(mst);
            }
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerViewServer.setLayoutManager(layoutManager);
        recyclerViewAdapterServer = new RecyclerViewAdapterServer(this, newList);
        recyclerViewServer.setAdapter(recyclerViewAdapterServer);
        return true;
    }
}