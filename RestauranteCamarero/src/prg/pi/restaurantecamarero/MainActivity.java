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
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.preference.PreferenceManager;
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

/**
 * Actividad de inicio de la aplicaci�n, en ella se solicitar� al usuario que se 
 * identifique para que pueda empezar a trabajar. Sobre la marcha detecta si el dispositivo
 * tiene el wifi activado, en caso de no tenerlo lo activa. Tambi�n hace la funci�n de 
 * pantalla de salida en la que el usuario puede desloguarse del sistema.
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class MainActivity extends Activity {
	
	private EditText editTextLogin;
	private Button botonLogin, botonLogout;
	private String usuario;
	private ProgressDialog pDialog;
	private DecodificadorResultadoLogin decoResultadoLogin, decoResultadoLogout;
	private AlertDialog.Builder dialog;
	private ConnectivityManager connMgr;
	private android.net.NetworkInfo wifi;
	private static SharedPreferences preferencias;
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
		preferencias = PreferenceManager.getDefaultSharedPreferences(this);
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
		
		/* Bot�n login */
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
					dialog.setMessage("No se detecta se�al wifi");
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
		
		/* Bot�n logout */
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
					dialog.setMessage("No se detecta se�al wifi, si continua su usuario no se desloguear� del sistema");
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
	
	/**
	 * Permite obtener el nombre del usuario que est� usando actualmente el dispositivo
	 * 
	 * @return [String] nombre del usuario
	 */
	public static String getUsuarioActual(){
		return usuarioActual;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		
		return true;
		/** true -> el men� ya est� visible */
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
	
	/**
	 * Comprueba si se est� detectando se�al wifi
	 * 
	 * @param wifi [NetworkInfo] se�al wifi
	 * @return [boolean] true si detecta se�al, false en caso contrario
	 */
	public boolean comprobarSenalWifi(android.net.NetworkInfo wifi){
		boolean resultado = false;
		if (wifi.getDetailedState() == DetailedState.CONNECTED)
			resultado = true;
		
		return resultado;
	}
	
	/**
	 * Clase encargada de loguear al usuario evitando que la aplicaci�n quede bloqueada en 
	 * el proceso. Mientras se realiza la operaci�n se muestra una animaci�n de espera.
	 * 
	 * @author Juan G. P�rez Leo
	 * @author Cristian Mar�n Honor
	 */
	public class LoginAsincrono extends AsyncTask<String, String, Boolean> {
		
		/**
		 * Antes de iniciar la operaci�n incializa la animaci�n con las condiciones deseadas
		 */
		protected void onPreExecute(){
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Verificando...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(String... args) {
			/* Loguea al camarero y obtiene la respuesta del serrvidor */
			boolean resultado = false;
			
			XMLLogin xmlLogin = new XMLLogin(args[0]);
			String mensaje = xmlLogin.xmlToString(xmlLogin.getDOM());
			Cliente cliente = new Cliente(mensaje,getIpServidor());
			try{
			cliente.init();
			} catch (NullPointerException e){
				return null;
			}
			SystemClock.sleep(3000);
			decoResultadoLogin = cliente.getResultadoLogin();
			String resultadoLogin = decoResultadoLogin.getResultado();
			Log.e("resultadoLogin", resultadoLogin);
			if(resultadoLogin.equals("OK")){
				usuarioActual = args[0];
				resultado = true;
			}
			
			return resultado;
		}
		
		/**
		 * Cuando finaliza la operaci�n principal de la clase se cierra el di�logo y pasa a la
		 * pantalla principal de la aplicaci�n. En caso de que el usuario introducido sea incorrecto o falla
		 * la conexi�n con el servidor mostrar� un mensaje acorde.
		 */
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
	
	/**
	 * Clase encargada de realizar el proceso de deslogueo del usuario sin bloquear la 
	 * aplicaci�n. Una vez ha finalizado el proceso la aplicaci�n se cierra. Muestra una
	 * animaci�n de espera durante el proceso.
	 * 
	 * @author Juan G. P�rez Leo
	 * @author Cristian Mar�n Honor
	 */
	public class LogoutAsincrono extends AsyncTask<String, String, Boolean> {
		
		/**
		 * Antes de iniciar la operaci�n incializa la animaci�n con las condiciones deseadas
		 */
		protected void onPreExecute(){
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Saliendo...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(String... args) {
			/* Este m�todo se encarga de realizar el deslogueo del usuario */
			boolean resultado = false;
			
			XMLLogout xmlLogout = new XMLLogout(args[0]);
			String mensaje = xmlLogout.xmlToString(xmlLogout.getDOM());
			Cliente cliente = new Cliente(mensaje,getIpServidor());
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
		
		/**
		 * Una vez finalizado el proceso, se obtiene el resultado. Si todo fue bien la aplicaci�n
		 * se cerrar�, en caso contrario se mostrar� alg�n error.
		 */
		protected void onPostExecute(Boolean resultado){
			pDialog.dismiss();
			
			if(resultado){
				finish();
			} else {
				Toast.makeText(MainActivity.this, "Se ha producido alg�n error, int�ntalo de nuevo", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	/**
	 * Clase encargada de conectar el wifi del dispositivo en caso de que no lo estuviera.
	 * Muestra una animaci�n de espera durante el proceso.
	 * 
	 * @author Juan G. P�rez Leo
	 * @author Cristian Mar�n Honor
	 */
	public class WifiAsincrono extends AsyncTask<Void, Void, Boolean>{
		
		/**
		 * Antes de iniciar la operaci�n incializa la animaci�n con las condiciones deseadas
		 */
		protected void onPreExecute(){
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Activando wifi del dispositivo...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			/* Este m�todo se encarga de conectar el wifi del dispositivo */
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
		
		/**
		 * Obtiene el resultado de la operaci�n y muestra un mensaje con el mismo
		 */
		protected void onPostExecute(Boolean resultado){
			pDialog.dismiss();
			
			if(resultado){
				Toast.makeText(MainActivity.this, "Wifi activado", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(MainActivity.this, "Se ha producido alg�n error...", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	/**
	 * Permite obtener la ip del servidor
	 * 
	 * @return [String] ip del servidor
	 */
	public static String getIpServidor(){
		return preferencias.getString("ipServidor", null)+"";
	}
}
