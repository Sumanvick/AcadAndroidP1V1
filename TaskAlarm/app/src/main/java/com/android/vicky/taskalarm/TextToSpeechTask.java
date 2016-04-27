package com.android.vicky.taskalarm;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class TextToSpeechTask extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private TextToSpeech tts;
    private String taskTitle, taskDescription;
    long rowID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speech_task);

      //  Run the Activity when the screen is locked
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);


        tts = new TextToSpeech(this, this);

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

        speakOut();

        imgExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgExit.setImageResource(R.drawable.exit_icon_black_128);
                SetAlarmIntent.cancelAlarm(TextToSpeechTask.this,rowID);
                finish();
            }
        });
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                speakOut();
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void speakOut() {
        String text = "Hi. the event "+taskTitle+" is started. And the event in detail. "+taskDescription+".";
        text = text+text+". Thank you. Have a great day.";
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}
