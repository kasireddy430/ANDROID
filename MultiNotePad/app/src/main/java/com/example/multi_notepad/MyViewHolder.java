package com.example.multi_notepad;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView title;
    TextView date;
    TextView description;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.textView4);
        date = itemView.findViewById(R.id.textView5);
        description = itemView.findViewById(R.id.textView6);
    }
}
