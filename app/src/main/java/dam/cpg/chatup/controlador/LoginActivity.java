package dam.cpg.chatup.controlador;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import dam.cpg.chatup.R;

/**
 * Clase de login y acceso a registro de nuevos usuarios.
 *
 * @author Carlos Pérez on 7/06/18.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private FirebaseAuth myAuthentication;
    private boolean loginCompleted;

    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // Inicializacion de flag.
        loginCompleted = false;

        // Referencia a Firebase Authentication.
        myAuthentication = FirebaseAuth.getInstance();

        // Listener de login.
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
        // Listener de registro (Signup).
        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    /**
     * Método que procesa el intento de login. Utiliza Firebase Authentication.
     */
    public void login() {
        Log.d(TAG, "Login");

        // Si los datos introducidos no son validos retornamos el proceso.
        // En caso contrario se procede a validar en Firebase.
        if (!validate()) {
            onLoginFailed();
            return;
        }

        // Desactivar botón para evitar pulsar más de una vez mientras se valida el login.
        _loginButton.setEnabled(false);

        // Representación gráfica de espera mientras se valida en Firebase.
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();


        // TODO: Implement authentication logic here.
        // Si hemos llegado aqui procedemos a validar en Firebase Authentication.
        myAuthentication.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this, "Login correcto.", Toast.LENGTH_SHORT).show();

                            // Flag de registro completado con éxito.
                            loginCompleted = true;

                        } else {
                            // Flag de login completado sin éxito.
                            loginCompleted = false;
                        }

                    }
                });

        // Llamada a métodos de éxito o fallo y finalización de diálogo de progreso.
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        if (loginCompleted) {
                            onLoginSuccess();
                        } else {
                            onLoginFailed();
                        }
                        // Finalizar simulación gráfica de progreso.
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Desactivar la posibilidad de volver atrás a la MainActivity hasta que haya login.
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        // Recuperar estado de botón de login.
        _loginButton.setEnabled(true);
        // Iniciar siguiente activity.
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        // Finalizar actividad.
        finish();
    }

    public void onLoginFailed() {
        // Sign fails, display a message to the user.
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        // Recuperar estado de botón de login.
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

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

        return valid;
    }

    /**
     * Para controlar el ciclo de vida de la actividad, en caso de que la actividad vuelva
     * a primer plano. Hay que comprobar si existe un usuario autenticado para cargar la
     * actividad principal, si no existe hará su curso normal a "onCreate" en esta actividad.
     */
    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = myAuthentication.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(this, "Usuario logeado.", Toast.LENGTH_SHORT).show();
            nextActivity();
        }
    }

    private void nextActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}