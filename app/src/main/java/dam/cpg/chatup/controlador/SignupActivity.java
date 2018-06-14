package dam.cpg.chatup.controlador;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import dam.cpg.chatup.R;
import dam.cpg.chatup.modelo.User;

/**
 * Clase de registro de nuevos usuarios.
 * Se registraran nuevos usuarios en Firebase Authentication con email y password.
 * Se creará un nuevo usuario en la base de datos por cada usuario registrado con nombre y email.
 *
 * @author Carlos Pérez on 11/06/18.
 */
public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    private FirebaseAuth myAuthentication;
    private FirebaseDatabase myFirebaseDatabase;
    private boolean signUpCompleted;

    @BindView(R.id.input_name)  EditText _nameText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        // Inicializacion de flag.
        signUpCompleted = false;

        // Referencias a Authentication y Database.
        myAuthentication = FirebaseAuth.getInstance();
        myFirebaseDatabase = FirebaseDatabase.getInstance();

        // Listener del botón de registro.
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        // Listener para "ir al login" desde esta actividad, hace un "slide" de transición.
        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    /**
     * Método que procesa el intento de registro. Utiliza Firebase Authentication y Firebase Database.
     * Se intenta registrar un usuario en Firebase Authentication con email y password.
     * Si el intento de crear el usuario en Authentication funciona, entonces se intentará
     * registrar el mismo usuario en la base de datos con el mismo UID, name, email.
     * Esto permite no exponer las contraseñas en la base de datos.
     */
    public void signup() {
        Log.d(TAG, "Signup");

        // Si los datos introducidos no son validos retornamos el proceso.
        // En caso contrario se procede a registrar en Firebase.
        if (!validate()) {
            onSignupFailed();
            return;
        }

        // Desactiva botón de registro mientras se crea la cuenta para evitar pulsar más de una vez.
        _signupButton.setEnabled(false);

        // Representación gráfica de espera mientras se registra la cuenta.
        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.signup_creating_account));
        progressDialog.show();

        // Recogida de los datos introducidos.
        final String name = _nameText.getText().toString();
        final String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement signup logic here.
        // Si hemos llegado aqui procedemos a crear la cuenta en Firebase Authentication.
        myAuthentication.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Logs para controlar resultados.
                        if (!task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Failed=" + task.getException().getMessage());
                        } else {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        }
                        // Si el registro en Auth tuvo exito hay que añadir el usuario a la BD.
                        if (task.isSuccessful()) {

                            User user = new User();
                            user.setName(name);
                            user.setEmail(email);
                            // Recuperar UID del usuario autenticado para hacerlo coincidir con el usuario en la BD.
                            FirebaseUser currentUser = myAuthentication.getCurrentUser();
                            // Esta linea también crea el nodo si no existe en la BD.
                            DatabaseReference reference = myFirebaseDatabase.getReference("Users/" + currentUser.getUid());
                            // Ahora el usuario autenticado y el de la BD, tendran la misma referencia (clave).
                            reference.setValue(user);

                            // Flag de registro completado con exito.
                            signUpCompleted = true;

                        } else {
                            // Flag de registro completado sin éxito.
                            signUpCompleted = false;
                        }
                    }
                });

        // Llamada a métodos de éxito o fallo y finalización de diálogo de progreso. Sirve para sincronizar el interfaz.
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        if (signUpCompleted) {
                            onSignupSuccess();
                        } else {
                            onSignupFailed();
                        }
                        // Finalizar simulación gráfica de progreso.
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    /**
     * Este método representa un registro exitoso.
     */
    public void onSignupSuccess() {
        Toast.makeText(SignupActivity.this, getString(R.string.signup_account_successful), Toast.LENGTH_SHORT).show();
        _signupButton.setEnabled(true);
        // Devolver código de éxito al intent que inició la actividad.
        setResult(RESULT_OK, null);
        // Finalizar actividad.
        finish();
    }

    /**
     * Este método representa un registro fallido.
     */
    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), getString(R.string.signup_account_failed), Toast.LENGTH_LONG).show();
        // Activar botón de nuevo para intentar otro registro.
        _signupButton.setEnabled(true);
    }

    /**
     * Método de validación de datos introducidos por el usuario.
     * Campos: name , email , password , reEnterPassword.
     *
     * @return Devuelve true en caso de ser válidos, false en caso contrario.
     */
    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError(getString(R.string.signup_validate_name));
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError(getString(R.string.signup_validate_email));
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError(getString(R.string.signup_validate_password));
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError(getString(R.string.signup_validate_password_match));
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }
}
