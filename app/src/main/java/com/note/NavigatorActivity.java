package com.note;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;

import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricManager.*;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.widget.Toast;

public class NavigatorActivity extends AppCompatActivity {
    public static SharedPreferences mSharedPreferences;
    public static SharedPreferences.Editor mEditor;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator);

        mSharedPreferences  = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();
        //mEditor.clear().commit();

        BiometricManager biometricManager = BiometricManager.from(this);

        if(!(biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED) && mSharedPreferences.contains("password")) {
            Intent intent = new Intent(getApplicationContext(), LoginChoice.class);
            startActivity(intent);
        }
        else if(!mSharedPreferences.contains("password")) {
            Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent2);
        }
        else {
            Toast.makeText(getApplicationContext(), "Dodaj odcisk palca. Zostaniesz przekierowany do ustawień.", Toast.LENGTH_LONG ).show();
            final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
            startActivityForResult(enrollIntent, 0);
        }
    }

    @Override
    protected void onResume() {
        SharedPreferences mSharedPreferences  = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        BiometricManager biometricManager = BiometricManager.from(this);
        if(!(biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED) && mSharedPreferences.contains("password")) {
            Intent intent = new Intent(getApplicationContext(), LoginChoice.class);
            startActivity(intent);
        }
        else if(!mSharedPreferences.contains("password")) {
            Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent2);
        }
        else {
            Toast.makeText(getApplicationContext(), "Dodaj odcisk palca. Zostaniesz przekierowany do ustawień.", Toast.LENGTH_LONG ).show();
            final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
            startActivityForResult(enrollIntent, 0);
        }
        super.onResume();
    }
}