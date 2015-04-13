package me.academeg.timetoend;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.Pair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TimeService extends Service {
    // Constants
    private final int NOTIFICATION_ID = 1;


    private NotificationManager notificationManager;
    private BroadcastReceiver broadcastReceiver;
    private ArrayList<Pair<String, String>> timePare;



    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("ServiceLog", "Create");
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        timePare = new ArrayList<Pair<String, String>>();
        timePare.add(new Pair<String, String>("08:00", "09:20"));
        timePare.add(new Pair<String, String>("09:30", "10:50"));
        timePare.add(new Pair<String, String>("11:00", "12:20"));
        timePare.add(new Pair<String, String>("12:40", "14:00"));
        timePare.add(new Pair<String, String>("14:20", "15:40"));
        timePare.add(new Pair<String, String>("15:50", "17:10"));
        timePare.add(new Pair<String, String>("17:20", "18:40"));
        timePare.add(new Pair<String, String>("18:50", "20:00"));


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Date curDate = new Date();
                SimpleDateFormat ft =
                        new SimpleDateFormat ("HH:mm");

                String curDateStr = ft.format(curDate);
                if (curDateStr.length()<5) curDateStr = '0' + curDateStr;

                for (int i=0; i<timePare.size(); i++) {
                    if(curDateStr.compareTo(timePare.get(i).first) >= 0) {
                        if(curDateStr.compareTo(timePare.get(i).second) <= 0) {
                            try {
                                long minutes = ft.parse(timePare.get(i).second).getTime()
                                        - ft.parse(curDateStr).getTime();
                                sendNotification("Осталость " + Long.toString(minutes/1000/60), "1");
                            }
                            catch(ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }


                String str = ft.format(curDate);
                try {
                    Log.d("timeLog", Long.toString(ft.parse(str).getTime()));
                }
                catch(ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ServiceLog", "Start command");
        //sendNotification("Title", "text");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ServiceLog", "Destroy");
        notificationManager.cancel(NOTIFICATION_ID);
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    public void sendNotification(String title, String text) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(text);

        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);
        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
