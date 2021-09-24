package com.example.thetodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thetodo.AppObjects.Notes;

public class Notes_RecyclerView_Adapter extends ListAdapter<Notes,Notes_RecyclerView_Adapter.MyViewHolder> {
    private OnItemClickListener listener;
    private  static final DiffUtil.ItemCallback<Notes> DIFF_CALLBACK_NOTES = new DiffUtil.ItemCallback<Notes>() {
        @Override
        public boolean areItemsTheSame(@NonNull Notes oldItem, @NonNull Notes newItem) {
            return oldItem.getN_id()==newItem.getN_id();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Notes oldItem, @NonNull Notes newItem) {
            return oldItem.getBody().equals(newItem.getBody()) && oldItem.getTitle().equals(newItem.getTitle());
        }
    };

    public Notes_RecyclerView_Adapter() {
        super(DIFF_CALLBACK_NOTES);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_super_list_item_note, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Notes_RecyclerView_Adapter.MyViewHolder holder, int position) {
        String title = getItem(position).getTitle();
        String about = getItem(position).getBody();
        String date = getItem(position).getDate();

        if (title.length()== 0) {
            holder.tv_title.setText(about);
            holder.tv_full.setText("");
        }else{
            holder.tv_title.setText(title);
            holder.tv_full.setText(about);
        }

        holder.tv_date.setText(date);
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
            tv_title = itemView.findViewById(R.id.Main_TextView_List_Item_Title);
            tv_full = itemView.findViewById(R.id.Main_RecyclerView_List_Item_Sub);

            ll_main.setOnClickListener(new View.OnClickListener() {
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
        void OnItemClick(Notes note);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
