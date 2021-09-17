package com.example.thetodo.dialogBoxes;

import static com.example.thetodo.MainActivity.viewModel;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.thetodo.AppObjects.Groups;
import com.example.thetodo.MainActivity;
import com.example.thetodo.R;

public class TaskRepeat_Dialog extends AppCompatDialogFragment {

    private Spinner s_repeatAfter;
    private String repeat="Once";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.main_task_repeat_dialog, null);
        s_repeatAfter = v.findViewById(R.id.Main_Task_repeat_Spinner);

        s_repeatAfter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                switch(s_repeatAfter.getItemIdAtPosition(i)){
//                    case R.array.repeat_options.once:
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                repeat="Once";
            }
        });
        builder.setView(v)
                .setTitle("Repeat Task")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            MainActivity.theTask.setType(repeat);
                    }
                });
        return builder.create();
    }
}
