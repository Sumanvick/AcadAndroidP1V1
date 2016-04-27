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
public class upcomingFragment extends Fragment {

    RecyclerView upcomingTaskList;
    private VivzAdapter adapter;

    public upcomingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_upcoming, container, false);
        rootView.setTag("upcomingFragment");
        upcomingTaskList = (RecyclerView) rootView.findViewById(R.id.upcomingTaskList);

        TextView upcomingFragmentNoData = (TextView) rootView.findViewById(R.id.upcomingFragmentNoData);
        List<TaskInfo> fragmentData;
        fragmentData = getData(getActivity());
        if (fragmentData.size() <= 0) {
            upcomingFragmentNoData.setVisibility(View.VISIBLE);
        } else {
            upcomingFragmentNoData.setVisibility(View.GONE);
            adapter = new VivzAdapter(getActivity(), fragmentData);
            upcomingTaskList.setAdapter(adapter);
            upcomingTaskList.setLayoutManager(new LinearLayoutManager(getActivity()));
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
            Cursor cursor = dbTask.getUpcomingTasks();
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
