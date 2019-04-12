package com.hqcd.smartsecuritycamera;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

public class SettingsFragment extends PreferenceFragment {

    public static final String PREF_IP_ADDRESS = "pref_ip_address";
    public static final String PREF_DEVICE_NAME = "pref_device_name";
    public static final String PREF_PORT = "pref_port";

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                if(s.equals(PREF_IP_ADDRESS))
                {
                    Preference ipPref = findPreference(s);
                    ipPref.setSummary(sharedPreferences.getString(s,"" ));
                }

                if(s.equals(PREF_DEVICE_NAME))
                {
                    Preference devicePref = findPreference(s);
                    devicePref.setSummary(sharedPreferences.getString(s,"" ));
                }

                if(s.equals(PREF_PORT))
                {
                    Preference portPref = findPreference(s);
                    portPref.setSummary(sharedPreferences.getString(s,"" ));
                }
            }
        };

    }

    @Override
    public void onResume() {
        super.onResume();

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);
        Preference ipPref = findPreference(PREF_IP_ADDRESS);
        ipPref.setSummary("IP Address: " + getPreferenceScreen().getSharedPreferences().getString(PREF_IP_ADDRESS, ""));

        Preference devicePref = findPreference(PREF_DEVICE_NAME);
        devicePref.setSummary("Device Name: " + getPreferenceScreen().getSharedPreferences().getString(PREF_DEVICE_NAME, ""));

        Preference portPref = findPreference(PREF_PORT);
        portPref.setSummary("Device Name: " + getPreferenceScreen().getSharedPreferences().getString(PREF_PORT, ""));

    }

    @Override
    public void onPause() {
        super.onPause();

        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }
}
