package com.example.thetodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Notes_Groups_RecyclerView_Adapter extends RecyclerView.Adapter<Notes_Groups_RecyclerView_Adapter.MyViewHolder> {
    private Notes_Groups_RecyclerView_Adapter.RecyclerViewClickListener listener;

    public Notes_Groups_RecyclerView_Adapter(ArrayList<Groups> allNotes, RecyclerViewClickListener myGroups_listener) {
        this.listener=myGroups_listener;
    }

    @NonNull
    @Override
    public Notes_Groups_RecyclerView_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycler_super_list_item_group, parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Notes_Groups_RecyclerView_Adapter.MyViewHolder holder, int position) {
        String title= MainActivity.myGroups.get(position).getTitle();
        String date=MainActivity.myGroups.get(position).getDate();
        ArrayList<Notes> groupNotes= MainActivity.getGroupNotes(MainActivity.myGroups.get(position).getG_id());

        holder.tv_date.setText(date);
        holder.tv_title.setText(title);

    }

    @Override
    public int getItemCount() {
        return MainActivity.myGroups.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tv_title;
        private TextView tv_date;
        private RecyclerView rv_notes;
        private TextView tv_tapToAddNote;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rv_notes=itemView.findViewById(R.id.Main_Super_RecyclerView_List_Item_List);
            tv_date=itemView.findViewById(R.id.Main_Super_RecyclerView_List_Item_GroupDate);
            tv_title=itemView.findViewById(R.id.Main_Super_RecyclerView_List_Item_GroupName);
            tv_tapToAddNote=itemView.findViewById(R.id.Main_Super_RecyclerView_List_Item_TapToAdd);
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
