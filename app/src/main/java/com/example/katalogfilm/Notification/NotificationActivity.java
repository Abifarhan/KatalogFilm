package com.example.katalogfilm.Notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.example.katalogfilm.R;

public class NotificationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{
        AlarmReceiver alarmReceiver;

        private SwitchPreferenceCompat notifikasiharian;
        private SwitchPreferenceCompat notifikasiRelease;

        String NOTIFIKASI_RILIS,NOTIFIKASI_HARIAN;

        Context context;
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            alarmReceiver = new AlarmReceiver();

            NOTIFIKASI_HARIAN = "notifikasi_harian";
            NOTIFIKASI_RILIS = "notifikasi rilis";

            notifikasiharian = findPreference(NOTIFIKASI_HARIAN);
            notifikasiRelease = findPreference(NOTIFIKASI_RILIS);

            SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
            notifikasiharian.setChecked(sharedPreferences.getBoolean(NOTIFIKASI_HARIAN,false));
            notifikasiRelease.setChecked(sharedPreferences.getBoolean(NOTIFIKASI_RILIS,false));


        }


        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
            this.context = context;
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

            if (s.equals(NOTIFIKASI_HARIAN)){
                boolean setNotifikasi = sharedPreferences.getBoolean(NOTIFIKASI_HARIAN,false);
                notifikasiharian.setChecked(setNotifikasi);

                if (setNotifikasi){
                    alarmReceiver.setNotificationforDaily(context, AlarmReceiver.PESAN_HARIAN);
                }
                else {
                    alarmReceiver.turnOfNotificationforDaily(context,AlarmReceiver.PESAN_HARIAN);
                }
            }

            if (s.equals(NOTIFIKASI_RILIS)){
                boolean setNotifikasiRilis = sharedPreferences.getBoolean(NOTIFIKASI_RILIS,false);
                notifikasiRelease.setChecked(setNotifikasiRilis);

                if (setNotifikasiRilis){
                    alarmReceiver.setNotificationforRilis(context,AlarmReceiver.PESAN_RILIS);
                }
                else {
                    alarmReceiver.turnOfNotificationforRilis(context,AlarmReceiver.PESAN_RILIS);
                }
            }
        }
    }
}