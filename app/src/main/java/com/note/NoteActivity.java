package com.note;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;


public class NoteActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Button changePassButton = (Button) findViewById(R.id.buttonChangePass);
        Button saveNote = (Button) findViewById(R.id.buttonSave);
        EditText noteEditText = (EditText) findViewById(R.id.note);

        SharedPreferences mSharedPreferences  = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();

        if (mSharedPreferences.contains("note")) {
            String encryptedNote = mSharedPreferences.getString("note", null);
            try {
                String password = mSharedPreferences.getString("password", null);
                String ivString = mSharedPreferences.getString("iv", null);
                String[] split = ivString.substring(1, ivString.length()-1).split(", ");
                byte[] array = new byte[split.length];
                for (int i = 0; i < split.length; i++) {
                    array[i] = Byte.parseByte(split[i]);
                }
                byte[] iv = array;
                String decryptedNote = AESEncryption.decrypt(encryptedNote, password, iv);
                noteEditText.setText(decryptedNote);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences mSharedPreferences  = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                String note = noteEditText.getText().toString().trim();
                try {
                    String password = mSharedPreferences.getString("password", null);
                    byte [] iv = generateIV();
                    mEditor.putString("iv", Arrays.toString(iv)).commit();
                    String encryptedNote = AESEncryption.encrypt(note, password, iv);
                    mEditor.putString("note", encryptedNote);
                    mEditor.apply();
                    Toast.makeText(getApplicationContext(), "Notatka została zapisana", Toast.LENGTH_LONG ).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        changePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPass = new Intent(getApplicationContext(), ChangePassActivity.class);
                startActivity(intentPass);
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