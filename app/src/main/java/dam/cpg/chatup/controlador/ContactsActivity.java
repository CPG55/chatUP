package dam.cpg.chatup.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dam.cpg.chatup.R;
import dam.cpg.chatup.modelo.Group;
import dam.cpg.chatup.modelo.User;
import dam.cpg.chatup.vista.UsersAdapter;

/**
 * @author Carlos Pérez on 12/06/18.
 */
public class ContactsActivity extends AppCompatActivity {

    private static final String TAG = "ContactsActivity";
    private List<User> usersList;
    private UsersAdapter usersAdapter;
    private LinearLayoutManager linearLayoutManager;

    private String myUserUID;
    private String myUserProfilePictureURL;

    private FirebaseAuth myAuthentication;
    private FirebaseDatabase myFirebaseDatabase;
    private DatabaseReference myDatabaseReference;

    @BindView(R.id.toolbar_contacts) Toolbar topToolBar;
    @BindView(R.id.recyclerView_contacts) RecyclerView myUsersRVList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);

        // Link de la toolbar.
        setSupportActionBar(topToolBar);

        // Referencia a Firebase Authentication.
        myAuthentication = FirebaseAuth.getInstance();

        // Recuperar UID de usuario.
        FirebaseUser currentUser = myAuthentication.getCurrentUser();
        if (currentUser != null) {
            myUserUID = currentUser.getUid();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        //usersList = new ArrayList<>();

        // Montar el RecyclerView.
        //usersAdapter = new UsersAdapter(this, usersList);
        usersAdapter = new UsersAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this);

        myUsersRVList.setLayoutManager(linearLayoutManager);
        myUsersRVList.setAdapter(usersAdapter);

        // Recuperar datos para la lista de usuarios.
        // usersList = getUsersList();

        // Referencia de la base de datos al nodo de usuarios.
        myDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");
//      myDatabaseReference = FirebaseDatabase.getInstance().getReference("Groups/group_UID/messages");

        myDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                myDatabaseReference.removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        // Añadir escuchador que va a devolver los datos de usuario.
        myDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                User user = dataSnapshot.getValue(User.class);
                user.setProfilePictureURL(dataSnapshot.getKey());
                usersAdapter.addUser(user);
                usersAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // Click largo en usuarios.
//        myUsersRVList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView,
//                                    View view, int position, long rowId) {
//
//                // Generate a message based on the position
//                String message = "You clicked on " + cheeses[position];
//
//                // Use the message to create a Snackbar
//                Snackbar.make(adapterView, message, Snackbar.LENGTH_LONG)
//                        .show(); // Show the Snackbar
//            }
//        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Opciones de menu.
        switch (item.getItemId()) {

            case R.id.action_settings:
                // Por ahora sin funcionalidad, guarda mensaje al log.
                Log.i("ActionBar", "Settings!");

                return true;

            case R.id.action_search:

                Intent intent = new Intent(ContactsActivity.this, ChatActivity.class);
                startActivity(intent);
                Log.i("ActionBar", "Buscar!");

                return true;

            case R.id.action_logout:

                FirebaseAuth.getInstance().signOut();
                returnLogin();
                Log.i("ActionBar", "Logout!");

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private List<User> getUsersList() {

        final List<User> fetchedUsers = new ArrayList<>();

        return fetchedUsers;
    }

    private void setProfilePicture() {
        //TODO

    }

    private void getProfilePicture() {
        //TODO


    }

    private void returnLogin() {
        startActivity(new Intent(ContactsActivity.this, LoginActivity.class));
        finish();
    }

}