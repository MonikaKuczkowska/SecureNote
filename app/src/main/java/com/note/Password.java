package com.note;

import android.content.SharedPreferences;
import android.widget.EditText;

import java.util.regex.Pattern;

public class Password {

    public static boolean passwordValidation (EditText password) {
        Pattern passwordPattern = Pattern.compile("^" + "(?=.*[0-9])" + "(?=.*[a-z])" + "(?=.*[A-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$)" + ".{8,}" + "$");

        String passwordInput = password.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            password.setError("Pole nie może być puste.");
            return false;
        } else if (!passwordPattern.matcher(passwordInput).matches()) {
            password.setError("Hasło za słabe.");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    public static void savePassword(EditText password) {
        String passwordInput = password.getText().toString().trim();

        String generatedSecuredPasswordHash = BCrypt.hashpw(passwordInput, BCrypt.gensalt(12));

        NavigatorActivity.mEditor.putString("password", generatedSecuredPasswordHash);
        NavigatorActivity.mEditor.commit();
    }

    public static boolean checkPassword(EditText password) {
        String passwordInput = password.getText().toString().trim();

        return BCrypt.checkpw(passwordInput, NavigatorActivity.mSharedPreferences.getString("password", null) );
    }

}
