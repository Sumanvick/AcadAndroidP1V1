package com.android.vicky.taskalarm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

public class TaskView extends AppCompatActivity {
    android.support.v7.widget.Toolbar toolbar;
    private DBTaskToDo db;
    private long row_ID;
    private int[] taskIcons ={R.drawable.alarm_icon_128n, R.drawable.msg_icon_128n, R.drawable.speech_icon_128n};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            row_ID = Long.parseLong(extras.getString("row_ID"));
            Log.d("Task View Intent Get : ", "onReceive: " + extras.getString("row_ID"));
        }
        setTaskViewItems(this, row_ID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_task_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.delete_task) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View dialogView = inflater.inflate(R.layout.task_dialog, null);
            final AlertDialog alert = new AlertDialog.Builder(this).create();
            Button dialog_yes = (Button) dialogView.findViewById(R.id.dialog_yes);
            Button dialog_no = (Button) dialogView.findViewById(R.id.dialog_no);

            dialog_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        db.open();
                        db.deleteTask(row_ID);
                        db.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    finish();
                    Intent startMainActivity = new Intent(TaskView.this, MainActivity.class);
                    startActivity(startMainActivity);
                    Toast.makeText(TaskView.this, "Task is deleted", Toast.LENGTH_SHORT).show();
                }
            });

            dialog_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });

            alert.setTitle("Are you sure ?");
            //alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alert.setView(dialogView);
            alert.show();
            return true;
        }else if (id == R.id.edit_task) {
            Intent gotoAddActivity = new Intent(this, AddTask.class);
            gotoAddActivity.putExtra("row_ID", row_ID);
            startActivity(gotoAddActivity);
            //Toast.makeText(this,"Edit is called",Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void setTaskViewItems(Context context, long row_ID){
        db = new DBTaskToDo(context);
        try {
            db.open();
            Cursor cursor = db.getSingleTask(row_ID);
            String taskTitle, taskDescription, taskType, taskStatus, TaskSendNo,taskDate,taskTime,taskPriority;

            taskTitle = cursor.getString(1);
            taskDescription = cursor.getString(2);
            taskType = cursor.getString(3);
            taskDate = cursor.getString(4);
            taskTime = cursor.getString(5);
            taskStatus = cursor.getString(6);
            TaskSendNo = cursor.getString(7);
            taskPriority = cursor.getString(8);

            TextView txtTaskTitle = (TextView) findViewById(R.id.txtViewTaskTitle);
            TextView txtTaskDescription = (TextView) findViewById(R.id.txtViewTaskDescription);
            TextView txtTaskType = (TextView) findViewById(R.id.txtViewTaskType);
            ImageView txtTaskIcon = (ImageView) findViewById(R.id.txtViewTaskIcon);
            TextView txtTaskTime = (TextView) findViewById(R.id.txtViewTaskTime);
            TextView txtTaskDate = (TextView) findViewById(R.id.txtViewTaskDate);
            TextView txtTaskPriority = (TextView) findViewById(R.id.txtViewPriority);
            TextView txtTaskStatus = (TextView) findViewById(R.id.txtViewTaskStatus);
            TextView txtTaskSendTo = (TextView) findViewById(R.id.txtViewTaskSentTo);
            TextView txtAmPm = (TextView) findViewById(R.id.txtViewAmPm);
            RelativeLayout viewSendToGroup = (RelativeLayout) findViewById(R.id.viewSendToGroup);


            setTitle(taskTitle);
            txtTaskTitle.setText(taskTitle);
            txtTaskDescription.setText(taskDescription);
            txtTaskType.setText(taskType);
            txtTaskSendTo.setText(TaskSendNo);

            String[] splitTime = taskTime.split(" ");
            txtTaskTime.setText(splitTime[0]);
            txtAmPm.setText(splitTime[1]);
            txtTaskDate.setText(MakeDateTimePattern.setDatePattern(taskDate));
            txtTaskPriority.setText(taskPriority);

            if (taskStatus.equals("on")){
                txtTaskStatus.setText("Active");
                txtTaskStatus.setTextColor(getResources().getColor(R.color.color_time_active));
            }else if(taskStatus.equals("complete")){
                txtTaskStatus.setText("Completed");
                txtTaskStatus.setTextColor(getResources().getColor(R.color.color_completed));
            }else{
                txtTaskStatus.setText("Inactive");
                txtTaskStatus.setTextColor(getResources().getColor(R.color.colorHint));
            }

            if(TaskSendNo.equals("")){
                viewSendToGroup.setVisibility(View.GONE);
            }else{
                viewSendToGroup.setVisibility(View.VISIBLE);
                txtTaskSendTo.setText(TaskSendNo);
            }

            switch (taskType) {
                case "Alarm":
                    txtTaskIcon.setImageResource(taskIcons[0]);
                    break;
                case "Send Task":
                    txtTaskIcon.setImageResource(taskIcons[1]);
                    break;
                case "Speech text":
                    txtTaskIcon.setImageResource(taskIcons[2]);
                    break;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*public void showConfirmationDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.task_dialog, null);
        final AlertDialog alert = new AlertDialog.Builder(this).create();
        Button dialog_yes = (Button) dialogView.findViewById(R.id.dialog_yes);
        Button dialog_no = (Button) dialogView.findViewById(R.id.dialog_no);
        dialog_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        dialog_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        alert.setTitle("Are you sure ?");
        alert.setView(dialogView);
        alert.show();
    }*/
}
