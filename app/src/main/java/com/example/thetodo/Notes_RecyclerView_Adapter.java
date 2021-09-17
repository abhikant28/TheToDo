package com.example.thetodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thetodo.AppObjects.Notes;
import com.example.thetodo.DataBase.TheViewModel;

import java.util.ArrayList;
import java.util.List;

public class Notes_RecyclerView_Adapter extends RecyclerView.Adapter<Notes_RecyclerView_Adapter.MyViewHolder> {
    private List<Notes> notes = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_super_list_item_note, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Notes_RecyclerView_Adapter.MyViewHolder holder, int position) {
        String title = notes.get(position).getTitle();
        String about = notes.get(position).getBody();
        String date = notes.get(position).getDate();

        if (title.length()== 0) {
            holder.tv_title.setText(about);
            holder.tv_full.setText("");
        }else{
            holder.tv_title.setText(title);
            holder.tv_full.setText(about);
        }

        holder.tv_date.setText(date);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;
        private TextView tv_full;
        private TextView tv_date;
        private LinearLayout ll_main;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_main = itemView.findViewById(R.id.Main_RecyclerView_List_Item_LinearLayout1);
            tv_date = itemView.findViewById(R.id.Main_RecyclerView_List_Item_Time);
            tv_title = itemView.findViewById(R.id.Main_RecyclerView_List_Item_Title);
            tv_full = itemView.findViewById(R.id.Main_RecyclerView_List_Item_Sub);

            ll_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.OnItemClick(notes.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

    public void setNotes(List<Notes> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void OnItemClick(Notes note);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
