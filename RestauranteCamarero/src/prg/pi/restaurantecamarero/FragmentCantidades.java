package prg.pi.restaurantecamarero;

import prg.pi.restaurantecamarero.restaurante.Cantidad;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 
 * Fragment encargado de controlar las cantidades de los productos con las que interactua el camarero.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class FragmentCantidades extends Fragment {

	private Cantidad cantidades[];
	private ListView listaCantidades;
	private CantidadListener cantidadListener;
	private AdaptadorCantidades adaptador;
	private int seleccionado = -1;
	private CantidadesThread hilo;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_cantidades, container, false);
	}

	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);
	}
	/**
     * Inicia el hilo para pedir las cantidades de los productos al servidor.
     * 
     */

	public void iniciarHilo() {
		hilo = new CantidadesThread(this);
		hilo.start();
	}
	/**
	 * 
	 * 
	 * Interface para la comunicación con la clase principal.
	 * 
	 * @author Juan G. Pérez Leo
	 * @author Cristian Marín Honor
	 */

	public interface CantidadListener {
		/**
	     * Comunica la cantidad seleccionada a la clase principal.
	     * 
	     * @param cantidad [Cantidad] Cantidad seleccionada.
	     */
		public void onCantidadSeleccionada(Cantidad cantidad);
		/**
	     * Recibe las cantidades a añadir a la lista de cantidades disponibles.
	     * 
	     * @return [Cantidad[]] Lista de cantidades a añadir.
	     */
		public Cantidad[] onHiloTerminado();
	}
	
	/**
     * Permite modificar el listener. 
     * 
     * @param cantidadListener [CantidadListener] Listener asignado.
     */

	public void setCantidadListener(CantidadListener cantidadListener) {
		this.cantidadListener = cantidadListener;
	}
	/**
	 * 
	 * Clase encargada de mostrar las cantidades de lista de cantidades.
	 * 
	 * @author Juan G. Pérez Leo
	 * @author Cristian Marín Honor
	 */

	public class AdaptadorCantidades extends ArrayAdapter<Cantidad> {

		Activity context;
		
		/**
	     * Constructor:
	     * 
	     * @param fragment [Fragment] Fragment en el que se encuentra el adaptador.
	     */

		AdaptadorCantidades(Fragment fragment) {
			super(fragment.getActivity(), R.layout.fragment_cantidades,
					cantidades);
			this.context = fragment.getActivity();
		}
		@Override
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
	/**
	 * 
	 * Clase encargada de pedir al servidor las cantidades almacenadas en la base de datos.
	 * 
	 * @author Juan G. Pérez Leo
	 * @author Cristian Marín Honor
	 */

	private class CantidadesThread extends Thread {
		private Fragment fragment;
		/**
	     * Constructor:
	     * 
	     * @param fragment [Fragment] Fragment en el que se encuentra el hilo.
	     */
		public CantidadesThread(Fragment fragment) {
			this.fragment = fragment;
		}
		@Override
		public void run() {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Log.e("CantidadesThread", "he llegado CANTIDADES");
					cantidades = cantidadListener.onHiloTerminado();
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
	
	/**
     * Devuelve el hilo que recoge las cantidades a mostrar en la lista de cantidades.
     * 
     * @return CantidadThread Hilo de cantidades.
     */

	public CantidadesThread getHilo() {
		return hilo;
	}
}
