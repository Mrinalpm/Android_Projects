package com.example.stopwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button buttonStartStop; //Start/Stop button
    private Button reset; //Reset button

    private TextView mainDisplay; //TextView to display

    //variable to keep track of when the start and stop buttons are clicked
    private long startTime;

    //If stop has been clicked and reset hasn't, this amount gets carried over when start gets clicked again
    private long carryOverTime;

    private boolean stopped; //state defining whether stopwatch has stopped or not

    Runnable thread = new Runnable() { //the thread that will handle running and display of the stopwatch
        @Override
        public void run() {

            if(!stopped) { //check to see that it isn't in a stopped state
                long milliseconds = System.currentTimeMillis() - startTime; //elapsed milliseconds
                long seconds = (milliseconds / 1000 ); //elapsed seconds
                long minutes =  seconds / 60; //elapsed minutes
                long hours = minutes / 60; //elapsed hours

                //continuously update display
                mainDisplay.setText(hours //hours
                        + ":" + String.format("%02d", (minutes%60)) //minutes
                        + ":" + String.format("%02d", (seconds%60)) //seconds
                        + ":" + String.format("%02d", ((milliseconds%1000)/10))); //milliseconds

                new Handler().postDelayed(this, 0); //add this runnable to the queue again
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    /*
    This method initializes all methods and program variables
    */
    private void initialize() {
        //buttons
        buttonStartStop = findViewById(R.id.buttonStartStop);
        reset = findViewById(R.id.buttonReset);

        //display
        mainDisplay = findViewById(R.id.mainDisplay);

        stopped = true; //initially state is stopped

        //all variables keeping track are initially zero
        startTime = 0;
        carryOverTime = 0;

        buttonStartStop.setOnClickListener(new View.OnClickListener() { //start/stop button
            @Override
            public void onClick(View v) {
                if (stopped) { //in stopped state, want to start
                    start();
                    startTime = System.currentTimeMillis() - carryOverTime ;
                    new Handler().postDelayed(thread, 0); //add runnable to the queue to start the stopwatch
                } else { //in running state, want to stop
                    stop();
                    carryOverTime = System.currentTimeMillis() - startTime;
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() { //reset button
            @Override
            public void onClick(View v) {
                stop();
                mainDisplay.setText("0:00:00:00");
                startTime = 0;
                carryOverTime = 0;
            }
        });
    }

    /*
    Method execution when the start button is pressed
    */
    private void start() {
        if(buttonStartStop != null) {
            stopped = false;
            buttonStartStop.setText("STOP");
        }
    }

    /*
    Method execution when the stop button is pressed
    */
    private void stop() {
        if(buttonStartStop != null) {
            stopped = true;
            buttonStartStop.setText("START");
        }
    }
}


