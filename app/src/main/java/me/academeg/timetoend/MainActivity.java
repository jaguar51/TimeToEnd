package me.academeg.timetoend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;


public class MainActivity extends ActionBarActivity {
    private CheckBox vibrateCheckBox;

    private Intent serviceIntent;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vibrateCheckBox = (CheckBox) findViewById(R.id.vibrateCheckBox);
        vibrateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveSettings(isChecked);
            }
        });
        loadSettings();
        serviceIntent = new Intent(this, TimeService.class);
    }

    public void onClickStartService(View v) {
        startService(serviceIntent);
    }

    public void onClickStopService(View v) {
        stopService(serviceIntent);
    }

    public void saveSettings(boolean checked) {
        preferences = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("vibrateOn", checked);
        editor.apply();
    }

    public void loadSettings() {
        preferences = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isOnVibrate = preferences.getBoolean("vibrateOn", false);
        vibrateCheckBox.setChecked(isOnVibrate);
    }
}
