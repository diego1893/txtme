package com.monsordi.txtme;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.monsordi.txtme.dialog.DialogTxtMe;
import com.monsordi.txtme.firebaseauth.EmailPassword;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity implements DialogTxtMe.DialogTxtMeTasks, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    public static final String DATA_REFERENCE = "chat2";

    private static final int CONTACTS_LIST = 100;
    private static final int SETTINGS = 101;
    private static final int SIGN_OUT = 102;
    private FirebaseRecyclerAdapter<User,UserHolder> adapter;


    private Menu menu;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    DatabaseReference db;
    @BindView(R.id.chat_toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navview)
    NavigationView navView;
    CircularImageView image;

    EditText chatMessage;
    FloatingActionButton chatButtonSend;
    ListView listMessages;

    //Elements Adapter
    List<Message> arrayMessages;
    ArrayAdapter<Message> messageAdapter;

    private FirebaseAuth mAuth;

    private String nameDisplay;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getMessages() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.chat_activity));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chatMessage = findViewById(R.id.chat_message);
        chatButtonSend = findViewById(R.id.chat_send_button);
        listMessages = findViewById(R.id.chat_listview);

        mAuth = FirebaseAuth.getInstance();

        arrayMessages = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, R.layout.row_my_message, arrayMessages, mAuth.getCurrentUser().getUid());

        listMessages.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listMessages.setStackFromBottom(true);
        listMessages.setAdapter(messageAdapter);

        navView.setNavigationItemSelectedListener(this);
        chatButtonSend.setOnClickListener(this);
        image =  navView.getHeaderView(0).findViewById(R.id.image);
        /*Checks if there is a current active user. If yes, a welcome greeting appears on the
         screen. On the contrary, the user is taken to the SignInActivity*/
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser == null){
            startActivity(new Intent(this,SignInActivity.class));
            finish();
        } else {
            getMessages();
            getFirebaseDatabase();
        }

        //Get Messages
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(DATA_REFERENCE);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> receivedMessages = dataSnapshot.getChildren();

                arrayMessages.clear();
                while (receivedMessages.iterator().hasNext()) {
                    DataSnapshot data = receivedMessages.iterator().next();
                    Message message1 = data.getValue(Message.class);
                    Log.i("Mensaje",message1.getMessage());
                    arrayMessages.add(message1);
                }

                messageAdapter.notifyDataSetChanged();
                //Toast.makeText(MainActivity.this, mensaje1.getMensaje(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.menu= menu;
        return true;
    }
    private void mostrarDatosMenu(int id, String titulo){
        MenuItem menuItem = navView.getMenu().findItem(id);
        menuItem.setTitle(titulo);
    }

    @Nullable
    @Override
    public ActionBar getSupportActionBar() {
        return super.getSupportActionBar();
    }

    private void getFirebaseDatabase() {

        db = FirebaseDatabase.getInstance().getReference().child("users").child(mFirebaseUser.getUid());
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nameDisplay = dataSnapshot.child("name").getValue().toString();
                mostrarDatosMenu(R.id.nombre,dataSnapshot.child("name").getValue().toString());
                mostrarDatosMenu(R.id.correo,dataSnapshot.child("email").getValue().toString());
                mostrarDatosMenu(R.id.telefono,dataSnapshot.child("phone").getValue().toString());
                if (dataSnapshot.child("imageString").getValue() !=null) {
                    Glide.with(ChatActivity.this)
                            .load(dataSnapshot.child("imageString").getValue().toString())
                            .into(image);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.menu_seccion1:
                Intent intent1 = new Intent(ChatActivity.this,editarActivity.class);
                intent1.putExtra("id",mFirebaseUser.getUid());
                startActivity(intent1);
                break;
            case R.id.menu_seccion2:
                Intent intent = new Intent(ChatActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_seccion3:
                DialogTxtMe dialogTxtMe = new DialogTxtMe(this,this);
                dialogTxtMe.showGoTravelDialog(getString(R.string.sign_out));
                break;

        }
        drawerLayout.closeDrawers();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chat_send_button:
                String text = chatMessage.getText().toString();
                if(!text.isEmpty()) {
                    Date date = new Date();
                    String idChild = date.getTime()+"";
                    String minutes = (date.getMinutes()<10)?"0"+date.getMinutes():""+date.getMinutes();
                    int hour = (date.getHours()>12)?date.getHours()-12:date.getHours();
                    String zone = (date.getHours()<12)?"A.M.":"P.M.";
                    String time = hour+":"+minutes+" "+zone;

                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    String uid = currentUser.getUid();
                    //String autor = currentUser.getDisplayName();

                    Message newMessage = new Message(text, uid, time, nameDisplay);

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference(DATA_REFERENCE).child(idChild);
                    myRef.setValue(newMessage);
                    //Toast.makeText(this, date.getTimezoneOffset(), Toast.LENGTH_SHORT).show();

                    chatMessage.setText("");
                }

                break;
        }
    }

}
