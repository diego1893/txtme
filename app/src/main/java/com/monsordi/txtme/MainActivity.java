package com.monsordi.txtme;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.monsordi.txtme.dialog.DialogTxtMe;
import com.monsordi.txtme.firebaseauth.EmailPassword;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity{

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_recyclerview)
    RecyclerView recyclerView;

    private RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter<UserDisplay,UserDisplayHolder> adapter;

    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.home));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void getFirebaseDatabase() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("user_display");
        adapter = new FirebaseRecyclerAdapter<UserDisplay, UserDisplayHolder>(UserDisplay.class,
                R.layout.row_contacts,UserDisplayHolder.class,databaseReference) {
            @Override
            protected void populateViewHolder(UserDisplayHolder viewHolder, UserDisplay model, int position) {
                viewHolder.bind(model, new UserDisplayHolder.OnItemClickListener() {
                    @Override
                    public void OnItemClick(UserDisplay userDisplay, int position) {
                        Intent intent = new Intent(MainActivity.this,ChatActivity.class);
                        intent.putExtra(getString(R.string.user_display),userDisplay);
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);

        /*

        Query diasPorClave = FirebaseDatabase.getInstance().getReference().child("dias").limitToLast(2);
        //Query diasPorClave = FirebaseDatabase.getInstance().getReference().child("dias").limitToLast(2).startAt().orderByKey();


        diasPorClave.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(MainActivity.this, "" +dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //databaseReference.setValue("Barbie Sirena");

        Map<String,String> peliculas = new HashMap<>();
        peliculas.put("1","Barbie Rapunzel");
        peliculas.put("2","Barbie en el lago de los cisnes");
        peliculas.put("3","Barbie cascanueces");
        peliculas.put("4","Barbie Fairytopia");
        peliculas.put("5","Barbie princesa isla");

        //databaseReference.setValue(peliculas);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("message");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //databaseReference.push().setValue(editText.getText().toString());
                Message message = new Message(editText.getText().toString(),"Diego");
                databaseReference.push().setValue(message);
                editText.setText("");
            }
        });
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(MainActivity.this, "Data added" + dataSnapshot.getValue(Message.class).getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(MainActivity.this, "Data changed" + dataSnapshot.getValue(Message.class).getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Toast.makeText(MainActivity.this, "Data removed" + dataSnapshot.getValue(Message.class).getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        databaseReference.addChildEventListener(childEventListener);*/
    }

    //****************************************************************************************************************

    //Methods related to the menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            /*When clicking the sign out button, the user is warned through a custom dialog
            if he really wants to complete the action.*/
            case R.id.menu_sign_out:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}


