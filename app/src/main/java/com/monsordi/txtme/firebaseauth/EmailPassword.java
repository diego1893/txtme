package com.monsordi.txtme.firebaseauth;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.monsordi.txtme.Utils;

/**
 * Created by Diego on 31/03/18.
 */

public class EmailPassword implements OnCompleteListener<AuthResult> {

    private FirebaseAuth mFirebaseAuth;
    private Context mContext;
    private EmailPasswordTasks mEmailPasswordTasks;

    public EmailPassword(){
        this.mFirebaseAuth = FirebaseAuth.getInstance();
    }

    public EmailPassword(Context context, EmailPasswordTasks emailPasswordTasks) {
        this.mContext = context;
        this.mEmailPasswordTasks = emailPasswordTasks;
        this.mFirebaseAuth = FirebaseAuth.getInstance();
    }

    //****************************************************************************************************************

    //Methods to be implemented depending on the current activity.
    public interface EmailPasswordTasks{
        void showProgressDialog(boolean isShown);
        void handleAuthTask(FirebaseUser user);
        void setEmailErrorCode(Utils.EmailCode emailErrorCode);
        void setPasswordErrorCode(Utils.PasswordCode passwordErrorCode);
    }

    //****************************************************************************************************************

    //Main methods

    public void signUp(String email, String password){
        if(!isValidForm(email,password,true))
            return;

        //Changes visibility in some aspects of the UI and attempts to create a new user.
        mEmailPasswordTasks.showProgressDialog(true);
        mFirebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this);
    }

    public void signIn(String email, String password){
        if(!isValidForm(email,password,false))
            return;

        //Changes visibility in some aspects of the UI and attempts to sign in.
        mEmailPasswordTasks.showProgressDialog(true);
        mFirebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this);
    }

    public void signOut(){
        mFirebaseAuth.signOut();
    }

    //****************************************************************************************************************

    //Represents the listener that is called whenever a signing up or signing in process is completed.
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if(task.isSuccessful()){
            FirebaseUser user = mFirebaseAuth.getCurrentUser();
            mEmailPasswordTasks.handleAuthTask(user);
        } else {
            String taskException = task.getException().toString();
            taskException = taskException.substring(taskException.lastIndexOf(":")+2);
            mEmailPasswordTasks.showProgressDialog(false);
            Toast.makeText(mContext,taskException, Toast.LENGTH_LONG).show();
            mEmailPasswordTasks.handleAuthTask(null);
        }
    }

    //Determines if the necessary data in order to sign in or sign up has the right format.
    private boolean isValidForm(String email, String password, boolean checkPasswordForm){
        Utils utils = new Utils(mContext);

        Utils.EmailCode emailCode = utils.isValidEmail(email);
        mEmailPasswordTasks.setEmailErrorCode(emailCode);

        Utils.PasswordCode passwordCode = utils.isValidPassword(checkPasswordForm,password);
        mEmailPasswordTasks.setPasswordErrorCode(passwordCode);

        return emailCode == Utils.EmailCode.VALID_EMAIL && passwordCode == Utils.PasswordCode.VALID_PASSWORD;
    }


}
