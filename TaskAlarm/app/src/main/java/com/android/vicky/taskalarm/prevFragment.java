package com.android.vicky.taskalarm;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class prevFragment extends Fragment {

    RecyclerView prevTaskList;
    private VivzAdapter adapter;

    public prevFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_prev, container, false);
        rootView.setTag("prevFragment");
        prevTaskList = (RecyclerView) rootView.findViewById(R.id.prevTaskList);

        TextView prevFragmentNoData = (TextView) rootView.findViewById(R.id.previousFragmentNoData);
        List<TaskInfo> fragmentData;
        fragmentData = getData(getActivity());
        if (fragmentData.size() <= 0) {
            prevFragmentNoData.setVisibility(View.VISIBLE);
        } else {
            prevFragmentNoData.setVisibility(View.GONE);
            adapter = new VivzAdapter(getActivity(), fragmentData);
            prevTaskList.setAdapter(adapter);
            prevTaskList.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static List<TaskInfo> getData(Context ctx){
        List<TaskInfo> data = new ArrayList<>();
        DBTaskToDo dbTask = new DBTaskToDo(ctx);
        try{
            dbTask.open();
            Cursor cursor = dbTask.getPreviousTasks();
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                do {
                    TaskInfo currentTask = new TaskInfo(cursor.getLong(0),cursor.getString(1),cursor.getString(2),
                            cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8));
                    data.add(currentTask);
                }while (cursor.moveToNext());
            }else{
                return data;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }
}
