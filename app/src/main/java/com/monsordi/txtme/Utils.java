package com.monsordi.txtme;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Created by Diego on 31/03/18.
 */

public class Utils {

    public static final int SIGN_UP_REQUEST_CODE = 0;
    public static final int SIGN_UP_COMPLETED_RESULT = 1;

    public enum EmailCode{ EMPTY_EMAIL,UNMATCHED_EMAIL,VALID_EMAIL }
    public enum PasswordCode{EMPTY_PASSWORD,UNMATCHED_PASSWORD,VALID_PASSWORD}
    private String mPasswordRegex;


    //Necessary constructor in order to obtain the regular expression from the resources.
    public Utils(Context context){
        this.mPasswordRegex = context.getString(R.string.password_regex);
    }


    //Returns an email code that determines what kind of email was written.
    public EmailCode isValidEmail(String email){
        if (TextUtils.isEmpty(email)) {
            return EmailCode.EMPTY_EMAIL;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return EmailCode.UNMATCHED_EMAIL;
        } else {
            return EmailCode.VALID_EMAIL;
        }
    }

    /*Returns a password code that determines what kind of password was written.
    If checkForm is true, the password is compared to the regular expression. */
    public PasswordCode isValidPassword(boolean checkForm,String password){
        if(TextUtils.isEmpty(password)){
            return PasswordCode.EMPTY_PASSWORD;
        } else if(checkForm && !Pattern.compile(mPasswordRegex).matcher(password).matches()){
            return PasswordCode.UNMATCHED_PASSWORD;
        } else{
            return PasswordCode.VALID_PASSWORD;
        }

    }
}
