package prg.pi.restaurantecamarero;

import java.util.ArrayList;

import prg.pi.restaurantecamarero.preferencias.Preferencias;
import prg.pi.restaurantecamarero.preferencias.PreferenciasSet;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends Activity {
	private Spinner spinnerUsuarios;
	private Button botonLogin;
	private String[] usuarios = {"Manolin","Pepe"};
	private int seleccionado = 0;
	private ArrayAdapter<String> adaptador;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.login);
		adaptador = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, usuarios);
		spinnerUsuarios = (Spinner) findViewById(R.id.spinnerLogin);
		spinnerUsuarios.setAdapter(adaptador);
		botonLogin = (Button) findViewById(R.id.botonLogin);
		botonLogin.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				if (seleccionado > 0) {
					String usuario = usuarios[seleccionado];
					// Mandar el nombre del user al servidor y si funciona
					// Intent intencion = new Intent(this,MainFragments.class);
					// intent.putExtra("usuario", idUsuario);
					// startActivity(intencion);
					// En MainFragments
					// Bundle extras = getIntent().getExtras();
					// int idUsuario = extras.getInt("idUsuario");
				}
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
		/** true -> el menú ya está visible */
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.configuracion:
			startActivity(new Intent(this, PreferenciasSet.class));
			break;
		}

		return true;
		/** true -> consumimos el item, no se propaga */
	}
}
