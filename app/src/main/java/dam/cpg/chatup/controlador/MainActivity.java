package dam.cpg.chatup.controlador;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import dam.cpg.chatup.R;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar topToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Llamada a login al arrancar la app si no existe un login.TODO
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        // Link de la toolbar.
        setSupportActionBar(topToolBar);


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

            default:
                return super.onOptionsItemSelected(item);
        }


    }
}