package com.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginChoice extends AppCompatActivity {
    public static SharedPreferences mSharedPreferences;
    public static SharedPreferences.Editor mEditor;

    private static final String TAG = MainActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_choice);

        Button passLogin = (Button) findViewById(R.id.passLogin);
        Button fingerprintLogin = (Button) findViewById(R.id.fingerprintLogin);

        mSharedPreferences  = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();
        //mEditor.clear().commit();

        Executor newExecutor = ContextCompat.getMainExecutor(this);
        final BiometricPrompt myBiometricPrompt = new BiometricPrompt(LoginChoice.this, newExecutor, new BiometricPrompt.AuthenticationCallback() {
           @Override
           public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
               super.onAuthenticationError(errorCode, errString);
               if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
               } else {
                   Log.d(TAG, "Błąd uwierzytelniania: " + errString);
               }
           }
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Log.d(TAG, "Odcisk palca rozpoznany!");
                Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.d(TAG, "Odcisk palca nierozpoznany!");
            }
        });

        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Logowanie odciskiem palca")
                .setDescription("Potwierdź swoją tożsamość za pomocą odcisku palca")
                .setNegativeButtonText("Anuluj")
                .build();

        passLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPassLogin = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentPassLogin);
            }
        });
        fingerprintLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myBiometricPrompt.authenticate(promptInfo);
            }
        });
    }

}