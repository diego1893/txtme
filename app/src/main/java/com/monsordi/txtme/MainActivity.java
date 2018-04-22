package com.monsordi.txtme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements UserHolder.OnItemClickListener {

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;


    @BindView(R.id.main_recyclerview)
    RecyclerView recyclerView;

    private RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter<User,UserHolder> adapter;

    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.participants));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        getFirebaseDatabase();

    }




    private void getFirebaseDatabase() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        adapter = new FirebaseRecyclerAdapter<User, UserHolder>(User.class,
                R.layout.row_contacts,UserHolder.class,databaseReference) {
            @Override
            protected void populateViewHolder(UserHolder viewHolder, User model, int position) {
                viewHolder.bind(model,MainActivity.this);
            }
        };
        recyclerView.setAdapter(adapter);
    }

    //****************************************************************************************************************

    //Handles click on each element of the list

    @Override
    public void OnItemClick(User user, int position) {
        Intent intent = new Intent(MainActivity.this,Detalles.class);
        intent.putExtra(getString(R.string.user_display), user);
        startActivity(intent);
    }
}


