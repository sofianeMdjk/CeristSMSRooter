package com.example.ceristsmsrooter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;


public class EmailFetching extends Activity implements View.OnClickListener {
    Button stopFetchingButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_fetching);

        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        new MailReader(FormActivity.getUsername(),FormActivity.getPassword()
                                ,FormActivity.getHost(),FormActivity.getIncomingMail(),FormActivity.getSubject()
                                ,FormActivity.context).execute();

                    }
                });
            }
        };
        timer.schedule(task, 0, 60000); //it executes this every 1minute you can change the time as you desire
        stopFetchingButton=(Button)this.findViewById(R.id.EmailStopFetch);
        stopFetchingButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_email_fetching, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        System.exit(1);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        this.finish();
    }
}
