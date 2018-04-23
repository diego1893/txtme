package com.monsordi.txtme;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.monsordi.txtme.dialog.DialogTxtMe;
import com.monsordi.txtme.firebaseauth.EmailPassword;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener,
        EmailPassword.EmailPasswordTasks, DialogTxtMe.DialogTxtMeTasks, OnSuccessListener<UploadTask.TaskSnapshot>, OnFailureListener, OnCompleteListener<Void> {

    private static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 100;
    private static final int REQUEST_IMAGE = 200;
    @BindView(R.id.signUp_picture)
    CircularImageView profilePicture;
    @BindView(R.id.signUp_nameEditText)
    EditText nameEditText;
    @BindView(R.id.signUp_userNameEditText)
    EditText userNameEditText;
    @BindView(R.id.signUp_phoneEditText)
    EditText phoneEditText;
    @BindView(R.id.signUp_emailEditText)
    EditText emailEditText;
    @BindView(R.id.signUp_passwordEditText)
    EditText passwordEditText;
    @BindView(R.id.signUp_confirmEditText)
    EditText confirmPasswordEditText;
    @BindView(R.id.signUp_signUpButton)
    Button signUpButton;
    @BindView(R.id.signUp_progressBar)
    ProgressBar progressBar;
    @BindView(R.id.signUp_container)
    ConstraintLayout container;
    @BindView(R.id.signUp_toolbar)
    Toolbar toolbar;

    private String name;
    private String userName;
    private String email;
    private String phone;
    private Uri pictureUri;
    private String pictureString = "https://app.hyperlingo.com/hyperlingo/img/defaultprofile.png";
    private String password;
    private String confirmPassword;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private int step=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        signUpButton.setOnClickListener(this);
        profilePicture.setOnClickListener(this);
    }

    //****************************************************************************************************************
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Reads all fields
            case R.id.signUp_signUpButton:
                name = nameEditText.getText().toString();
                email = emailEditText.getText().toString();
                userName = userNameEditText.getText().toString();
                phone = phoneEditText.getText().toString();
                password = passwordEditText.getText().toString();
                confirmPassword = confirmPasswordEditText.getText().toString();

                //If no field is empty, the user is warned to check their information
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(userName) && !TextUtils.isEmpty(phone)
                        && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword)) {
                    DialogTxtMe DialogTxtMe = new DialogTxtMe(this, this);
                    DialogTxtMe.showGoTravelDialog(getString(R.string.continue_sign_up));
                } else
                    Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
                break;

            case R.id.signUp_picture:
                checkPermissionForPicture();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Handles back navigation
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //****************************************************************************************************************

    //The next three following methods are the ones related to the selection in the appearing dialog.

    //Closes dialog
    @Override
    public void doCancelTask(Dialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void doOkTask(Dialog dialog) {
        dialog.dismiss();
        proceedWithSignUp(email, password, confirmPassword);
    }

    //Proceeds with signing up process if both passwords match.
    public void proceedWithSignUp(String email, String password, String confirmPassword) {
        if (password.equals(confirmPassword)) {
            EmailPassword emailPassword = new EmailPassword(this, this);
            emailPassword.signUp(email, password);
        } else
            Toast.makeText(this, getString(R.string.passwords_dont_match), Toast.LENGTH_SHORT).show();
    }

    //****************************************************************************************************************

    //These methods allows the user to get their image from their gallery

    private void checkPermissionForPicture() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
        } else {
            getPictureFromGallery();
        }
    }

    private void getPictureFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE:
                    pictureUri = data.getData();
                    Glide.with(this)
                            .load(pictureUri)
                            .asBitmap()
                            .fitCenter()
                            .into(new BitmapImageViewTarget(profilePicture));
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPictureFromGallery();

                } else {
                    Toast.makeText(this, getString(R.string.grant_external), Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    //****************************************************************************************************************

    /*The next four following methods are the implementation of the EmailPassword sign up tasks. The last one is an
    auxiliar method*/

    //Changes visibility depending of the sign up state
    @Override
    public void showProgressDialog(boolean isShown) {
        container.setVisibility(isShown ? View.GONE : View.VISIBLE);
        toolbar.setVisibility(isShown ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(isShown ? View.VISIBLE : View.GONE);
    }

    /**/
    @Override
    public void handleAuthTask(FirebaseUser user) {
        if (user != null) {
            this.user = user;
            User newUser = new User(user.getUid(),name,userName,email,phone,pictureString);
            databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
            databaseReference.setValue(newUser).addOnCompleteListener(this).addOnFailureListener(this);
            /*
            storageReference = FirebaseStorage.getInstance().getReference().child("users").child(user.getUid());
            UploadTask uploadTask = storageReference.putFile(pictureUri);
            uploadTask.addOnSuccessListener(this).addOnFailureListener(this);*/
        }
    }

    //Sets an error in the corresponding editText depending on the EmailCode
    @Override
    public void setEmailErrorCode(Utils.EmailCode emailErrorCode) {
        if (emailErrorCode == Utils.EmailCode.UNMATCHED_EMAIL)
            emailEditText.setError(getString(R.string.unmatched_email));
    }

    //Sets an error in the corresponding editText depending on the PasswordCode
    @Override
    public void setPasswordErrorCode(Utils.PasswordCode passwordErrorCode) {
        if (passwordErrorCode == Utils.PasswordCode.UNMATCHED_PASSWORD)
            passwordEditText.setError(getString(R.string.unmatched_password));
    }

    //****************************************************************************************************************

    //Implementations of uploading task listeners

    @Override
    public void onFailure(@NonNull Exception e) {

    }

    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        pictureString = taskSnapshot.getDownloadUrl().toString();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("imageString");
        databaseReference.setValue(pictureString).addOnCompleteListener(this).addOnFailureListener(this);
    }

    //****************************************************************************************************************

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if(step==0) {
            step++;
            storageReference = FirebaseStorage.getInstance().getReference().child("users").child(user.getUid());
            UploadTask uploadTask = storageReference.putFile(pictureUri);
            uploadTask.addOnSuccessListener(this).addOnFailureListener(this);
        } else if(step==1){
            startActivity(new Intent(this, ChatActivity.class));
            setResult(Utils.SIGN_UP_COMPLETED_RESULT);
            finish();
        }
    }


}
