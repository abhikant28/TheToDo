package akw.android.thetodo.Adapter;

import static android.content.Context.MODE_PRIVATE;
import static akw.android.thetodo.MainActivity.viewModel;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;


import akw.android.thetodo.AppObjects.Notes;
import akw.android.thetodo.Keys;
import akw.android.thetodo.R;
import akw.android.thetodo.dialogBoxes.Notes_Delete_Dialog;

public class Notes_RecyclerView_Adapter extends ListAdapter<Notes, Notes_RecyclerView_Adapter.MyViewHolder> {
    private static final DiffUtil.ItemCallback<Notes> DIFF_CALLBACK_NOTES = new DiffUtil.ItemCallback<Notes>() {
        @Override
        public boolean areItemsTheSame(@NonNull Notes oldItem, @NonNull Notes newItem) {
            return oldItem.getN_id() == newItem.getN_id();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Notes oldItem, @NonNull Notes newItem) {
            return oldItem.getBody().equals(newItem.getBody()) && oldItem.getTitle().equals(newItem.getTitle());
        }
    };
    private OnItemClickListener listener;

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

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

        if (title.length() == 0) {
            Spanned spanned= Html.fromHtml(about);
            holder.tv_title.setText(spanned);
            holder.tv_full.setText("");
        } else {
            holder.tv_title.setText(title);
            holder.tv_full.setText(about);
        }

        holder.tv_date.setText(date);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void showPopup(View v, int pos) {
        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
        popupMenu.inflate(R.menu.long_press_menu_notes);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.Notes_Option_Delete:
                        deleteDialogBox(getItem(pos), v.getContext());
                        return true;

                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    private void deleteDialogBox(Notes note, Context cxt) {
        SharedPreferences sharedPreferences = cxt.getSharedPreferences(Keys.SHARED_PREFS_NAME, MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("NEVER_REMIND_NOTES_DELETE", false)) {
            Notes_Delete_Dialog deleteDialog = new Notes_Delete_Dialog(note);
            deleteDialog.show(((FragmentActivity) cxt).getSupportFragmentManager(), "Delete Note");
            return;
        }
        viewModel.delete(note);
    }

    public interface OnItemClickListener {
        void OnItemClick(Notes note);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title,tv_full,tv_date;
        private LinearLayout ll_main;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_main = itemView.findViewById(R.id.Main_RecyclerView_List_Item_LinearLayout1);
            tv_date = itemView.findViewById(R.id.Main_RecyclerView_List_Item_Time);
            tv_title = itemView.findViewById(R.id.Main_TextView_List_Item_Title);
            tv_full = itemView.findViewById(R.id.Main_RecyclerView_List_Item_Sub);

            ll_main.setOnLongClickListener(view -> {
                //Log.i("LONG:::","CLICKED");
                showPopup(view, getAdapterPosition());
                return true;
            });
            ll_main.setOnClickListener(view -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.OnItemClick(getItem(getAdapterPosition()));
                }
            });
        }
    }


}
