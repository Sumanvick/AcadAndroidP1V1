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
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class todayFragment extends Fragment {

    private RecyclerView todayTaskList;
    private VivzAdapter adapter;
    private List<TaskInfo> fragmentData;
    private TextView todayFragmentNoData;

    public todayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_today, container, false);
        rootView.setTag("todayFragment");
        todayTaskList = (RecyclerView) rootView.findViewById(R.id.todayTaskList);
        todayFragmentNoData = (TextView) rootView.findViewById(R.id.todayFragmentNoData);
//        fragmentData = getData(getActivity());
//        if (fragmentData.size() <= 0) {
//            todayFragmentNoData.setVisibility(View.VISIBLE);
//        } else {
//            todayFragmentNoData.setVisibility(View.GONE);
//            adapter = new VivzAdapter(getActivity(), fragmentData);
//            todayTaskList.setAdapter(adapter);
//            //registerForContextMenu(todayTaskList);
//            todayTaskList.setLayoutManager(new LinearLayoutManager(getActivity()));
//        }
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getActivity(),"Today Frgament onResume() Called.",Toast.LENGTH_SHORT).show();
        fragmentData = getData(getActivity());
        if (fragmentData.size() <= 0) {
            todayFragmentNoData.setVisibility(View.VISIBLE);
        } else {
            todayFragmentNoData.setVisibility(View.GONE);
            adapter = new VivzAdapter(getActivity(), fragmentData);
            todayTaskList.setAdapter(adapter);
            //registerForContextMenu(todayTaskList);
            todayTaskList.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Call");
        menu.add(0, v.getId(), 0, "SMS");
    }*/

    /*@Override
     public boolean onContextItemSelected(MenuItem item) {
        //AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if(item.getTitle().toString().equals("Update Task")){
            Toast.makeText(getActivity(), "You Clicked On Update Task ", Toast.LENGTH_SHORT).show();
        }else if(item.getTitle().toString().equals("Delete Task")){
            Toast.makeText(getActivity(),"You Clicked On Delete Task ",Toast.LENGTH_SHORT).show();
        }else{
            return false;
        }
        return true;
    }*/

    public static List<TaskInfo> getData(Context ctx) {
        List<TaskInfo> data = new ArrayList<>();
        DBTaskToDo dbTask = new DBTaskToDo(ctx);
        try {
            dbTask.open();
            Cursor cursor = dbTask.getTodaysTasks();
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    TaskInfo currentTask = new TaskInfo(cursor.getLong(0), cursor.getString(1), cursor.getString(2),
                            cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7),cursor.getString(8));
                    data.add(currentTask);
                } while (cursor.moveToNext());
            } else {
                return data;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

}
