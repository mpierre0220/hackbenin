package com.example.textmapl.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.textmapl.R;

/**
 * Created by MAPL COMMUNICATION on 11/06/2022.
 */
/*
    Classe permettant d'avoir l'accès directement avec les design representatifs des textes messages
    venant du serveur pour la classe RecyclerViewAdapterServer
 */
public final class RecyclerViewHolderServer extends RecyclerView.ViewHolder {
    TextView textMessage;
    ImageView imageColorText;

    public RecyclerViewHolderServer(View itemView) {
        super(itemView);

        textMessage = (TextView) itemView.findViewById(R.id.idCardV_text_server);
        imageColorText = (ImageView) itemView.findViewById(R.id.idCardV_imageView_text_server);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //RecyclerViewAdapter.recyclerViewClickInterface.onItemClick(getAdapterPosition());
            }
        });
    }
}
