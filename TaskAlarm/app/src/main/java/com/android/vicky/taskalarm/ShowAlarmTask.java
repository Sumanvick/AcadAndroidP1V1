package com.android.vicky.taskalarm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowAlarmTask extends AppCompatActivity {
    private String taskTitle, taskDescription;
    long rowID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_alarm_task);


        //        Run the Activity when the screen is locked
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            rowID = extras.getLong("row_ID");
            taskTitle = extras.getString("TaskTitle");
            taskDescription = extras.getString("TaskDescription");
            Log.d("Intent Get TaskTitle : ", "onReceive: " + taskTitle);
            Log.d("TaskDescription : ", "onReceive: " + taskDescription);
        }

        TextView txtViewTask = (TextView) findViewById(R.id.viewTask);
        TextView txtViewTaskDetail = (TextView) findViewById(R.id.viewTaskInDetail);
        final ImageView imgExit = (ImageView) findViewById(R.id.exitFrom);

        txtViewTask.setText(taskTitle);
        txtViewTaskDetail.setText(taskDescription);

        imgExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgExit.setImageResource(R.drawable.exit_icon_black_128);
                SetAlarmIntent.cancelAlarm(ShowAlarmTask.this, rowID);
                finish();
            }
        });
    }
}
