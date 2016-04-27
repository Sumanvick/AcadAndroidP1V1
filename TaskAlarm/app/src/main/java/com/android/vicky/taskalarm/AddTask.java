package com.android.vicky.taskalarm;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTask extends AppCompatActivity {
    android.support.v7.widget.Toolbar toolbar;
    private LinearLayout lstTimePicker, lstDatePicker;
    private TextView edtTimePicker, edtDatePicker, edtSeekBar;
    private EditText edtTaskTitle, edtTaskDetails, edtSendTextPhoneNumber, edtSpeechText;
    private RadioGroup notificationType;
    private RadioButton radioAlarm, radioSendText, radioSpeechText;
    private ToggleButton toggleOnOff;
    private SeekBar rateBar;
    private long taskRowID = 0;
    private DBTaskToDo db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            taskRowID = extras.getLong("row_ID");
            Log.d("Add task Intent Get : ", "onReceive: " + extras.getString("row_ID"));
        }

        db = new DBTaskToDo(this);

        lstDatePicker = (LinearLayout) findViewById(R.id.lstDatePicker);
        lstTimePicker = (LinearLayout) findViewById(R.id.lstTimePicker);
        rateBar = (SeekBar) findViewById(R.id.volume_bar);
        toggleOnOff = (ToggleButton) findViewById(R.id.toggleOnOff);
        radioAlarm = (RadioButton) findViewById(R.id.radioNormalAlarm);
        radioSendText = (RadioButton) findViewById(R.id.radioMsg);
        radioSpeechText = (RadioButton) findViewById(R.id.radioSpchToText);

        //Form Fields.........
        edtTaskTitle = (EditText) findViewById(R.id.edtTaskTitle);
        edtTaskDetails = (EditText) findViewById(R.id.edtTaskDescription);
        edtSeekBar = (TextView) findViewById(R.id.valSeekbar);
        edtTimePicker = (TextView) findViewById(R.id.edtTimePicker);
        edtDatePicker = (TextView) findViewById(R.id.edtDatePicker);
        notificationType = (RadioGroup) findViewById(R.id.notificationType);

        edtSendTextPhoneNumber = (EditText) findViewById(R.id.edtSendTaskContactNo);
//        edtSpeechText = (EditText) findViewById(R.id.edtSpeechText);

        //Event Handling.........

        lstDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        lstTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        rateBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress;
                String finalProgressValue = (progressChanged < 10) ? "0" + progressChanged : "" + progressChanged;
                edtSeekBar.setText(finalProgressValue);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(AddTask.this, "seek bar progress:" + progressChanged,Toast.LENGTH_SHORT).show();
            }
        });

        radioAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSendTextPhoneNumber.setVisibility(View.GONE);
//                edtSpeechText.setVisibility(View.GONE);
            }
        });

        radioSendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                edtSpeechText.setVisibility(View.GONE);
                edtSendTextPhoneNumber.setVisibility(View.VISIBLE);
            }
        });

        radioSpeechText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSendTextPhoneNumber.setVisibility(View.GONE);
