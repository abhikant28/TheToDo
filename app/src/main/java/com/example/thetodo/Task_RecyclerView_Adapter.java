package com.example.thetodo;

import static com.example.thetodo.MainActivity.viewModel;

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
import java.util.List;

public class Task_RecyclerView_Adapter extends RecyclerView.Adapter<Task_RecyclerView_Adapter.MyViewHolder> {
    private RecyclerViewClickListener listener;
    private List<Tasks> tasks = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private CheckBox cb_Tasks_completed;
        private TextView tv_type;
        private TextView tv_title;

        public MyViewHolder(@NonNull View view) {
            super(view);
            tv_type = view.findViewById(R.id.Main_Task_List_Item_Type);
            cb_Tasks_completed = view.findViewById(R.id.Main_Task_List_Item_checkButton);
            tv_title = view.findViewById(R.id.Main_Task_List_Item_Title);

            tv_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_type.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    tv_type.requestLayout();
                }
            });
            cb_Tasks_completed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Tasks task= tasks.get(getAdapterPosition());
                    if (b) {
                        tv_title.setPaintFlags(compoundButton.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        tv_title.setPaintFlags(0);
                        }
                    task.setCompleted(b);
                    viewModel.update(task);
                }
            });
        }
    }

    @NonNull
    @Override
    public Task_RecyclerView_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_task_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Task_RecyclerView_Adapter.MyViewHolder holder, int position) {
        Tasks task = tasks.get(position);

        holder.tv_title.setText(task.getTitle());
        holder.tv_type.setText(task.getType());
        holder.cb_Tasks_completed.setChecked(task.isCompleted());
        if (task.isCompleted()) {
            holder.tv_title.setPaintFlags(holder.cb_Tasks_completed.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }

    //    private int newPosition(int currentPosition){
//        int newPosition=currentPosition;
//
//        for (;newPosition>=0;newPosition--){
//            if(!MainActivity.myTasks.get(newPosition).isCompleted())
//            {break;}
//        }
//        return newPosition;
//    }
    public void setTasks(List<Tasks> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void OnItemClick(Tasks tasks);
    }

}
