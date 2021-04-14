package com.note;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static SharedPreferences mSharedPreferences;
    public static SharedPreferences.Editor mEditor;

    private int attemptsCounter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView passwordMessage = (TextView) findViewById(R.id.passMessage);
        EditText password = (EditText) findViewById(R.id.password);
        Button logInButton = (Button) findViewById(R.id.buttonLogIn);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();
        //mEditor.clear().commit();

        if (mSharedPreferences.contains("password")) {
            passwordMessage.setText("Podaj hasło");
        }
        else {
            passwordMessage.setText("Stwórz hasło zabezpieczające aplikację");
        }

        logInButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if ((mSharedPreferences.contains("password"))) {
                    if(Password.checkPassword(password)) {
                        Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                        startActivity(intent);
                    }
                    else {
                        attemptsCounter--;
                        password.setError("Błędne hasło");
                        if (attemptsCounter == 0) {
                            logInButton.setEnabled(false);
                            Toast.makeText(getApplicationContext(), "Przekroczyłeś dopuszczalną liczbę prób.", Toast.LENGTH_LONG ).show();
                        }
                    }
                }
                else {
                    if (Password.passwordValidation(password)) {
                        Password.savePassword(password);
                        BiometricManager biometricManager = BiometricManager.from(getApplicationContext());
                        if(biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED) {
                            Intent intent = new Intent(getApplicationContext(), NavigatorActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }
}