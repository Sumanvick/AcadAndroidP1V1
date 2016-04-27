package com.android.vicky.taskalarm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.sql.SQLException;

/**
 * Created by Vicky on 3/22/2016.
 */


public class AlarmReciever extends BroadcastReceiver {
    private DBTaskToDo db;
    private long row_ID;
    private Cursor cursor;
    private final int[] icons = {R.drawable.alarm_icon_64n, R.drawable.msg_icon_64n, R.drawable.speech_icon_64n};

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();
        if (extras != null) {
            row_ID = Long.parseLong(extras.getString("row_ID"));
            Log.d("Intent Get : ", "onReceive: " + row_ID);
        }

        db = new DBTaskToDo(context);
        try {
            db.open();
            cursor = db.getSingleTask(row_ID);
            String taskTitle, taskDescription, taskType, taskStatus, TaskSendNo,taskPriority;

            taskTitle = cursor.getString(1);
            taskDescription = cursor.getString(2);
            taskType = cursor.getString(3);
            taskStatus = cursor.getString(6);
            TaskSendNo = cursor.getString(7);
            taskPriority = cursor.getString(8);

            if (taskStatus.equals("on")) {
                switch (taskType) {
                    case "Alarm":
                        setAlarmIntent(context, row_ID, 0, taskTitle, taskDescription, taskPriority);
                        break;
                    case "Send Task":
                        setSendTaskIntent(context, row_ID, 1, taskTitle, taskDescription, TaskSendNo, taskPriority);
                        break;
                    case "Speech text":
                        setSpeechTextIntent(context, row_ID, 2, taskTitle, taskDescription, taskPriority);
                        break;
                }
                if(db.updateTaskStatus(row_ID,"complete")){}
            }
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setAlarmIntent(Context context, long row_ID, int taskIcon, String taskTitle, String taskDescription, String taskPriority) {
        /* Need to create a alarm activity and call that activity by intent*/

        Intent crntIntent = new Intent(context, ShowAlarmTask.class);
        crntIntent.putExtra("TaskTitle", ""+taskTitle);
        crntIntent.putExtra("TaskDescription", "" + taskDescription);
        crntIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(crntIntent);

        String desCriptionExtra = "\nPriority " + taskPriority;
        makeNotification(context, row_ID, taskTitle, taskDescription + desCriptionExtra, taskIcon);
        makeRingTone(context);
        Toast.makeText(context, "The " + taskTitle + " event is started", Toast.LENGTH_LONG).show();

    }

    private void setSendTaskIntent(Context context, long row_ID, int taskIcon, String taskTitle, String taskDescription, String sendPhNum, String taskPriority) {
        /* Need to include sms intent*/
        try {
            /*To send task as a sms*/
            SmsManager smsManager = SmsManager.getDefault();
            String smsMsg = "Task : " + taskTitle + "\nDescription : " + taskDescription + "\nPriority : " + taskPriority;
            smsManager.sendTextMessage(sendPhNum, null, smsMsg, null, null);

            String desCriptionExtra = "\nSent to " + sendPhNum + "\nPriority " + taskPriority;
            makeNotification(context, row_ID, taskTitle, taskDescription + desCriptionExtra, taskIcon);
            makeRingTone(context);
            Toast.makeText(context, taskTitle + " sent successfully", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(context, "Failed to send sms", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void setSpeechTextIntent(Context context, long row_ID, int taskIcon, String taskTitle, String taskDescription, String taskPriority) {
        /* Need to create a speech to text service activity and call that activity by intent*/

        Intent crntIntent = new Intent(context, TextToSpeechTask.class);
        crntIntent.putExtra("row_ID", row_ID);
        crntIntent.putExtra("TaskTitle", taskTitle);
        crntIntent.putExtra("TaskDescription", taskDescription);
        crntIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(crntIntent);

        String desCriptionExtra = "\nPriority " + taskPriority;
        makeNotification(context, row_ID, taskTitle, taskDescription + desCriptionExtra, taskIcon);
        //makeRingTone(context);
        Toast.makeText(context, "The " + taskTitle + " event is started", Toast.LENGTH_LONG).show();
    }

    private void makeNotification(Context context, long row_ID, String taskTitle, String taskDescription, int taskIcon) {
        NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, TaskView.class);
        notificationIntent.putExtra("row_ID", ""+row_ID);
        Log.d("MakeNotification row_ID", ""+row_ID);
        PendingIntent contentIntent = PendingIntent.getActivity(context, (int)row_ID,
                notificationIntent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(icons[taskIcon])
                        .setContentTitle(taskTitle)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(taskDescription))
                        .setContentText(taskDescription);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify((int) row_ID, mBuilder.build());
    }

    private void makeRingTone(Context context) {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();
    }
}

