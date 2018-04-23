package com.monsordi.txtme;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Detalles extends AppCompatActivity implements View.OnClickListener {

    TextView userName, phone, email;
    android.support.v7.widget.Toolbar toolbar;
    ImageView imgUser,phoneIcon,emailIcon;
    CollapsingToolbarLayout ctl;

    FirebaseDatabase database;
    DatabaseReference user;

    String key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Firebase
        database = FirebaseDatabase.getInstance();
        user = database.getReference().child("users");

        userName = findViewById(R.id.tx_username);
        phone = findViewById(R.id.tx_userphone);
        phoneIcon = findViewById(R.id.phone_icon);
        phoneIcon.setOnClickListener(this);
        email = findViewById(R.id.tx_userEmail);
        emailIcon = findViewById(R.id.email_icon);
        emailIcon.setOnClickListener(this);

        imgUser = findViewById(R.id.img_user);

        ctl = findViewById(R.id.collapsing);

        ctl.setCollapsedTitleTextAppearance(R.style.CollapseAppbar);
        ctl.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);

        if (getIntent() != null) {
            key = getIntent().getStringExtra(getString(R.string.user_display));

        }
        if (!key.isEmpty()) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.phone_icon:
                String phoneUri = "tel:" + phone.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(phoneUri));
                startActivity(intent);
                break;

            case R.id.email_icon:
                break;
        }
    }
}
