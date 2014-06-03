package prg.pi.restaurantecamarero;

import java.io.IOException;
import java.util.ArrayList;

import prg.pi.restaurantecamarero.conexion.Cliente;
import prg.pi.restaurantecamarero.decodificador.DecodificadorDameloTodo;
import prg.pi.restaurantecamarero.restaurante.Mesa;
import prg.pi.restaurantecamarero.restaurante.Producto;
import prg.pi.restaurantecamarero.restaurante.Seccion;
import prg.pi.restaurantecamarero.xml.XMLDameloTodo;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * Fragment encargado de controlar las secciones y mesas con las que interactua
 * el camarero.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class FragmentSeccionMesas extends Fragment {
	private Spinner seccion;
	private Spinner mesa;
	private Mesa mesaSeleccionada = null;
	private Seccion secciones[];
	public ArrayList<Mesa> mesasActivas = new ArrayList<Mesa>();
	private ArrayAdapter<String> adaptadorSeccion;
	private ArrayAdapter<String> adaptadorMesa;
	private int posicionSeccion = 0;
	private int posicionMesa = 0;
	private SeccionesMesasListener seccionesMesasListener;
	private SeccionesThread hilo;
	private AlertDialog.Builder dialog;
	private DecodificadorDameloTodo decoTodo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.mesasseccionspinner, container, false);
	}

	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);
		decoTodo = null;
		final ConnectivityManager connMgr = (ConnectivityManager) getView()
				.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		final android.net.NetworkInfo wifi = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifi.isAvailable()) {
			if (wifi.getDetailedState() == DetailedState.CONNECTED) {
				iniciarHilo();
			} else {
				dialog = new AlertDialog.Builder(getView().getContext());
				dialog.setMessage("No se detecta señal wifi");
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
		} else {
			dialog = new AlertDialog.Builder(getView().getContext());
			dialog.setMessage("El wifi no esta activado");
			dialog.setCancelable(false);
			dialog.setNeutralButton("Activar",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							activarWifi();
							try {
								Thread.sleep(15000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							iniciarHilo();
							dialog.cancel();
						}
					});
			dialog.show();
		}
	}

	/**
	 * Inicia el hilo para recibir las secciones y mesas del servidor.
	 * 
	 */

	public void iniciarHilo() {
		hilo = new SeccionesThread();
		hilo.start();
	}

	/**
	 * Devuelve el nombre de las secciones.
	 * 
	 * @return [String[]] Nombre de las secciones.
	 */

	private String[] dameSecciones() {
		ArrayList<String> seccionesArray = new ArrayList<String>();
		try {
			for (Seccion seccion : secciones)
				seccionesArray.add(seccion.getNombre());
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return seccionesArray.toArray(new String[0]);
	}

	/**
	 * Devuelve el nombre de las mesas de la sección seleccionada
	 * 
	 * @param seccion
	 *            [Seccion] Sección seleccionada
	 * @return [String[]] Nombres de las mesas.
	 */

	private String[] dameMesas(Seccion seccion) {
		ArrayList<String> mesasArray = new ArrayList<String>();
		for (Mesa mesa : seccion.getMesas())
			mesasArray.add(mesa.getNombre());
		return mesasArray.toArray(new String[0]);
	}

	/**
	 * 
	 * Clase encargada de pedir al servidor las secciones y mesas almacenadas en
	 * la base de datos.
	 * 
	 * @author Juan G. Pérez Leo
	 * @author Cristian Marín Honor
	 */

	public class SeccionesThread extends Thread {
		private Seccion seccionesT[];

		@Override
		public void run() {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					XMLDameloTodo xml = new XMLDameloTodo();
					String mensaje = xml.xmlToString(xml.getDOM());
					Log.e("SeccionesThread", "he llegado MESAS");
					Cliente c = new Cliente(mensaje, MainActivity
							.getIpServidor());

					try {
						c.init();
						seccionesMesasListener.onIniciarHiloCantidad();
						Log.e("SeccionesThread", "he terminado MESAS");
						decoTodo = c.getTodo();
						seccionesT = decoTodo.getSecciones().toArray(
								new Seccion[0]);
						secciones = seccionesT;
						seccion = (Spinner) getView().findViewById(
								R.id.spinnerSeccion);
						mesa = (Spinner) getView().findViewById(
								R.id.spinnerMesas);
						adaptadorSeccion = new ArrayAdapter<String>(getView()
								.getContext(),
								android.R.layout.simple_spinner_item,
								dameSecciones());
						adaptadorSeccion
								.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						seccion.setAdapter(adaptadorSeccion);

						seccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> parent,
									View view, int pos, long id) {
								posicionSeccion = pos;
								Seccion seccion = secciones[pos];
								adaptadorMesa = new ArrayAdapter<String>(
										getView().getContext(),
										android.R.layout.simple_spinner_item,
										dameMesas(seccion));
								adaptadorMesa
										.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
								mesa.setAdapter(adaptadorMesa);
								mesaSeleccionada = seccion.getMesas().get(0);
								mesa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
									@Override
									public void onItemSelected(
											AdapterView<?> parent, View view,
											int pos, long id) {
										posicionMesa = pos;
										mesaSeleccionada = secciones[posicionSeccion]
												.getMesas().get(pos);
									}

									@Override
									public void onNothingSelected(
											AdapterView<?> parent) {
										// Do nothing, just another required
										// interface callback
									}
								});
							}

							@Override
							public void onNothingSelected(AdapterView<?> parent) {
								// Do nothing, just another required interface
								// callback
							}
						});
						seccion.setOnTouchListener(new View.OnTouchListener() {
							@Override
							public boolean onTouch(View v, MotionEvent event) {
								if (seccionesMesasListener.onExistenPedidos()) {
									seccion.setClickable(false);
									mostrarNotificacion();
								} else {
									seccion.setClickable(true);
								}
								return false;
							}
						});
						mesa.setOnTouchListener(new View.OnTouchListener() {
							@Override
							public boolean onTouch(View v, MotionEvent event) {
								if (seccionesMesasListener.onExistenPedidos()) {
									mesa.setClickable(false);
									mostrarNotificacion();
								} else {
									mesa.setClickable(true);
								}
								return false;
							}

						});
					} catch (NullPointerException e) {
						dialog = new AlertDialog.Builder(getView().getContext());
						dialog.setMessage("No se pudo conectar con el servidor¿Reintentar?.");
						dialog.setCancelable(false);
						dialog.setNeutralButton("OK",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										iniciarHilo();
										dialog.cancel();
									}
								});
						dialog.show();
					}
				}

			});
		}
		/**
	     * Devuelve la lista de secciones.
	     * 
	     * @return [Seccion[]] Lista de secciones.
	     */

		public Seccion[] getSeccionesT() {
			return seccionesT;
		}
		/**
	     * Permite modificar la lista de secciones.
	     * 
	     * @param seccionesT [Seccion[]] Lista de secciones.
	     */
		public void setSeccionesT(Seccion[] seccionesT) {
			this.seccionesT = seccionesT;
		}
	}
	
	/**
     * Devuelve la mesa seleccionada.
     * 
     * @return [Mesa] Mesa seleccionada.
     */

	public Mesa getMesaSeleccionada() {
		return mesaSeleccionada;
	}
	//Mirar
	/**
	 * 
     * Añade la mesa seleccionada.
     * 
     * @param mesa [Mesa] Mesa seleccionada.
     */

	public void addMesaActiva(Mesa mesa) {
		if (!mesasActivas.contains(mesa))
			mesasActivas.add(mesa);
	}
	/**
	 * 
	 * 
	 * Interface para la comunicación con la clase principal.
	 * 
	 * @author Juan G. Pérez Leo
	 * @author Cristian Marín Honor
	 */

	public interface SeccionesMesasListener {
		/**
	     * Recibe si existen pedidos en la comanda actual.
	     * 
	     * @return [boolean] true si existen pedidos, false si no existen.
	     */
		public boolean onExistenPedidos();
		/**
	     * Envia los pedidos de la comanda actual.
	     * 
	     */
		public void onEnviarPedidosSinEnviar();
		/**
	     * Inicia el hilo del fragment responsable de las cantidades.
	     * 
	     */
		public void onIniciarHiloCantidad();

	}
	/**
     * Permite modificar el listener. 
     * 
     * @param seccionesMesasListener [SeccionesMesasListener] Listener asignado.
     */
	public void setSeccionesMesasListener(
			SeccionesMesasListener seccionesMesasListener) {
		this.seccionesMesasListener = seccionesMesasListener;
	}
	
	/**
     * Muestra una notificación en el caso de cambiar de mesa sin enviar los pedidos de la misma.
     * 
     */
	public void mostrarNotificacion() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(getView()
				.getContext());
		dialog.setMessage("Hay pedidos sin enviar¿Desea enviarlos?");
		dialog.setCancelable(false);
		dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				seccion.setClickable(true);
				mesa.setClickable(true);
				seccionesMesasListener.onEnviarPedidosSinEnviar();
				dialog.cancel();
			}
		});
		dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		dialog.show();
	}
	
	/**
     * Devuelve la hilo de las secciones.
     * 
     * @return [SeccionesThread] Thread de secciones.
     */

	public SeccionesThread getHilo() {
		return hilo;
	}
	
	/**
     * Activa el wifi del dispositivo.
     * 
     */

	private void activarWifi() {
		WifiManager wifiManager = (WifiManager) getView().getContext()
				.getSystemService(Context.WIFI_SERVICE);
		wifiManager.setWifiEnabled(true);
	}
	
	/**
     * Devuelve el decodificador del mensaje DameloTodo.
     * 
     * @return [DecodificadoDameloTodo] Decodificador de mensaje DameloTodo.
     */
	public DecodificadorDameloTodo getDecoTodo() {
		return decoTodo;
	}

}