//                edtSpeechText.setVisibility(View.VISIBLE);
            }
        });

        if (taskRowID > 0) {
            setTitle("Update Task");
            setTaskItems(taskRowID);
        } else {
            setTitle("New Task");
        }

        toggleOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(AddTask.this, "U Clicked On : "+toggleOnOff.isChecked(),Toast.LENGTH_SHORT).show();
                taskSaveUpdate();

            }
        });


    }

    public void taskSaveUpdate() {
        String taskTitle, taskDescription, taskType, taskDate, taskTime, taskPriority, taskStatus;
        String taskPhNum = "";

        if (edtTaskTitle.getText().toString().equals("")) {
            Toast.makeText(AddTask.this, "Please give task title", Toast.LENGTH_SHORT).show();
            toggleOnOff.setChecked(false);
            return;
        }
        if (edtDatePicker.getText().toString().equals("")) {
            Toast.makeText(AddTask.this, "Please select date", Toast.LENGTH_SHORT).show();
            toggleOnOff.setChecked(false);
            return;
        }
        if (edtTimePicker.getText().toString().equals("")) {
            Toast.makeText(AddTask.this, "Please select time", Toast.LENGTH_SHORT).show();
            toggleOnOff.setChecked(false);
            return;
        }
        if (!edtDatePicker.getText().toString().equals("") && !edtTimePicker.getText().toString().equals("")) {
            if (!checkValidDate(MakeDateTimePattern.unsetDatePattern(edtDatePicker.getText().toString()), edtTimePicker.getText().toString())) {
                Toast.makeText(AddTask.this, "Please select valid date and time", Toast.LENGTH_SHORT).show();
                toggleOnOff.setChecked(false);
                return;
            }
        }
        if (radioSendText.isChecked()) {
            if (edtSendTextPhoneNumber.getText().toString().equals("")) {
                Toast.makeText(AddTask.this, "Please give Phone number", Toast.LENGTH_SHORT).show();
                toggleOnOff.setChecked(false);
                return;
            } else {
                taskPhNum = edtSendTextPhoneNumber.getText().toString();
            }
        }

        int selectedTypeId = notificationType.getCheckedRadioButtonId();
        RadioButton r = (RadioButton) findViewById(selectedTypeId);

        taskType = r.getText().toString();
        taskTitle = edtTaskTitle.getText().toString();
        taskDescription = edtTaskDetails.getText().toString();
        taskTime = edtTimePicker.getText().toString();
        taskDate = MakeDateTimePattern.unsetDatePattern(edtDatePicker.getText().toString());
        taskPriority = edtSeekBar.getText().toString();
        taskPriority = (taskPriority.equals("")) ? "00" : taskPriority;

        if (toggleOnOff.isChecked()) taskStatus = "on";
        else taskStatus = "off";

        if (taskRowID == 0) {
            try {
                db.open();
                taskRowID = db.insertTask(taskTitle, taskType, taskDescription, taskDate, taskTime, taskPriority, taskStatus, taskPhNum);
                if (taskRowID > 0) {
                    Log.d("val of taskPriority", taskPriority);
                    Toast.makeText(AddTask.this, "Task is saved", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(AddTask.this, "Failed, Sql Error..", Toast.LENGTH_SHORT).show();
                db.close();
            } catch (SQLException ex) {
            }
        } else if (taskRowID > 0) {
            try {
                db.open();
                if (db.updateTask(taskRowID, taskTitle, taskType, taskDescription, taskDate, taskTime, taskPriority, taskStatus, taskPhNum)) {
                    Toast.makeText(AddTask.this, "Task is updated", Toast.LENGTH_SHORT).show();
                    Log.d("val of taskPriority", taskPriority);
                } else
                    Toast.makeText(AddTask.this, "Failed, Sql Error..", Toast.LENGTH_SHORT).show();
                db.close();
            } catch (SQLException ex) {
            }
        }
        if (taskRowID > 0) {
            if (taskStatus.equals("on")) {
                long time = convertDateTimeToMs(taskDate, taskTime);
                SetAlarmIntent.setAlarm(AddTask.this, taskRowID, time);
            } else {
                //long time = convertDateTimeToMs(taskDate, taskTime);
                SetAlarmIntent.cancelAlarm(AddTask.this, taskRowID);
            }
        }
    }

    public void setTaskItems(long rowID) {
        db = new DBTaskToDo(this);
        try {
            db.open();
            Cursor cursor = db.getSingleTask(rowID);
            String taskTitle, taskDescription, taskType, taskStatus, TaskSendNo, taskDate, taskTime, taskPriority;

            taskTitle = cursor.getString(1);
            taskDescription = cursor.getString(2);
            taskType = cursor.getString(3);
            taskDate = cursor.getString(4);
            taskTime = cursor.getString(5);
            taskStatus = cursor.getString(6);
            TaskSendNo = cursor.getString(7);
            taskPriority = cursor.getString(8);


            edtTaskTitle.setText(taskTitle);
            edtTaskDetails.setText(taskDescription);
            edtDatePicker.setText(MakeDateTimePattern.setDatePattern(taskDate));
            edtTimePicker.setText(taskTime);
            edtSendTextPhoneNumber.setText(TaskSendNo);
            edtSeekBar.setText(taskPriority);
            rateBar.setProgress(Integer.parseInt(taskPriority));

            switch (taskType) {
                case "Alarm":
                    radioAlarm.setChecked(true);
                    break;
                case "Send Task":
                    radioSendText.setChecked(true);
                    edtSendTextPhoneNumber.setVisibility(View.VISIBLE);
                    break;
                case "Speech text":
                    radioSpeechText.setChecked(true);
                    break;
            }

            if (taskStatus.equals("on")) {
                toggleOnOff.setChecked(true);
            } else {
                toggleOnOff.setChecked(false);
            }

            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkValidDate(String fldDate, String fldTime) {
        Date todayDate = new Date();
        Date fldRealDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
        Log.d("Today Date Format", sdf.format(todayDate));
        try {
            fldRealDate = sdf.parse(fldDate + " " + fldTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("Today date-time ", "" + todayDate.getTime());
        Log.d("Fld date-time ", "" + fldRealDate.getTime());
        if (sdf.format(todayDate).equals(sdf.format(fldRealDate))) {
            return true;
        } else if (todayDate.getTime() < fldRealDate.getTime()) {
            return true;
        } else
            return false;
    }

    public long convertDateTimeToMs(String date, String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
        String givenDateString = date + " " + time;
        long timeInMilliseconds = 0;
        try {
            Date mDate = sdf.parse(givenDateString);
            timeInMilliseconds = mDate.getTime();
            //Log.d("convertDateTimeToMs: ", "" + timeInMilliseconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }

    public void setTimeIntoTextView(int hourOfDay, int minute) {
        String finalHour, finalMinute, setAmPm;
        int convertHour;
        convertHour = (hourOfDay > 12) ? hourOfDay - 12 : hourOfDay;
        finalHour = (convertHour < 10) ? "0" + convertHour : "" + convertHour;
        finalMinute = (minute < 10) ? "0" + minute : "" + minute;
        setAmPm = (hourOfDay > 11) ? "pm" : "am";

        edtTimePicker.setText(finalHour + ":" + finalMinute + " " + setAmPm);
    }

    public void setDateIntoTextView(int year, int month, int day) {
        String finalMonth, finalDay;

        finalMonth = (month < 10) ? "0" + (month + 1) : "" + (month + 1);
        finalDay = (day < 10) ? "0" + day : "" + day;

        edtDatePicker.setText(MakeDateTimePattern.setDatePattern(year + "-" + finalMonth + "-" + finalDay));
    }

    public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            //Toast.makeText(getActivity(),"The Hour = "+ hourOfDay+" Minute = "+minute,Toast.LENGTH_SHORT).show();
            setTimeIntoTextView(hourOfDay, minute);
        }
    }

    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            //Toast.makeText(getActivity(),""+month,Toast.LENGTH_SHORT).show();
            setDateIntoTextView(year, month, day);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.save_task) {
            taskSaveUpdate();
            //finish();
            /*Intent backToMainActivity = new Intent(this, MainActivity.class);
            startActivity(backToMainActivity);*/
            return true;
        }

        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            //do your stuff
            Toast.makeText(AddTask.this,"Back Button Is Pressed...",Toast.LENGTH_LONG).show();
        }
        return super.onKeyDown(keyCode, event);
    }

}



