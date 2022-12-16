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
    Classe permettant d'avoir l'accès directement avec les design representatifs des textes messages internes
    pour la classe RecyclerViewAdapter
 */
public final class RecyclerViewHolder extends RecyclerView.ViewHolder {
    TextView textMessage;
    ImageView imageColorText;

    public RecyclerViewHolder(View itemView) {
        super(itemView);

        textMessage = (TextView) itemView.findViewById(R.id.idCardV_text);
        imageColorText = (ImageView) itemView.findViewById(R.id.idCardV_imageView_text);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerViewAdapter.recyclerViewClickInterface.onItemClick(getAdapterPosition());
            }
        });
    }
}
