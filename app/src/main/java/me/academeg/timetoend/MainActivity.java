package me.academeg.timetoend;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serviceIntent = new Intent(this, TimeService.class);
    }

    public void onClickStartService(View v) {
        startService(serviceIntent);
    }

    public void onClickStopService(View v) {
        stopService(serviceIntent);
    }

}
