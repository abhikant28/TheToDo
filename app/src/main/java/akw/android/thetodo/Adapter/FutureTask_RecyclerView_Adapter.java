package akw.android.thetodo.Adapter;

import static akw.android.thetodo.MainActivity.viewModel;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import akw.android.thetodo.AppObjects.Tasks;
import akw.android.thetodo.Keys;
import akw.android.thetodo.R;
import akw.android.thetodo.ReminderWorker;
import akw.android.thetodo.dialogBoxes.Tasks_Delete_Dialog;

public class FutureTask_RecyclerView_Adapter extends ListAdapter<Tasks, FutureTask_RecyclerView_Adapter.MyViewHolder> {
    private static final DiffUtil.ItemCallback<Tasks> DIFF_CALLBACK_TASKS = new DiffUtil.ItemCallback<Tasks>() {
        @Override
        public boolean areItemsTheSame(@NonNull Tasks oldItem, @NonNull Tasks newItem) {
            return oldItem.getT_id() == newItem.getT_id();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Tasks oldItem, @NonNull Tasks newItem) {
            return true;
        }
    };
    private RecyclerViewClickListener listener;
    private List<Tasks> selectedTasks = new ArrayList<>();
    private boolean selecting = false;

    public FutureTask_RecyclerView_Adapter() {
        super(DIFF_CALLBACK_TASKS);
    }

    @NonNull
    @Override
    public FutureTask_RecyclerView_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_task_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FutureTask_RecyclerView_Adapter.MyViewHolder holder, int position) {
        Tasks task = getItem(position);
        holder.tv_title.setText(task.getTitle());
        Calendar date = task.getNextDate();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, dd/MM/yy");
        String currentDate = sdf.format(date.getTime());
        Log.i("FuturAdapter.ViewBinder:::::",date.getTimeInMillis()+"");
        holder.tv_type.setText(currentDate);
        holder.cb_Tasks_completed.setChecked(task.isCompleted());
        if (task.isCompleted()) {
            holder.tv_title.setPaintFlags(holder.cb_Tasks_completed.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    public Tasks getTask(int position) {
        return getItem(position);
    }


    public void showPopup(View v, int pos) {
        //Log.i("LONG:::","CLICKED");
        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
        popupMenu.inflate(R.menu.long_press_menu_task);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.Task_MenuOption_Delete:

                    deleteDialogBox(getItem(pos), v.getContext());
                    return true;

                default:
                    return false;
            }
        });
        popupMenu.show();
    }

    public void setNextAlert(Tasks task, Context cxt) {
        Calendar date = task.getNextDate();

        switch (task.getType()) {
            case Keys.REPEAT_TYPE_MONTHLY:
                date= getDate(date,Calendar.MONTH);
                break;
            case Keys.REPEAT_TYPE_DAILY:
                date.add(Calendar.DATE, 1);
                break;
            case Keys.REPEAT_TYPE_YEARLY:
                date= getDate(date,Calendar.YEAR);
                break;
            case Keys.REPEAT_TYPE_WEEKLY:
                date.add(Calendar.DATE, 7);
        }

        task.setNextDate(date);
        viewModel.update(task);

        long initialDelay = date.getTimeInMillis()-  Calendar.getInstance().getTimeInMillis();
        OneTimeWorkRequest work = new
                OneTimeWorkRequest.Builder(ReminderWorker.class)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(cxt).enqueue(work);
        task.setWorkerID(work.getId().toString());

    }


    private Calendar getDate(Calendar date, int type) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR,23);
        cal.set(Calendar.MINUTE,59);
        cal.set(Calendar.SECOND,59);

        Calendar next;

        for(int i =1; i<100;i++){
            next= date;
            next.add(type,i);
            if(next.getTimeInMillis()>cal.getTimeInMillis()){
                return next;
            }
        }
        return null;

    }

    private void deleteDialogBox(Tasks task, Context cxt) {
        Tasks_Delete_Dialog deleteDialog = new Tasks_Delete_Dialog(task);
        deleteDialog.show(((FragmentActivity) cxt).getSupportFragmentManager(), "New Group");
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }

    public interface OnItemClickListener {
        void OnItemClick(Tasks tasks);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CheckBox cb_Tasks_completed;
        private TextView tv_type;
        private TextView tv_title;

        public MyViewHolder(@NonNull View view) {
            super(view);
            tv_type = view.findViewById(R.id.Main_Task_List_Item_Type);
            cb_Tasks_completed = view.findViewById(R.id.Main_Task_List_Item_checkButton);
            tv_title = view.findViewById(R.id.Main_Task_List_Item_Title);

            tv_title.setOnClickListener(view1 -> {
                tv_type.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                tv_type.requestLayout();
            });
            tv_title.setOnLongClickListener(view12 -> {
                showPopup(view12, getAdapterPosition());
                return true;
            });
            view.setOnLongClickListener(view13 -> {
                showPopup(view13, getAdapterPosition());
                return true;
            });
            cb_Tasks_completed.setOnCheckedChangeListener((compoundButton, b) -> {
                Tasks task = getItem(getAdapterPosition());
                if (b) {
                    tv_title.setPaintFlags(compoundButton.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    if (task.getNextDate() != null) {
                        setNextAlert(task, view.getContext());
                    }
                } else {
                    tv_title.setPaintFlags(0);
                }
                task.setCompleted(b);
                viewModel.update(task);
            });
        }
    }

}
