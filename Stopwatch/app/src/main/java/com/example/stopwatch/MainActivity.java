package com.example.stopwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button buttonStartStop; //Start/Stop button]
    private Button reset; //Reset button

    private TextView mainDisplay; //TextView to display

    private boolean stopped; //state defining whether stopwatch has stopped or not

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    private void initialize() {
        buttonStartStop = findViewById(R.id.buttonStartStop);
        reset = findViewById(R.id.buttonReset);

        mainDisplay = findViewById(R.id.mainDisplay);

        stopped = true; //initially state is stopped

        buttonStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stopped) { //in stopped state, want to start
                    start();
                } else { //in running state, want to stop
                    stop();
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });
    }

    private void start() {
        if(buttonStartStop != null) {
            stopped = false;
            buttonStartStop.setText("STOP");
        }
    }

    private void stop() {
        if(buttonStartStop != null) {
            stopped = true;
            buttonStartStop.setText("START");
        }
    }
}


