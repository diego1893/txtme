package com.monsordi.txtme;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.monsordi.txtme.dialog.DialogTxtMe;
import com.monsordi.txtme.firebaseauth.EmailPassword;

import butterknife.BindView;

public class editarActivity extends AppCompatActivity implements View.OnClickListener,
        DialogTxtMe.DialogTxtMeTasks, OnCompleteListener, OnFailureListener,  OnSuccessListener<UploadTask.TaskSnapshot>{
    private static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 100;
    private static final int REQUEST_IMAGE = 200;
    private Uri pictureUri;
    DatabaseReference db;
    String id, nameS, emailS,phoneS, pictureString, picture ;
    EditText name;
    EditText email;
    EditText phone;
    CircularImageView image;
    Button save;
    String userName = "panchito";
    private StorageReference storageReference;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);
        name= findViewById(R.id.signUp_nameEditText);
        email= findViewById(R.id.signUp_emailEditText);
        phone= findViewById(R.id.signUp_PhoneEditText);
        image = findViewById(R.id.signUp_picture);
        save = findViewById(R.id.save);
        progressBar = findViewById(R.id.signUp_progressBar);
        id  = getIntent().getStringExtra("id");
        db = FirebaseDatabase.getInstance().getReference().child("users").child(id);
        image.setOnClickListener(this);
        save.setOnClickListener(this);
        getFirebaseDatabase();
    }

    private void getFirebaseDatabase() {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name.setText( dataSnapshot.child("name").getValue().toString());
                email.setText( dataSnapshot.child("email").getValue().toString());
                phone.setText( dataSnapshot.child("phone").getValue().toString());
                picture = dataSnapshot.child("imageString").getValue().toString();
                if (dataSnapshot.child("imageString").getValue() !=null) {
                    Glide.with(editarActivity.this)
                            .load(picture)
                            .into(image);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //Reads all fields
            case R.id.save:
                 nameS = name.getText().toString();
                 emailS = email.getText().toString();
                 phoneS = phone.getText().toString();


                //If no field is empty, the user is warned to check their information
                if (!TextUtils.isEmpty(nameS)  && !TextUtils.isEmpty(emailS)
                        && !TextUtils.isEmpty(phoneS)) {
                    DialogTxtMe DialogTxtMe = new DialogTxtMe(editarActivity.this, this);
                    DialogTxtMe.showGoTravelDialog(getString(R.string.continue_sign_up));

                } else
                    Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
                break;

            case R.id.signUp_picture:
                checkPermissionForPicture();
                break;
        }
    }
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

    //entra a la galeria
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
                            .into(new BitmapImageViewTarget(image));
                    break;
            }
        }
    }

    @Override
    public void doCancelTask(Dialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void doOkTask(Dialog dialog) {
        dialog.dismiss();
        save();
    }

    public void save(){
        progressBar.setVisibility(View.VISIBLE);
        storageReference = FirebaseStorage.getInstance().getReference().child("users").child(id);
        if (pictureUri!=null){
            UploadTask uploadTask = storageReference.putFile(pictureUri);
            uploadTask.addOnSuccessListener(this).addOnFailureListener(this);
           // pictureString = taskSnapshot.getDownloadUrl().toString();
        }else {
            User updateUser = new User(id, nameS, userName, emailS, phoneS, picture);
            udate(updateUser);
        }

    }


    @Override
    public void onComplete(@NonNull Task task) {
        Toast.makeText(this, "Se actualizo correctamente", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
       // finish();

    }

    @Override
    public void onFailure(@NonNull Exception e) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, "Ocurrio un error"+ e.getMessage(), Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        pictureString = taskSnapshot.getDownloadUrl().toString();
        User updateUser = new User(id,nameS,userName,emailS,phoneS, pictureString);
        udate(updateUser);
    }
    public void  udate(User updateUser){
        db.setValue(updateUser).addOnCompleteListener(this).addOnFailureListener(this);

    }
}
