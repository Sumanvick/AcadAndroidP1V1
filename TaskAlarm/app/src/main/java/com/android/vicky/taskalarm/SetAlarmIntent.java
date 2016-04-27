package com.android.vicky.taskalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Vicky on 3/22/2016.
 */
public class SetAlarmIntent {
    public static void setAlarm(Context context,long rowID, long time) {
        Intent intentAlarmReciever = new Intent(context, AlarmReciever.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        intentAlarmReciever.putExtra("row_ID", String.valueOf(rowID));
        alarmManager.set(AlarmManager.RTC_WAKEUP, time,
                PendingIntent.getBroadcast(context, (int) rowID, intentAlarmReciever, PendingIntent.FLAG_UPDATE_CURRENT)
        );
        //Toast.makeText(context, "event is active", Toast.LENGTH_SHORT).show();
        Log.d("Alarm Set", "RowID : " + (int) rowID);
    }
    public static void cancelAlarm(Context context,long rowID) {
        Intent intentAlarmReciever = new Intent(context, AlarmReciever.class);
        //AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        intentAlarmReciever.putExtra("row_ID", String.valueOf(rowID));
        PendingIntent alarmIntent;
        alarmIntent = PendingIntent.getBroadcast(context,(int)rowID, intentAlarmReciever, PendingIntent.FLAG_CANCEL_CURRENT);
        //Toast.makeText(context, "event is inactive", Toast.LENGTH_SHORT).show();
        Log.d("Alarm Cancel", "RowID : " + (int)rowID);
    }
}
