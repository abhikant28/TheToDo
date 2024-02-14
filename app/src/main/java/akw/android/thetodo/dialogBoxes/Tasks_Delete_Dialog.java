package akw.android.thetodo.dialogBoxes;

import static akw.android.thetodo.MainActivity.viewModel;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.work.WorkManager;

import java.util.UUID;

import akw.android.thetodo.AppObjects.Tasks;
import akw.android.thetodo.R;

public class Tasks_Delete_Dialog extends AppCompatDialogFragment {

    private CheckBox cb_never;
    private Tasks task;

    public Tasks_Delete_Dialog(Tasks task) {
        this.task = task;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.main_delete_tasks, null);

        cb_never = v.findViewById(R.id.Main_Task_Delete_CheckBox);

        builder.setView(v)
                .setTitle("Delete Task")
                .setNegativeButton("Cancel", (dialogInterface, i) -> {

                })
                .setPositiveButton("Delete", (dialogInterface, i) -> {
                    if (task.getNextDate() != null) {

                        WorkManager.getInstance(getActivity().getApplicationContext()).cancelWorkById(UUID.fromString(task.getWorkerID()));
                        viewModel.delete(task);

                    }
                });
        return builder.create();
    }
}
