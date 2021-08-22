package com.example.multi_notepad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MultiAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<Multi> multiList;
    private MainActivity mainAct;

    MultiAdapter(List<Multi> mltList, MainActivity ma){
        this.multiList = mltList;
        this.mainAct = ma;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.multi_entry,parent,false);
        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Multi m = multiList.get(position);
            String description = m.getDescription();
            int size = description.length();
            holder.title.setText(m.getTitle());
            holder.date.setText(m.getDate());
            if(size>80){
                description = description.substring(0,80)+"...";
                holder.description.setText(description);
            }
            else{
                holder.description.setText(m.getDescription());
            }
    }

    @Override
    public int getItemCount() {
        return multiList.size();
    }
}
