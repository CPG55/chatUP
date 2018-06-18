package dam.cpg.chatup.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import dam.cpg.chatup.R;
import dam.cpg.chatup.modelo.Message;
import dam.cpg.chatup.modelo.User;
import dam.cpg.chatup.util.DateParsing;
import dam.cpg.chatup.vista.MessagesAdapter;

/**
 * @author Carlos Pérez on 12/06/18.
 */
public class ChatActivity extends AppCompatActivity {

    private MessagesAdapter myMessagesAdapter;
    private FirebaseAuth myAuthentication;
    private FirebaseDatabase myFirebaseDatabase;
    private DatabaseReference myDataBaseReference;

    private String userUID;
    private String userName;

    private String messageSentTimeRegistered = "";
    private String messageSentTimeParsedToUTC = "";

    @BindView(R.id.toolbar_chat) Toolbar topToolBar;
    @BindView(R.id.chat_recyclerView) RecyclerView _myMessagesRVList;
    @BindView(R.id.chat_write_message) EditText _writeTxtMessage;
    @BindView(R.id.chat_btn_send_message) Button _btnSendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        // Link de la toolbar.
        setSupportActionBar(topToolBar);

        // Recuperar datos de usuario actual.
        myAuthentication = FirebaseAuth.getInstance();
        myFirebaseDatabase = FirebaseDatabase.getInstance();

        FirebaseUser currentUser = myAuthentication.getCurrentUser();
        if (currentUser!=null) {
            userUID = currentUser.getUid();
            myDataBaseReference = myFirebaseDatabase.getReference("Users/" + userUID);
        } else {
            returnToLogin();
        }

        myDataBaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userName = user.getName();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myFirebaseDatabase = FirebaseDatabase.getInstance();
        myDataBaseReference = myFirebaseDatabase.getReference("ChatPublico");

        // Preparar lista de mensajes.
        myMessagesAdapter = new MessagesAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        // Lista de mensajes preparada.
        _myMessagesRVList.setLayoutManager(linearLayoutManager);
        _myMessagesRVList.setAdapter(myMessagesAdapter);

        // Aqui se va a enviar un nuevo mensaje.
        _btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!_writeTxtMessage.getText().toString().isEmpty()) {

                    // Recuperar datos del mensaje.
                    Message newMessage = new Message();
                    newMessage.setSenderName(userName);
                    newMessage.setSenderUID(userUID);
                    newMessage.setText(_writeTxtMessage.getText().toString());

//                // Convertir TimeStamp.
//                messageSentTimeRegistered = (DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date()).toString());
//                // Convertir timeStamp registrado a "Universal Time Coordinated".
//                messageSentTimeParsedToUTC = DateParsing.convertToUTC(messageSentTimeRegistered);
//                // Set del timestamp.
//                newMessage.setText(messageSentTimeParsedToUTC);

                    // Push a Firebase del nuevo mensaje.
                    myDataBaseReference.push().setValue(newMessage);
                }

                // Limpiar campo de texto de mensajes.
                _writeTxtMessage.setText("");

            }
        });

        // Observer para registrar cambios en los datos de la lista.
        myMessagesAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });

        // Listener para suscribir a los cambios en mensajes.
        myDataBaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message newMessageReceived = dataSnapshot.getValue(Message.class);
                // Añadir a la lista de mensajes.
                myMessagesAdapter.addMensaje(newMessageReceived);
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


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);


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
                returnToLogin();
                Log.i("ActionBar", "Logout!");

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    /**
     * Método para hacer que la lista de mensajes haga scroll al último mensaje.
     */
    private void setScrollbar() {
        _myMessagesRVList.scrollToPosition(myMessagesAdapter.getItemCount() - 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // En caso de implementar envio de imagenes, se  manejará aqui.


    }

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseUser currentUser = myAuthentication.getCurrentUser();
        if (currentUser != null) {
            //_btnSendMessage.setEnabled(false);
            DatabaseReference reference = myFirebaseDatabase.getReference("Users/" + currentUser.getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User currentUser = dataSnapshot.getValue(User.class);
                    userUID = currentUser.getUserUID();
                    userName = currentUser.getName();
              //      _btnSendMessage.setEnabled(true);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            returnToLogin();
        }
    }

    private void returnToLogin() {
        startActivity(new Intent(ChatActivity.this, LoginActivity.class));
        finish();
    }
}