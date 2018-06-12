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
 * En esta clase se registraran nuevos usuarios en Firebase.
 * Se utiliza Firebase Authentication para el usuario y password.
 * Se crea un nodo de usuarios en la base de datos.
 *
 * @author Carlos Pérez on 11/06/18.
 */
public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    private FirebaseAuth myAuthentication;
    private FirebaseDatabase myFirebaseDatabase;
    private boolean signUpCompleted;

    @BindView(R.id.input_name)
    EditText _nameText;
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup)
    Button _signupButton;
    @BindView(R.id.link_login)
    TextView _loginLink;

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
     * Método que procesa el intento de registro. Utiliza Firebase Authentication y Database.
     */
    public void signup() {
        Log.d(TAG, "Signup");

        // Si las validaciones fallan lo comunica al usuario y retorna el proceso.
        if (!validate()) {
            onSignupFailed();
            return;
        }

        // En caso de no fallar la validación continua el flujo.
        // Desactiva botón de registro mientras se crea la cuenta.
        _signupButton.setEnabled(false);

        // Simulacion gráfica con un dialog animado mientras se crea la cuenta.
        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        // Recogida de los datos introducidos.
        final String name = _nameText.getText().toString();
        final String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        //String reEnterPassword = _reEnterPasswordText.getText().toString();

        // TODO: Implement signup logic here.
        // Si el intento de crear una cuenta de usuario con email y password en Authentication funciona
        // Entonces se intentará registrar el usuario como usuario en la base de datos también.
        // Esto va a permitir no tener las contraseñas en la base de datos.
        myAuthentication.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignupActivity.this, "Se registro correctamente.", Toast.LENGTH_SHORT).show();
                            User user = new User(email, name);
                            // Recuperamos el UID del usuario autenticado para hacerlo coincidir con la referencia en la BD.
                            FirebaseUser currentUser = myAuthentication.getCurrentUser();
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

        // Llamada a métodos de éxito o fallo y finalización de diálogo de progreso.
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed depending on success
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
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        // Finalizar actividad.
        finish();
    }

    /**
     * Este método avisa de un registro fallido.
     */
    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        // Activar botón de nuevo para intentar otro login.
        _signupButton.setEnabled(true);
    }

    /**
     * Método de validación de datos introducidos.
     *
     * @return valid
     */
    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }
}
