package prg.pi.restaurantecamarero;

import java.util.ArrayList;

import prg.pi.restaurantecamarero.conexion.Cliente;
import prg.pi.restaurantecamarero.restaurante.Mesa;
import prg.pi.restaurantecamarero.restaurante.Producto;
import prg.pi.restaurantecamarero.restaurante.Seccion;
import prg.pi.restaurantecamarero.xml.XMLDameloTodo;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.mesasseccionspinner, container, false);
	}

	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);
		hilo = new SeccionesThread();
		hilo.start();
		try {
			hilo.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String[] dameSecciones() {
		ArrayList<String> seccionesArray = new ArrayList<String>();
		for (Seccion seccion : secciones)
			seccionesArray.add(seccion.getNombre());
		return seccionesArray.toArray(new String[0]);
	}

	private String[] dameMesas(Seccion seccion) {
		ArrayList<String> mesasArray = new ArrayList<String>();
		for (Mesa mesa : seccion.getMesas())
			mesasArray.add(mesa.getNombre());
		return mesasArray.toArray(new String[0]);
	}

	class SeccionesThread extends Thread {
		private Seccion seccionesT[];

		public void run() {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					XMLDameloTodo xml = new XMLDameloTodo();
					String mensaje = xml.xmlToString(xml.getDOM());
					Cliente c = new Cliente(mensaje);
					c.run();
					try {
						c.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					seccionesT = c.getTodo().getSecciones()
							.toArray(new Seccion[0]);
					secciones = seccionesT;
					seccion = (Spinner) getView().findViewById(
							R.id.spinnerSeccion);
					mesa = (Spinner) getView().findViewById(R.id.spinnerMesas);
					adaptadorSeccion = new ArrayAdapter<String>(getView()
							.getContext(),
							android.R.layout.simple_spinner_item,
							dameSecciones());
					adaptadorSeccion
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					seccion.setAdapter(adaptadorSeccion);

					seccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
						public void onItemSelected(AdapterView<?> parent,
								View view, int pos, long id) {
							posicionSeccion = pos;
							Seccion seccion = secciones[pos];
							adaptadorMesa = new ArrayAdapter<String>(getView()
									.getContext(),
									android.R.layout.simple_spinner_item,
									dameMesas(seccion));
							adaptadorMesa
									.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							mesa.setAdapter(adaptadorMesa);
							mesaSeleccionada = seccion.getMesas().get(0);
							mesa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
								public void onItemSelected(
										AdapterView<?> parent, View view,
										int pos, long id) {
									posicionMesa = pos;
									mesaSeleccionada = secciones[posicionSeccion]
											.getMesas().get(pos);
								}

								public void onNothingSelected(
										AdapterView<?> parent) {
									// Do nothing, just another required
									// interface callback
								}
							});
						}

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
							}
							else{
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
							}
							else{
								mesa.setClickable(true);
							}
							return false;
						}
					});
				}
			});
		}

		public Seccion[] getSeccionesT() {
			return seccionesT;
		}

		public void setSeccionesT(Seccion[] seccionesT) {
			this.seccionesT = seccionesT;
		}
	}

	public Mesa getMesaSeleccionada() {
		return mesaSeleccionada;
	}

	public void addMesaActiva(Mesa mesa) {
		if (!mesasActivas.contains(mesa))
			mesasActivas.add(mesa);
	}

	public interface SeccionesMesasListener {
		public boolean onExistenPedidos();

		public void onBorrarPedidos();

	}

	public void setSeccionesMesasListener(
			SeccionesMesasListener seccionesMesasListener) {
		this.seccionesMesasListener = seccionesMesasListener;
	}

	public void mostrarNotificacion() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(getView()
				.getContext());
		dialog.setMessage("Los pedidos sin enviar se borraran¿Continuar?");
		dialog.setCancelable(false);
		dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				seccion.setClickable(true);
				mesa.setClickable(true);
				seccionesMesasListener.onBorrarPedidos();
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

}
