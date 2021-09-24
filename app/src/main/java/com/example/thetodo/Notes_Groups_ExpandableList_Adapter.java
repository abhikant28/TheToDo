package com.example.thetodo;

import static com.example.thetodo.MainActivity.viewModel;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.thetodo.AppObjects.Groups;
import com.example.thetodo.AppObjects.Notes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Notes_Groups_ExpandableList_Adapter extends BaseExpandableListAdapter {

    Context cxt;
    private AdapterView.OnItemClickListener listener;
    static List<Groups> groupsList = new ArrayList<>();
    static HashMap<Integer, List<Notes>> groupMap=new HashMap<>();

    public Notes_Groups_ExpandableList_Adapter(Context cxt) {
        this.cxt = cxt;
    }


    @Override
    public int getGroupCount() {
        return groupMap.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return groupMap.get(groupsList.get(i).getG_id()).size();
    }

    @Override
    public Object getGroup(int i) {
        return groupMap.get(i);
    }

    @Override
    public Notes getChild(int groupPosition, int childPosition) {
        return groupMap.get(groupsList.get(groupPosition).getG_id()).get(childPosition);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) cxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.main_recycler_super_list_item_group, null);
        }
        TextView tv_grpName = view.findViewById(R.id.Main_Super_TextView_List_Item_GroupName);
        tv_grpName.setText(groupsList.get(groupPosition).getTitle());
        TextView tv_tapToAddNote = view.findViewById(R.id.Main_Super_RecyclerView_List_Item_TapToAdd);

        tv_tapToAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cxt, EditNote.class);
                intent.putExtra("isNew", true);
                intent.putExtra("g_id", groupsList.get(groupPosition).getG_id());
                cxt.startActivity(intent);
//                overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
            }
        });
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) cxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.main_super_list_item_note, null);
        }
        TextView tv_date = view.findViewById(R.id.Main_RecyclerView_List_Item_Time);
        TextView tv_title = view.findViewById(R.id.Main_TextView_List_Item_Title);
        TextView tv_desc= view.findViewById(R.id.Main_RecyclerView_List_Item_Sub);
        Notes note = getChild(groupPosition, childPosition);
        Log.i("NOTE:::",note.getTitle());
        tv_title.setText(note.getTitle());
        tv_date.setText(note.getDate());
        tv_desc.setText(note.getBody());

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int i) {

    }

    @Override
    public void onGroupCollapsed(int i) {

    }

    @Override
    public long getCombinedChildId(long l, long l1) {
        return l;
    }

    @Override
    public long getCombinedGroupId(long l) {
        return l;
    }

    public void setList(List<Groups> groups) {
        Log.i("NEW LIST :::","Updated....");
        groupsList = groups;
        if(groupMap!=null){
            groupMap.clear();
        }
        for (Groups i : groups) {
            List gNote= viewModel.getGroupNotes(i.getG_id());
            groupMap.put(i.getG_id(), gNote);
        }
        Log.i("NEW LIST:::::",groupMap.toString());
        notifyDataSetChanged();
    }
}