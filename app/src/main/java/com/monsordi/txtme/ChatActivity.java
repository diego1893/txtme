package com.monsordi.txtme;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity implements DialogTxtMe.DialogTxtMeTasks, NavigationView.OnNavigationItemSelectedListener{

    private static final int CONTACTS_LIST = 100;
    private static final int SETTINGS = 101;
    private static final int SIGN_OUT = 102;
    private FirebaseRecyclerAdapter<User,UserHolder> adapter;


    private Menu menu;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    DatabaseReference db;
    @BindView(R.id.appbar)
    Toolbar toolbar;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navview)
    NavigationView navView;
    CircularImageView image;


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
        // setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(getString(R.string.chat_activity));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navView.setNavigationItemSelectedListener(this);
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
}
