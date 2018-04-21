package com.monsordi.txtme;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Detalles extends AppCompatActivity {

    TextView userName,phone,email;
    ImageView imgUser;
    CollapsingToolbarLayout ctl;

    FirebaseDatabase database;
    DatabaseReference user;

    String key ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        //Firebase
        database = FirebaseDatabase.getInstance();
        user = database.getReference().child("users");

        userName = findViewById(R.id.tx_username);
        phone = findViewById(R.id.tx_userphone);
        email = findViewById(R.id.tx_userEmail);

        imgUser = findViewById(R.id.img_user);
        ctl = findViewById(R.id.collapsing);

        ctl.setCollapsedTitleTextAppearance(R.style.CollapseAppbar);
        ctl.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);

        if (getIntent() != null){
            key = getIntent().getStringExtra(getString(R.string.user_display));

        }if (!key.isEmpty()){
            getDetailUser(key);
        }
    }

    private void getDetailUser(String key) {

        user.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                Glide.with(getBaseContext()).load(user.getImageString()).into(imgUser);
                ctl.setTitle(user.getName());
                userName.setText(user.getUserName());
                phone.setText(user.getPhone());
                email.setText(user.getEmail());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
