package com.example.thetodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thetodo.AppObjects.Notes;

import java.util.ArrayList;

public class Notes_RecyclerView_Adapter extends RecyclerView.Adapter<Notes_RecyclerView_Adapter.MyViewHolder> {
    private Notes_RecyclerView_Adapter.RecyclerViewClickListener listener;

    public Notes_RecyclerView_Adapter(ArrayList<Notes> allNotes, RecyclerViewClickListener allNotes_listener) {
        this.listener=allNotes_listener;
    }

    @NonNull
    @Override
    public Notes_RecyclerView_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.main_super_list_item_note, parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Notes_RecyclerView_Adapter.MyViewHolder holder, int position) {
        String title= MainActivity.allNotes.get(position).getTitle();
        String about=MainActivity.allNotes.get(position).getBody();
        String date=MainActivity.allNotes.get(position).getDate();

        if(title.length()!=0){
            holder.tv_title.setText(title);
        }
        holder.tv_full.setText(about);
        holder.tv_date.setText(date);
    }

    @Override
    public int getItemCount() {
        return MainActivity.allNotes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tv_title;
        private TextView tv_full;
        private TextView tv_date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date=itemView.findViewById(R.id.Main_RecyclerView_List_Item_Time);
            tv_title=itemView.findViewById(R.id.Main_RecyclerView_List_Item_Title);
            tv_full=itemView.findViewById(R.id.Main_RecyclerView_List_Item_Sub);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }
}
