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
        //Log.d("ServiceLog", "Create");
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


        checkTimeAndSendMsg();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                checkTimeAndSendMsg();
            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.d("ServiceLog", "Start command");
        checkTimeAndSendMsg();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Log.d("ServiceLog", "Destroy");
        notificationManager.cancel(NOTIFICATION_ID);
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    public void checkTimeAndSendMsg() {
        Date curDate = new Date();

        SimpleDateFormat ft =
                new SimpleDateFormat ("HH:mm");
        String curDateStr = ft.format(curDate);
        if(curDateStr.compareTo(timePare.get(timePare.size()-1).second) > 0) {
            sendNotification(getResources().getString(R.string.titleEnd),
                    getResources().getString(R.string.textEnd),
                    NOTIFICATION_ID);
            return;
        }

        long minutes = getMinutesToEnd(curDate);
        sendNotification(Long.toString(minutes) + " " + getResources().getString(R.string.minutes),
                getResources().getString(R.string.before_end_pair) + " " + Long.toString(minutes)
                        + " " + getResources().getString(R.string.minutes), NOTIFICATION_ID);
    }

    public void sendNotification(String title, String text, int notifyId) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(text);

        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);
        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(notifyId, mBuilder.build());
    }

    public long getMinutesToEnd(Date curDate) {
        SimpleDateFormat ft =
                new SimpleDateFormat ("HH:mm");

        String curDateStr = ft.format(curDate);
        if (curDateStr.length()<5) curDateStr = '0' + curDateStr;

        long minutes = 0;
        for (int i=0; i<timePare.size(); i++) {
            if(curDateStr.compareTo(timePare.get(i).first) >= 0) {
                if(curDateStr.compareTo(timePare.get(i).second) <= 0) {
                    try {
                        minutes = ft.parse(timePare.get(i).second).getTime()
                                - ft.parse(curDateStr).getTime();
                    }
                    catch(ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        minutes = minutes/1000/60;
        return minutes;
    }

}
