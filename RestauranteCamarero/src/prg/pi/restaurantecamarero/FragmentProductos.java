package prg.pi.restaurantecamarero;

import prg.pi.restaurantecamarero.restaurante.Cantidad;
import prg.pi.restaurantecamarero.restaurante.Producto;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 
 * Fragment encargado de controlar los productos con los que interactua el camarero.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class FragmentProductos extends Fragment{
	private ListView listaProductos;
	private ProductoListener productoListener;
	private Cantidad cantidad;
	private int seleccionado = -1;
	private AdaptadorProductos adaptador;

		@Override
	    public View onCreateView(LayoutInflater inflater,
	                             ViewGroup container,
	                             Bundle savedInstanceState) {
	 
	        return inflater.inflate(R.layout.fragment_productos, container, false);
	    }
	    
	    @Override
		public void onActivityCreated(Bundle state) {
			super.onActivityCreated(state);

			listaProductos = (ListView) getView().findViewById(R.id.listaProductos);
			

		}
	    
	    /**
		 * 
		 * Clase encargada de mostrar los productos de lista de productos.
		 * 
		 * @author Juan G. Pérez Leo
		 * @author Cristian Marín Honor
		 */

		public class AdaptadorProductos extends ArrayAdapter<Producto> {

			Activity context;
			
			/**
		     * Constructor:
		     * 
		     * @param fragment [Fragment] Fragment en el que se encuentra el adaptador.
		     */

			AdaptadorProductos(Fragment context) {
				super(context.getActivity(), R.layout.fragment_productos, cantidad.getProductos());
				this.context = context.getActivity();
			}
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				LayoutInflater inflater = context.getLayoutInflater();
				View item = inflater.inflate(R.layout.productoslist, null);
				TextView labelProductoNombre = (TextView) item
						.findViewById(R.id.labelProductoNombre);
				labelProductoNombre.setText(cantidad.getProductos().get(position).getNombreProducto());
				if(seleccionado == position){
					labelProductoNombre.setBackgroundColor(Color.parseColor("#6E7172"));
		        }else{
		        	labelProductoNombre.setBackgroundColor(Color.TRANSPARENT);
		        }
				return (item);
			}
		}
		
		/**
	     * Muerstra los productos en la lista de la cantidad seleccionada.
	     * 
	     * @param cantidad [Cantidad] Cantidad seleccionada.
	     */
		
		public void rellenarProductos(Cantidad cantidad){
			this.cantidad = cantidad;
			adaptador = new AdaptadorProductos(this);
			listaProductos.setAdapter(adaptador);
			seleccionado = -1;
			listaProductos.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> list, View view, int pos,
						long id) {
					if (productoListener != null) {
						seleccionado = pos;
						adaptador.notifyDataSetChanged();
						((Producto)listaProductos.getAdapter().getItem(pos)).setCantidadPadre(getCantidad().getNombre());
						productoListener.onProductoSeleccionado(((Producto)listaProductos.getAdapter().getItem(pos)));
					}
				}
			});
		}
		
		/**
		 * 
		 * 
		 * Interface para la comunicación con la clase principal.
		 * 
		 * @author Juan G. Pérez Leo
		 * @author Cristian Marín Honor
		 */

		public interface ProductoListener {
			/**
		     * Comunica el producto seleccionado.
		     * 
		     * @param producto [Producto] Producto seleccionado.
		     */
			public void onProductoSeleccionado(Producto producto);
		}
		
		/**
	     * Permite modificar el listener. 
	     * 
	     * @param productoListener [ProductoListener] Listener asignado.
	     */

		public void setProductoListener(ProductoListener productoListener) {
			this.productoListener = productoListener;
		}
		
		/**
	     * Permite obtener la cantidad
	     * 
	     * @return [Cantidad] Cantidad
	     */
		
		public Cantidad getCantidad() {
			return cantidad;
		}
		/**
	     * Permite modificar la cantidad. 
	     * 
	     * @param cantidad [Cantidad] Cantidad.
	     */

		public void setCantidad(Cantidad cantidad) {
			this.cantidad = cantidad;
		}
}
