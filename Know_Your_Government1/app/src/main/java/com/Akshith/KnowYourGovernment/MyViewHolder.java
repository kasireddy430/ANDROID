package com.Akshith.KnowYourGovernment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


public class MyViewHolder extends RecyclerView.ViewHolder{
    public TextView office;
    public TextView nameAndParty;

    public MyViewHolder(View view) {
        super(view);
        office =  view.findViewById(R.id.location);
        nameAndParty =  view.findViewById(R.id.nameAndParty);
    }
}
