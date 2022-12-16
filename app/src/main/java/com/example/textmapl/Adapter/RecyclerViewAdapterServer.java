package com.example.textmapl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.textmapl.Interface.RecyclerViewClickInterface;
import com.example.textmapl.R;
import com.example.textmapl.modal.MessageText;
import com.example.textmapl.server.TextMessage;

import java.util.ArrayList;

/**
 * Created by MAPL COMMUNICATION on 12/13/2020.
 */

/*
    Classe permettant d'adapter les textes venant du serveur dans un recyclerView
 */
public class RecyclerViewAdapterServer extends RecyclerView.Adapter<RecyclerViewHolderServer>{
    Context context;
    ArrayList<TextMessage> arrayList;

    public RecyclerViewAdapterServer(Context context, ArrayList arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerViewHolderServer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_view_for_server, parent, false);
        return new RecyclerViewHolderServer(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolderServer holder, int position) {
        TextMessage arLisText = arrayList.get(position);
        holder.textMessage.setText(arLisText.message);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
