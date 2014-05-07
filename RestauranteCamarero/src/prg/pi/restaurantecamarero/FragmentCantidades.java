package prg.pi.restaurantecamarero;

import java.util.ArrayList;

import prg.pi.restaurantecamarero.FragmentSeccionMesas.SeccionesThread;
import prg.pi.restaurantecamarero.conexion.Cliente;
import prg.pi.restaurantecamarero.restaurante.Cantidad;
import prg.pi.restaurantecamarero.restaurante.Producto;
import prg.pi.restaurantecamarero.restaurante.Seccion;
import prg.pi.restaurantecamarero.xml.XMLDameloTodo;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class FragmentCantidades extends Fragment {

	private ArrayList<Producto> productos1 = new ArrayList<Producto>();
	private ArrayList<Producto> productos2 = new ArrayList<Producto>();
	private Cantidad cantidades[];
	private ListView listaCantidades;
	private CantidadListener cantidadListener;
	private AdaptadorCantidades adaptador;
	private int seleccionado = -1;
	private CantidadesThread hilo;
	private AlertDialog.Builder dialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_cantidades, container, false);
	}

	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);
		iniciarHilo();
	}
	public void iniciarHilo(){
		hilo = new CantidadesThread(this);
		hilo.start();
	}

	public interface CantidadListener {
		void onCantidadSeleccionada(Cantidad cantidad);
	}

	public void setCantidadListener(CantidadListener cantidadListener) {
		this.cantidadListener = cantidadListener;
	}

	class AdaptadorCantidades extends ArrayAdapter<Cantidad> {

		Activity context;

		AdaptadorCantidades(Fragment context) {
			super(context.getActivity(), R.layout.fragment_cantidades,
					cantidades);
			this.context = context.getActivity();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(R.layout.cantidadeslist, null);
			TextView labelCantidadNombre = (TextView) item
					.findViewById(R.id.labelCantidadNombre);
			labelCantidadNombre.setText(cantidades[position].getNombre());
			if (seleccionado == position) {
				labelCantidadNombre.setBackgroundColor(Color
						.parseColor("#6E7172"));
			} else {
				labelCantidadNombre.setBackgroundColor(Color.TRANSPARENT);
			}
			return (item);
		}
	}

	class CantidadesThread extends Thread {
		private Cantidad cantidadesT[];
		private Fragment fragment;

		public CantidadesThread(Fragment fragment) {
			this.fragment = fragment;
		}

		public void run() {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					XMLDameloTodo xml = new XMLDameloTodo();
					String mensaje = xml.xmlToString(xml.getDOM());
					Cliente c = new Cliente(mensaje, getView().getContext());
					c.run();
					try {
						c.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						cantidadesT = c.getTodo().getCantidades()
								.toArray(new Cantidad[0]);
					} catch (NullPointerException e) {

					}
					cantidades = cantidadesT;
					listaCantidades = (ListView) getView().findViewById(
							R.id.listaCategorias);
					try {
						adaptador = new AdaptadorCantidades(fragment);
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
					listaCantidades.setAdapter(adaptador);

					listaCantidades
							.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> list,
										View view, int pos, long id) {
									if (cantidadListener != null) {
										seleccionado = pos;
										adaptador.notifyDataSetChanged();
										cantidadListener
												.onCantidadSeleccionada((Cantidad) listaCantidades
														.getAdapter().getItem(
																pos));
									}
								}

							});
				}
			});
		}
	}

	public CantidadesThread getHilo() {
		return hilo;
	}
}
