package com.monsordi.txtme;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.monsordi.txtme.firebaseauth.EmailPassword;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener,
        EmailPassword.EmailPasswordTasks {

    @BindView(R.id.login_emailEditText)
    EditText emailEditText;
    @BindView(R.id.login_passwordEditText)
    EditText passwordEditText;
    @BindView(R.id.login_layout)
    ConstraintLayout loginLayout;
    @BindView(R.id.login_progressBar)
    ProgressBar progressBar;
    @BindView(R.id.login_signInButton)
    Button signInButton;
    @BindView(R.id.login_signUpTextView)
    TextView signUpTextView;
    @BindView(R.id.login_signInTextView)
    TextView signInTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        signInButton.setOnClickListener(this);
        signUpTextView.setOnClickListener(this);
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            //A sign in attempt occurs
            case R.id.login_signInButton:
                EmailPassword emailPassword = new EmailPassword(this,this);
                emailPassword.signIn(emailEditText.getText().toString(),passwordEditText.getText().toString());
                break;

            //The user is taken to another activity in order to create a new account
            case R.id.login_signUpTextView:
                startActivityForResult(new Intent(this,SignUpActivity.class),Utils.SIGN_UP_REQUEST_CODE);
                break;
        }
    }


    //If the signing up process is completed successfully, this activity is closed.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case Utils.SIGN_UP_REQUEST_CODE:
                if(Utils.SIGN_UP_COMPLETED_RESULT == resultCode)
                    finish();
                break;
        }
    }

   // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

//The next four following methods are the implementation of the EmailPassword sign in tasks

    //Changes visibility depending of the sign in state
    @Override
    public void showProgressDialog(boolean isShown) {
        loginLayout.setVisibility(isShown ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(isShown ? View.VISIBLE : View.GONE);
    }


    /*After the sign in attempt occurs, if there is an existing user with the written data, then the
    user is taken to the MainActivity*/
    @Override
    public void handleAuthTask(FirebaseUser user) {
        if(user != null){
            startActivity(new Intent(this,ChatActivity.class));
            finish();
        }
    }

    //Sets an error in the corresponding editText depending on the EmailCode
    @Override
    public void setEmailErrorCode(Utils.EmailCode emailErrorCode) {
        if(emailErrorCode == Utils.EmailCode.EMPTY_EMAIL)
            emailEditText.setError(getString(R.string.empty_email));
        else if(emailErrorCode == Utils.EmailCode.UNMATCHED_EMAIL)
            emailEditText.setError(getString(R.string.unmatched_email));
    }

    //Sets an error in the corresponding editText depending on the PasswordCode
    @Override
    public void setPasswordErrorCode(Utils.PasswordCode passwordErrorCode){
        if(passwordErrorCode == Utils.PasswordCode.EMPTY_PASSWORD)
            passwordEditText.setError(getString(R.string.empty_password));
    }
}


