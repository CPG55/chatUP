package dam.cpg.chatup.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

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

    private List<User> usersList;
    private UsersAdapter usersAdapter;
    private LinearLayoutManager linearLayoutManager;

    private String myUserUID;
    private String myUserProfilePictureURL;

    private FirebaseAuth myAuthentication;
    private FirebaseDatabase myFirebaseDatabase;

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
        }

        // Recuperar datos para la lista de usuarios.
        usersList = getUsersList();

        // Montar el RecyclerView.
        usersAdapter = new UsersAdapter(this, usersList);
        linearLayoutManager = new LinearLayoutManager(this);

        myUsersRVList.setLayoutManager(linearLayoutManager);
        myUsersRVList.setAdapter(usersAdapter);


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

        List<User> fetchedUsers = new ArrayList<>();

        // Query Firebase para el usuario.
//        new Firebase('https://example-data-sql.firebaseio.com/user/1').once('value', function(snap) {
//            console.log('I fetched a user!', snap.val());
//        });

        // 1 Recuperar UID de mi usuario. DONE
        // 2 Con el UID de mi usuario, tengo acceso a la lista de grupos a los que pertenezco.
        // 3 Con la lista de grupos, tengo el UID de cada grupo, puedo recuperar una lista de UIDS de usuarios
        // de esos grupos, cada UID de usuario se asocia a un UID de grupo. (Mapa?).
        // 4 Relleno una lista de objetos Group con esos datos, y listo ! (easy).


        return fetchedUsers;
    }

    private void setProfilePicture() {
        //TODO

    }

    private void getProfilePicture() {

//        //Uri u = group.getData(); Aqui se recupera la cadena del usuario.
//        storageReference = storage.getReference("profile_pictures");//imagenes de perfil
//        final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());
//        fotoReferencia.putFile(u).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Uri u = taskSnapshot.getDownloadUrl();
//                fotoPerfilCadena = u.toString();
//                //MensajeEnviar m = new MensajeEnviar(NOMBRE_USUARIO+" ha actualizado su foto de perfil",u.toString(),NOMBRE_USUARIO,fotoPerfilCadena,"2",ServerValue.TIMESTAMP);
//                //databaseReference.push().setValue(m);
//                Glide.with(MainActivity.this).load(u.toString()).into(fotoPerfil);
//            }
//        });


    }

    private void returnLogin() {
        startActivity(new Intent(ContactsActivity.this, LoginActivity.class));
        finish();
    }

}