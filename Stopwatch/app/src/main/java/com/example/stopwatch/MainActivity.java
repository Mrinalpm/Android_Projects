package com.example.stopwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button buttonStartStop; //Start/Stop button

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

        mainDisplay = findViewById(R.id.mainDisplay);

        stopped = true; //initially state is stopped

        buttonStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stopped) { //stopped
                    ((Button) v).setText("STOP");
                } else { //running
                    ((Button) v).setText("START");
                }
                stopped = !stopped;
            }
        });
    }
}


