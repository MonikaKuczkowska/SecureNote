package com.note;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.regex.Pattern;

public class ChangePassActivity extends AppCompatActivity {
    static EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        Button saveNewPassButton = (Button) findViewById(R.id.buttonConfirm);
        password = (EditText) findViewById(R.id.passwordNew);

        SharedPreferences mSharedPreferences  = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();

        saveNewPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Password.passwordValidation(password)) {
                    String encryptedNote = mSharedPreferences.getString("note", null);
                    String oldPassword = mSharedPreferences.getString("password", null);
                    try {
                        String ivString = mSharedPreferences.getString("iv", null);
                        String[] split = ivString.substring(1, ivString.length()-1).split(", ");
                        byte[] array = new byte[split.length];
                        for (int i = 0; i < split.length; i++) {
                            array[i] = Byte.parseByte(split[i]);
                        }
                        byte[] iv = array;
                        String decryptedNote = AESEncryption.decrypt(encryptedNote, oldPassword, iv);
                        Password.savePassword(password);
                        String newPassword = mSharedPreferences.getString("password", null);
                        byte [] ivNew = generateIV();
                        mEditor.putString("iv", Arrays.toString(ivNew)).commit();
                        String encryptedNoteNewPass = AESEncryption.encrypt(decryptedNote, newPassword, ivNew);
                        mEditor.putString("note", encryptedNoteNewPass);
                        mEditor.apply();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "Hasło zostało zmienione", Toast.LENGTH_LONG ).show();
                    finish();
                }
            }
        });
    }
    public byte[] generateIV() {
        byte[] iv = new byte[16];
        SecureRandom random;
        random = new SecureRandom();
        random.nextBytes(iv);
        return iv;
    }
}