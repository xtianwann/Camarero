package prg.pi.restaurantecamarero;

import prg.pi.restaurantecamarero.MainFragments;
import prg.pi.restaurantecamarero.conexion.Cliente;
import prg.pi.restaurantecamarero.decodificador.DecodificadorResultadoLogin;
import prg.pi.restaurantecamarero.preferencias.PreferenciasSet;
import prg.pi.restaurantecamarero.xml.XMLLogin;
import prg.pi.restaurantecamarero.xml.XMLLogout;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
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
	private AlertDialog.Builder dialog;
	private ConnectivityManager connMgr;
	private android.net.NetworkInfo wifi;
	
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
		connMgr = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
		wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(!wifi.isAvailable()){
			new WifiAsincrono().execute();
		}
		
		botonLogin.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				usuario = editTextLogin.getText().toString().toLowerCase();
				wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if(comprobarSenalWifi(wifi)){
					if(!usuario.equals(""))
						new LoginAsincrono().execute(usuario);
					else
						Toast.makeText(MainActivity.this, "Debes rellenar el campo usuario", Toast.LENGTH_SHORT).show();
				} else{
					dialog = new AlertDialog.Builder(MainActivity.this);
					dialog.setMessage("No se detecta señal wifi");
					dialog.setCancelable(false);
					dialog.setNeutralButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
								}
							});
					dialog.show();
				}
			}

		});
		botonLogout = (Button) findViewById(R.id.botonLogout);
		botonLogout.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if(comprobarSenalWifi(wifi)){
					if(!usuarioActual.equals("")){
						new LogoutAsincrono().execute(usuarioActual);
					} else {
						finish();
					}
				} else {
					dialog = new AlertDialog.Builder(MainActivity.this);
					dialog.setMessage("No se detecta señal wifi, si continua su usuario no se deslogueará del sistema");
					dialog.setCancelable(true);
					dialog.setNeutralButton("Continuar",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
								}
							});
					dialog.show();
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
	
	public boolean comprobarSenalWifi(android.net.NetworkInfo wifi){
		boolean resultado = false;
		if (wifi.getDetailedState() == DetailedState.CONNECTED)
			resultado = true;
		return resultado;
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
			try{
			cliente.init();
			} catch (NullPointerException e){
				return null;
			}
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
			if(pDialog != null)
				pDialog.dismiss();
			if(resultado != null){
				if(resultado){
					Intent intencion = new Intent(MainActivity.this, MainFragments.class);
					startActivity(intencion);
				} else {
					Toast.makeText(MainActivity.this, "Usuario incorrecto...", Toast.LENGTH_SHORT).show();
				}
			} else {
				dialog = new AlertDialog.Builder(MainActivity.this);
				dialog.setMessage("No se pudo conectar con el servidor");
				dialog.setCancelable(false);
				dialog.setNeutralButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				dialog.show();
			}
		}
		
	}
	
	public class LogoutAsincrono extends AsyncTask<String, String, Boolean> {
		
		protected void onPreExecute(){
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Saliendo...");
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
			try {
			cliente.init();
			} catch (NullPointerException e){
				return resultado;
			}
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
	
	public class WifiAsincrono extends AsyncTask<Void, Void, Boolean>{
		
		protected void onPreExecute(){
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Activando wifi del dispositivo...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean resultado = false;
			
			WifiManager wifiManager = (WifiManager) MainActivity.this
					.getSystemService(Context.WIFI_SERVICE);
			wifiManager.setWifiEnabled(true);
			while(!wifiManager.isWifiEnabled()){
				resultado = false;
			}
			usuarioActual = "";
			resultado = true;
			
			return resultado;
		}
		
		protected void onPostExecute(Boolean resultado){
			pDialog.dismiss();
			
			if(resultado){
				Toast.makeText(MainActivity.this, "Wifi activado", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(MainActivity.this, "Se ha producido algún error...", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
}
