package prg.pi.restaurantecamarero;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import prg.pi.restaurantecamarero.FragmentResumen.Calculadora;
import prg.pi.restaurantecamarero.restaurante.Pedido;
import prg.pi.restaurantecamarero.restaurante.Producto;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ActivityPedidosPendientes extends Activity{
	private ListView pedidos;
	private Button limpiar,cambiar,mas,menos,x;
	private Calculadora calculadora;
	public HashMap<Pedido, Integer> pedidoMap = new HashMap<Pedido, Integer>();
	private int seleccionado = -1;
	private AdaptadorResumen adaptador;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //Remove title bar
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);

	    //Remove notification bar
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.pedidos_pendientes);
        prepararListeners();
    }
	
	private class AdaptadorResumen extends BaseAdapter {
		private LayoutInflater mInflater;

		public AdaptadorResumen(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return pedidoMap.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			PedidoPendiente pedido;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.pedidos_pendientes_list, null);
				pedido = new PedidoPendiente();
				pedido.cantidadTexto = (TextView) convertView
						.findViewById(R.id.TextView01);
				pedido.productoTexto = (TextView) convertView
						.findViewById(R.id.TextView02);

				convertView.setTag(pedido);
			} else {
				pedido = (PedidoPendiente) convertView.getTag();
			}

			Iterator iterador = pedidoMap.entrySet().iterator();
			if (iterador.hasNext()) {
				ArrayList<Producto> productos = new ArrayList(pedidoMap.keySet());
				String producto = productos.get(position).getNombreProducto();
				String categoria = productos.get(position).getCantidadPadre();
				int cantidad = (Integer) (new ArrayList(pedidoMap.values()))
						.get(position);
				pedido.cantidadTexto.setText(cantidad + "");
				pedido.productoTexto.setText(categoria+" "+producto);
			}
			if (seleccionado == position) {
				pedido.cantidadTexto.setBackgroundColor(Color
						.parseColor("#F6A421"));
				pedido.productoTexto.setBackgroundColor(Color
						.parseColor("#F6A421"));
			} else {
				pedido.cantidadTexto.setBackgroundColor(Color.TRANSPARENT);
				pedido.productoTexto.setBackgroundColor(Color.TRANSPARENT);
			}
			return convertView;
		}

		class PedidoPendiente {
			TextView seccionTexto;
			TextView mesaTexto;
			TextView cantidadTexto;
			TextView productoTexto;
			TextView listoTexto;
			TextView servidoTexto;
		}
	}

	public class Calculadora {
		Button cero, uno, dos, tres, cuatro, cinco, seis, siete, ocho, nueve,
				ce;
		public Button botones[] = { cero, uno, dos, tres, cuatro, cinco, seis,
				siete, ocho, nueve };
		public TextView total;

		public Calculadora(int botonesR[], int ceR, int totalR) {
			for (int contador = 0; contador < botones.length; contador++) {
				botones[contador] = (Button) findViewById(
						botonesR[contador]);
				botones[contador]
						.setOnClickListener(new AdapterView.OnClickListener() {
							public void onClick(View view) {
								if (total.getText().length() < 3) {
									Button botonPulsado = (Button) view;
									int sumando = Integer.parseInt(botonPulsado
											.getText() + "");
									sumar(sumando);
								}
							}
						});
			}
			ce = (Button) findViewById(ceR);
			ce.setOnClickListener(new AdapterView.OnClickListener() {
				public void onClick(View view) {
					total.setText(0+"");
				}
			});
			total = (TextView) findViewById(totalR);
		}

		public void sumar(int sumando) {
			String totalSuma = total.getText() + "";
			int suma = Integer.parseInt(totalSuma);
			if (suma == 0) {
				totalSuma = sumando + "";
			} else {
				totalSuma = suma + "" + sumando + "";
			}
			total.setText(totalSuma);
		}
	}

//	public void apuntarPedido(Producto producto) {
//		seleccionado = -1;
//		if (pedidoMap.containsKey(producto)) {
//			pedidoMap.put(producto, pedidoMap.get(producto) + 1);
//		} else {
//			pedidoMap.put(producto, 1);
//		}
//		pedidos.invalidateViews();
//	}

	public void limpiarPedidos() {
		pedidoMap.clear();
		pedidos.invalidateViews();
	}
	private void prepararListeners() {
		pedidos = (ListView) findViewById(R.id.pedidosPendientes);
		adaptador = new AdaptadorResumen(this);
		pedidos.setAdapter(adaptador);
		pedidos.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> list, View view, int pos,
					long id) {
				seleccionado = pos;
				adaptador.notifyDataSetChanged();
			}
		});
		calculadora = new Calculadora(
				new int[] { R.id.c0, R.id.c1, R.id.c2, R.id.c3, R.id.c4,
						R.id.c5, R.id.c6, R.id.c7, R.id.c8, R.id.c9 }, R.id.ce,
				R.id.total);
		cambiar = (Button) findViewById(R.id.cambiar);
		cambiar.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				if(seleccionado > -1 && Integer.parseInt(calculadora.total.getText()+"") > 0){
					Pedido pedido = (Pedido)(new ArrayList(pedidoMap.keySet())).get(seleccionado);
					pedidoMap.put(pedido, Integer.parseInt(calculadora.total.getText()+""));
					adaptador.notifyDataSetChanged();
					calculadora.total.setText("0");
				}
			}

		});
		mas = (Button) findViewById(R.id.mas);
		mas.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				if(seleccionado > -1){
					Pedido pedido = (Pedido)(new ArrayList(pedidoMap.keySet())).get(seleccionado);
					pedidoMap.put(pedido, pedidoMap.get(pedido)+1);
					adaptador.notifyDataSetChanged();
				}
			}

		});
		menos = (Button) findViewById(R.id.menos);
		menos.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				if(seleccionado > -1){
					Pedido pedido = (Pedido)(new ArrayList(pedidoMap.keySet())).get(seleccionado);
					int cantidad = pedidoMap.get(pedido)-1;
					if (cantidad < 1){
						pedidoMap.remove(pedido);
						seleccionado = -1;
					}
					else{
						pedidoMap.put(pedido,cantidad);
					}
					adaptador.notifyDataSetChanged();
				}
			}

		});
		x = (Button) findViewById(R.id.x);
		x.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				
			}

		});
		
	}
}
