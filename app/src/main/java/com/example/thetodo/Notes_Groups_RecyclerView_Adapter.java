package com.example.thetodo;

import static androidx.core.content.ContextCompat.startActivity;
import static com.example.thetodo.MainActivity.viewModel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thetodo.AppObjects.Groups;
import com.example.thetodo.AppObjects.Notes;

import java.util.ArrayList;
import java.util.List;

public class Notes_Groups_RecyclerView_Adapter extends ListAdapter<Groups,Notes_Groups_RecyclerView_Adapter.MyViewHolder> {

    private Notes_Groups_RecyclerView_Adapter.OnItemClickListener listener;
    Context cxt;
    private static final DiffUtil.ItemCallback<Groups> DIFF_CALLBACK_NOTES_GROUPS = new DiffUtil.ItemCallback<Groups>() {
        @Override
        public boolean areItemsTheSame(@NonNull Groups oldItem, @NonNull Groups newItem) {
            return oldItem.getG_id()== newItem.getG_id();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Groups oldItem, @NonNull Groups newItem) {
            return false;
        }
    };

    public Notes_Groups_RecyclerView_Adapter() {
        super(DIFF_CALLBACK_NOTES_GROUPS);
    }


    @NonNull
    @Override
    public Notes_Groups_RecyclerView_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycler_super_list_item_group, parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Notes_Groups_RecyclerView_Adapter.MyViewHolder holder, int position) {
        String title= getItem(position).getTitle();
        String date=getItem(position).getDate();
        List<Notes> groupNotes=new ArrayList<>();

        holder.tv_date.setText(date);
        holder.tv_title.setText(title);
        groupNotes= viewModel.getGroupNotes(getItem(position).getG_id());

        Notes_RecyclerView_Adapter adapter = new Notes_RecyclerView_Adapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(cxt);
        holder.rv_notes.setLayoutManager(layoutManager);
        holder.rv_notes.setItemAnimator(new DefaultItemAnimator());
        holder.rv_notes.setAdapter(adapter);
        adapter.submitList(groupNotes);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
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
            tv_tapToAddNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.OnItemClick(getItem(getAdapterPosition()));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(Groups group);
    }

    public void setOnItemClickListener(Notes_Groups_RecyclerView_Adapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
