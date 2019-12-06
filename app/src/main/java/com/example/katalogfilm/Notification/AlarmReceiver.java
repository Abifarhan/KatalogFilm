package com.example.katalogfilm.Notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.katalogfilm.BuildConfig;
import com.example.katalogfilm.Item.Movies;
import com.example.katalogfilm.MainActivity;
import com.example.katalogfilm.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class AlarmReceiver extends BroadcastReceiver {

    private final int HARIAN_ID = 1;
    private final int RILIS_ID = 2;

    private static final int  NOTIFIKASI_ID =3;
    private static final String CHANNEL_ID = "Channel_01";
    private static final String CHANNEL_NAME = "Channel_01";

    public static final String PESAN_RILIS = "PESAN_RILIS";
    public static final String PESAN_HARIAN= "PESAN_REMINDER";
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    public static final String EXTRA_NOTIFIKASI = "EXTRA_NOTIFIKASI";
    String judulRelease;

    @Override
    public void onReceive(final Context context, Intent intent) {
        final String pesanforrilis = context.getString(R.string.item_yang_dirilis);
        String pesan = intent.getStringExtra(EXTRA_MESSAGE);
        String type = intent.getStringExtra(EXTRA_NOTIFIKASI);

        if (type.equalsIgnoreCase(PESAN_HARIAN)){
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Intent harianIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,HARIAN_ID,harianIntent,PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_foreground))
                    .setContentTitle(context.getResources().getString(R.string.app_name))
                    .setContentText(pesan)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);



            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                /* Create or update. */
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

                mBuilder.setChannelId(CHANNEL_ID);

                if (mNotificationManager != null) {
                    mNotificationManager.createNotificationChannel(channel);
                }
            }

            Notification notification = mBuilder.build();

            if (mNotificationManager != null) {
                mNotificationManager.notify(NOTIFIKASI_ID, notification);
            }
//            Toast.makeText(context, "jam 7"+type, Toast.LENGTH_SHORT).show();
        }
        else {
            final ArrayList<Movies> listMovie = new ArrayList<>();

            Calendar calendar = Calendar.getInstance();
            String today = Integer.toString(calendar.get(Calendar.YEAR)) + "-" + Integer.toString(calendar.get(Calendar.MONTH)) + "-"+Integer.toString(calendar.get(Calendar.DATE));

            String urlforRelease ="https://api.themoviedb.org/3/discover/movie?api_key="+ BuildConfig.API_KEY + "&primary_release_date.gte=" +  today + "&primary_release_date.lte=" + today;
            Log.wtf("cek url", urlforRelease);

            AsyncHttpClient client = new AsyncHttpClient();
            client.get(urlforRelease, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String rilis = new String(responseBody);
                        JSONObject object = new JSONObject(rilis);
                        JSONArray jsonArray = object.getJSONArray("results");

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject objectRelease = jsonArray.getJSONObject(i);
                            Movies movies = new Movies();
                            movies.setId(objectRelease.getInt("id"));
                            movies.setTitle(objectRelease.getString("title"));
                            movies.setPoster_path(objectRelease.getString("poster_path"));
                            movies.setOverview(objectRelease.getString("overview"));
                            listMovie.add(movies);
                        }

                        for (Movies getTitle : listMovie){
                            judulRelease = getTitle.getTitle();
                        }
                        sendNotificationRelease(context,judulRelease,pesanforrilis,RILIS_ID);


                    } catch (Exception e) {

                        Log.d("gagal rilis",e.getMessage());
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("FAilure","gagal merilis");
                }
            });

        }

    }

    public void sendNotificationRelease(Context context,String judulRelease, String textRelease, int id){

        NotificationCompat.Builder builder;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,id,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        builder = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setContentTitle(judulRelease)
                .setContentText(textRelease)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId(CHANNEL_ID);

            if (notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
        }
        Notification notification = builder.build();
        if (notificationManager != null){
            notificationManager.notify(id,notification);
        }
    }

    public void setNotificationforDaily(Context context, String s){
        String info = context.getResources().getString(R.string.kembali_ke_aplikasi);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, info);
        intent.putExtra(EXTRA_NOTIFIKASI,s);

        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE),7,0,0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,HARIAN_ID,intent,0);
        if (alarmManager != null){
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        }


        Toast.makeText(context, "Notifikasi harian sudah aktif", Toast.LENGTH_SHORT).show();
    }


    public void setNotificationforRilis(Context context, String s){
        String info = context.getResources().getString(R.string.film_yang_rilis);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, info);
        intent.putExtra(EXTRA_NOTIFIKASI,s);

        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE),8,0,0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,RILIS_ID,intent,0);
        if (alarmManager != null){
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        }

        Toast.makeText(context, "Notifikasi rilis sudah aktif", Toast.LENGTH_SHORT).show();
    }

    public void turnOfNotificationforDaily(Context context,String s){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,HARIAN_ID,intent,0);
        pendingIntent.cancel();

        if (alarmManager != null){
            alarmManager.cancel(pendingIntent);
        }

        Toast.makeText(context, "Notifikasi harian sudah dimatikan", Toast.LENGTH_SHORT).show();
    }

    public void turnOfNotificationforRilis(Context context,String s){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,RILIS_ID,intent,0);
        pendingIntent.cancel();

        if (alarmManager != null){
            alarmManager.cancel(pendingIntent);
        }

        Toast.makeText(context, "Notifikasi rilis sudah dimatikan", Toast.LENGTH_SHORT).show();
    }
}
