package prg.pi.restaurantecamarero;

import prg.pi.restaurantecamarero.MainFragments;
import prg.pi.restaurantecamarero.conexion.Cliente;
import prg.pi.restaurantecamarero.decodificador.DecodificadorResultadoLogin;
import prg.pi.restaurantecamarero.preferencias.PreferenciasSet;
import prg.pi.restaurantecamarero.xml.XMLLogin;
import prg.pi.restaurantecamarero.xml.XMLLogout;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private EditText editTextLogin;
	private Button botonLogin, botonLogout;

	private String usuario;
	private ProgressDialog pDialog;
	private DecodificadorResultadoLogin decoResultadoLogin, decoResultadoLogout;
	
	private static String usuarioActual;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.login);
		decoResultadoLogin = null;
		decoResultadoLogout = null;
		usuarioActual = "";
		editTextLogin = (EditText) findViewById(R.id.editText_login);
		botonLogin = (Button) findViewById(R.id.botonLogin);
		botonLogin.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				usuario = editTextLogin.getText().toString().toLowerCase();
				if(!usuario.equals(""))
					new LoginAsincrono().execute(usuario);
				else
					Toast.makeText(MainActivity.this, "Debes rellenar el campo usuario", Toast.LENGTH_SHORT).show();
			}

		});
		botonLogout = (Button) findViewById(R.id.botonLogout);
		botonLogout.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				if(!usuarioActual.equals("")){
					new LogoutAsincrono().execute(usuarioActual);
				} else {
					finish();
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
	
	public class LoginAsincrono extends AsyncTask<String, String, Boolean> {
		
		protected void onPreExecute(){
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Verificando...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(String... args) {
			boolean resultado = false;
			
			XMLLogin xmlLogin = new XMLLogin(args[0]);
			String mensaje = xmlLogin.xmlToString(xmlLogin.getDOM());
			Cliente cliente = new Cliente(mensaje);
			cliente.init();
			SystemClock.sleep(1000);
			decoResultadoLogin = cliente.getResultadoLogin();
			String resultadoLogin = decoResultadoLogin.getResultado();
			if(resultadoLogin.equals("OK")){
				usuarioActual = args[0];
				resultado = true;
			}
			
			return resultado;
		}
		
		protected void onPostExecute(Boolean resultado){
			pDialog.dismiss();
			
			if(resultado){
				Intent intencion = new Intent(MainActivity.this, MainFragments.class);
				startActivity(intencion);
			} else {
				Toast.makeText(MainActivity.this, "Usuario incorrecto...", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	public class LogoutAsincrono extends AsyncTask<String, String, Boolean> {
		
		protected void onPreExecute(){
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Deslogueando...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(String... args) {
			boolean resultado = false;
			
			XMLLogout xmlLogout = new XMLLogout(args[0]);
			String mensaje = xmlLogout.xmlToString(xmlLogout.getDOM());
			Cliente cliente = new Cliente(mensaje);
			cliente.init();
			SystemClock.sleep(1000);
			decoResultadoLogout = cliente.getResultadoLogin();
			String resultadoLogout = decoResultadoLogout.getResultado();
			if(resultadoLogout.equals("OK")){
				usuarioActual = "";
				resultado = true;
			}
			
			return resultado;
		}
		
		protected void onPostExecute(Boolean resultado){
			pDialog.dismiss();
			
			if(resultado){
				finish();
			} else {
				Toast.makeText(MainActivity.this, "Se ha producido algún error, inténtalo de nuevo", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
