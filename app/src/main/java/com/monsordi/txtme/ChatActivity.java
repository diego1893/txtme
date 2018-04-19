package com.monsordi.txtme;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.monsordi.txtme.dialog.DialogTxtMe;
import com.monsordi.txtme.firebaseauth.EmailPassword;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity implements DialogTxtMe.DialogTxtMeTasks {

    private static final int CONTACTS_LIST = 100;
    private static final int SETTINGS = 101;
    private static final int SIGN_OUT = 102;
    @BindView(R.id.chat_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.chat_activity));

        /*Checks if there is a current active user. If yes, a welcome greeting appears on the
         screen. On the contrary, the user is taken to the SignInActivity*/
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser == null){
            startActivity(new Intent(this,SignInActivity.class));
            finish();
        } else {
            String welcomeGreeting = new StringBuilder(getString(R.string.welcome)).
                    append(" ").append(mFirebaseUser.getDisplayName()).toString();
            Toast.makeText(this, welcomeGreeting, Toast.LENGTH_SHORT).show();
            getMessages();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE,CONTACTS_LIST,Menu.NONE,getString(R.string.see_contacts));
        menu.add(Menu.NONE,SETTINGS,Menu.NONE,getString(R.string.settings));
        menu.add(Menu.NONE,SIGN_OUT,Menu.NONE,getString(R.string.sign_out_title));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case CONTACTS_LIST:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;

            case SETTINGS:
                break;

            case SIGN_OUT:
                DialogTxtMe dialogTxtMe = new DialogTxtMe(this,this);
                dialogTxtMe.showGoTravelDialog(getString(R.string.sign_out));
                    break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getMessages() {

    }

    //****************************************************************************************************************

    //The next two following methods are the ones related to the selection in the appearing dialog.

    //Closes dialog
    @Override
    public void doCancelTask(Dialog dialog) {
        dialog.dismiss();
    }


    //Signs out and navigates to the SignInActivity
    @Override
    public void doOkTask(Dialog dialog) {
        EmailPassword emailPassword = new EmailPassword();
        emailPassword.signOut();
        startActivity(new Intent(this,SignInActivity.class));
        finish();
    }


}
