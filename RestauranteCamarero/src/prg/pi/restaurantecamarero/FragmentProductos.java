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

		public interface ProductoListener {
			void onProductoSeleccionado(Producto producto);
		}

		public void setProductoListener(ProductoListener productoListener) {
			this.productoListener = productoListener;
		}

		class AdaptadorProductos extends ArrayAdapter<Producto> {

			Activity context;

			AdaptadorProductos(Fragment context) {
				super(context.getActivity(), R.layout.fragment_productos, cantidad.getProductos());
				this.context = context.getActivity();
			}

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
		public Cantidad getCantidad() {
			return cantidad;
		}

		public void setCantidad(Cantidad cantidad) {
			this.cantidad = cantidad;
		}
}
