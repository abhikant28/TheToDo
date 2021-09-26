package com.example.thetodo.dialogBoxes;

import static com.example.thetodo.MainActivity.viewModel;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.thetodo.AlertReceiver;
import com.example.thetodo.AppObjects.Groups;
import com.example.thetodo.AppObjects.Tasks;
import com.example.thetodo.R;

public class Tasks_Delete_Dialog extends AppCompatDialogFragment {

    private CheckBox cb_never;
    private Tasks task;
    Context cxt;

    public Tasks_Delete_Dialog(Tasks task) {
        this.task=task;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.main_delete_tasks, null);

        cb_never=v.findViewById(R.id.Main_Task_Delete_CheckBox);

        builder.setView(v)
                .setTitle("Delete Task")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        viewModel.delete(task);
                        if(task.getDate()!=null){
                            Intent intent = new Intent(cxt.getApplicationContext(), AlertReceiver.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(cxt, task.getT_id(), intent, 0);
                            AlarmManager alarmManager = (AlarmManager) cxt.getSystemService(Context.ALARM_SERVICE);
                            alarmManager.cancel(pendingIntent);
                        }
                    }
                });
        return builder.create();
    }
}
