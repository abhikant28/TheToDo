package com.example.thetodo;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thetodo.AppObjects.Tasks;

import java.util.ArrayList;

public class Task_RecyclerView_Adapter extends RecyclerView.Adapter<Task_RecyclerView_Adapter.MyViewHolder> {
    private RecyclerViewClickListener listener;

    public Task_RecyclerView_Adapter(ArrayList<Tasks> tasks, RecyclerViewClickListener listener){
        this.listener= listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private CheckBox rb_Tasks_completed;
        private TextView tv_type;
        private TextView tv_title;

        public MyViewHolder(@NonNull View view) {
            super(view);
            tv_type = view.findViewById(R.id.Main_Task_List_Item_Type);
            rb_Tasks_completed= view.findViewById(R.id.Main_Task_List_Item_checkButton);
            tv_title=view.findViewById(R.id.Main_Task_List_Item_Title);

            tv_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_type.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    tv_type.requestLayout();
                }
            });
            rb_Tasks_completed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        MainActivity.myTasks.get(getAdapterPosition()).setCompleted(true);
                        tv_title.setPaintFlags(compoundButton.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        MainActivity.myTasks.add(MainActivity.myTasks.get(getAdapterPosition()));
                        MainActivity.myTasks.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                    }else{
                        MainActivity.myTasks.get(getAdapterPosition()).setCompleted(false);
                        tv_title.setPaintFlags(0);
                        MainActivity.myTasks.add(newPosition(getAdapterPosition()),MainActivity.myTasks.get(getAdapterPosition()));
                        MainActivity.myTasks.remove(getAdapterPosition());
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public Task_RecyclerView_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.main_task_list_item, parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Task_RecyclerView_Adapter.MyViewHolder holder, int position) {
    String Title = MainActivity.myTasks.get(position).getTitle();
    String type= MainActivity.myTasks.get(position).getType();
    boolean completed = MainActivity.myTasks.get(position).isCompleted();

    holder.tv_title.setText(Title);
    holder.tv_type.setText(type);
    holder.rb_Tasks_completed.setChecked(completed);
        if(completed) {
            holder.tv_title.setPaintFlags(holder.rb_Tasks_completed.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    @Override
    public int getItemCount() {
        return MainActivity.myTasks.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }

    private int newPosition(int currentPosition){
        int newPosition=currentPosition;

        for (;newPosition>=0;newPosition--){
            if(!MainActivity.myTasks.get(newPosition).isCompleted())
            {break;}
        }
        return newPosition;
    }
}
