package com.example.textmapl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.textmapl.Interface.RecyclerViewClickInterface;
import com.example.textmapl.R;
import com.example.textmapl.modal.MessageText;

import java.util.ArrayList;

/**
 * Created by MAPL COMMUNICATION on 12/13/2020.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{
    Context context;
    ArrayList<MessageText> arrayList;
    ColorGenerator mgeratorColor = ColorGenerator.DEFAULT;
    TextDrawable mDrawableConstructor;

    public static RecyclerViewClickInterface recyclerViewClickInterface;

    public RecyclerViewAdapter(Context context, ArrayList arrayList, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.context = context;
        this.arrayList = arrayList;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_view, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        MessageText arLisText = arrayList.get(position);

        holder.textMessage.setText(arLisText.getTeks());
        //getImageTitleColorTextMessage(holder, arLisText.getTeks());
    }

    public void getImageTitleColorTextMessage(RecyclerViewHolder holder, String textM){
        String titre = "A";

        if (textM != null && !textM.isEmpty()){
            titre = textM.substring(0,1).toUpperCase() + "" + textM.substring(1,2).toUpperCase();
        }else
            titre = "UKNOW";

        int couleur = mgeratorColor.getRandomColor();

        // Creation d'un icon avec lien du color
        mDrawableConstructor = TextDrawable.builder()
                .buildRound(titre, couleur);
        holder.imageColorText.setImageDrawable(mDrawableConstructor);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
