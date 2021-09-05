package com.example.thetodo;

import android.graphics.Paint;
import android.icu.text.DateTimePatternGenerator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class task_RecyclerView_Adapter extends RecyclerView.Adapter<task_RecyclerView_Adapter.MyViewHolder> {
    private RecyclerViewClickListener listener;

    public task_RecyclerView_Adapter(ArrayList<Tasks> tasks, RecyclerViewClickListener listener){
        this.listener= listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private RadioButton rb_Tasks_completed;
        private TextView tv_type;

        public MyViewHolder(@NonNull View view) {
            super(view);
            tv_type = view.findViewById(R.id.Main_Task_List_Item_Type);
            rb_Tasks_completed= view.findViewById(R.id.Main_Task_List_Item_radioButton);

            rb_Tasks_completed.setOnClickListener(new View.OnClickListener() {
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
                        compoundButton.setPaintFlags(compoundButton.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        try {
                            TimeUnit.SECONDS.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }finally {
                            MainActivity.myTasks.add(MainActivity.myTasks.get(getAdapterPosition()));
                            MainActivity.myTasks.remove(getAdapterPosition());
                        }
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
    public task_RecyclerView_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.main_task_list_item, parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull task_RecyclerView_Adapter.MyViewHolder holder, int position) {
    String Title = MainActivity.myTasks.get(position).getTitle();
    String type= MainActivity.myTasks.get(position).getType();
    boolean completed = MainActivity.myTasks.get(position).isCompleted();

    holder.rb_Tasks_completed.setText(Title);
    holder.tv_type.setText(type);
    holder.rb_Tasks_completed.setChecked(completed);
        if(completed) {
            holder.rb_Tasks_completed.setPaintFlags(holder.rb_Tasks_completed.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    @Override
    public int getItemCount() {
        return MainActivity.myTasks.size();
    }
    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }
}
