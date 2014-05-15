package prg.pi.restaurantecamarero;

import prg.pi.restaurantecamarero.FragmentSeccionMesas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import prg.pi.restaurantecamarero.FragmentCantidades.AdaptadorCantidades;
import prg.pi.restaurantecamarero.FragmentProductos.ProductoListener;
import prg.pi.restaurantecamarero.conexion.Cliente;
import prg.pi.restaurantecamarero.decodificador.DecodificadorPedidosPendientesCamarero;
import prg.pi.restaurantecamarero.restaurante.Cantidad;
import prg.pi.restaurantecamarero.restaurante.Comanda;
import prg.pi.restaurantecamarero.restaurante.Mesa;
import prg.pi.restaurantecamarero.restaurante.PedidosPendientesCamarero;
import prg.pi.restaurantecamarero.restaurante.Producto;
import prg.pi.restaurantecamarero.restaurante.Pedido;
import prg.pi.restaurantecamarero.xml.XMLDameloTodo;
import prg.pi.restaurantecamarero.xml.XMLPedidosComanda;
import XML.XML;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentResumen extends Fragment {
	private ListView resumen;
	private Button cambiar, mas, menos, x, enviar;
	private Calculadora calculadora;
	public HashMap<Producto, Integer> pedidos = new HashMap<Producto, Integer>();
	private int seleccionado = -1;
	private AdaptadorResumen adaptador;
	private AlertDialog.Builder dialog;
	private ResumenListener resumenListener;
	public HashMap<Producto, Integer> getPedido() {
		return pedidos;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_resumen, container, false);
	}

	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);
		prepararListeners();
		/*
		 * limpiar = (Button) getView().findViewById(R.id.limpiar);
		 * limpiar.setOnClickListener(new AdapterView.OnClickListener() { public
		 * void onClick(View view) { limpiarPedidos(); }
		 * 
		 * });
		 */
	}

	private class AdaptadorResumen extends BaseAdapter {
		private LayoutInflater mInflater;

		public AdaptadorResumen(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return pedidos.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			Pedido pedido;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.resumen_list, null);
				pedido = new Pedido();
				pedido.cantidadTexto = (TextView) convertView
						.findViewById(R.id.TextView01);
				pedido.productoTexto = (TextView) convertView
						.findViewById(R.id.TextView02);

				convertView.setTag(pedido);
			} else {
				pedido = (Pedido) convertView.getTag();
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
			Iterator iterador = pedidos.entrySet().iterator();
			if (iterador.hasNext()) {
				ArrayList<Producto> productos = new ArrayList(pedidos.keySet());
				String producto = productos.get(position).getNombreProducto();
				String categoria = productos.get(position).getCantidadPadre();
				int cantidad = (Integer) (new ArrayList(pedidos.values()))
						.get(position);
				pedido.cantidadTexto.setText(cantidad + "");
				pedido.productoTexto.setText(categoria + " " + producto);
			}
			return convertView;
		}

		class Pedido {
			TextView cantidadTexto;
			TextView productoTexto;
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
				botones[contador] = (Button) getView().findViewById(
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
			ce = (Button) getView().findViewById(ceR);
			ce.setOnClickListener(new AdapterView.OnClickListener() {
				public void onClick(View view) {
					total.setText(0 + "");
				}
			});
			total = (TextView) getView().findViewById(totalR);
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

	public void apuntarPedido(Producto producto) {
		seleccionado = -1;
		if (pedidos.containsKey(producto)) {
			pedidos.put(producto, pedidos.get(producto) + 1);
		} else {
			pedidos.put(producto, 1);
		}
		resumen.invalidateViews();
	}

	public void limpiarPedidos() {
		pedidos.clear();
		seleccionado = -1;
		resumen.invalidateViews();
	}

	private void prepararListeners() {
		resumen = (ListView) getView().findViewById(R.id.lv_country);
		adaptador = new AdaptadorResumen(getView().getContext());
		resumen.setAdapter(adaptador);
		resumen.setOnItemClickListener(new OnItemClickListener() {
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
		cambiar = (Button) getView().findViewById(R.id.cambiar);
		cambiar.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				if (seleccionado > -1
						&& Integer.parseInt(calculadora.total.getText() + "") > 0) {
					Producto producto = (Producto) (new ArrayList(pedidos
							.keySet())).get(seleccionado);
					pedidos.put(producto,
							Integer.parseInt(calculadora.total.getText() + ""));
					adaptador.notifyDataSetChanged();
					calculadora.total.setText("0");
				}
			}

		});
		mas = (Button) getView().findViewById(R.id.mas);
		mas.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				if (seleccionado > -1) {
					Producto producto = (Producto) (new ArrayList(pedidos
							.keySet())).get(seleccionado);
					pedidos.put(producto, pedidos.get(producto) + 1);
					adaptador.notifyDataSetChanged();
				}
			}

		});
		menos = (Button) getView().findViewById(R.id.menos);
		menos.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				if (seleccionado > -1) {
					Producto producto = (Producto) (new ArrayList(pedidos
							.keySet())).get(seleccionado);
					int cantidad = pedidos.get(producto) - 1;
					if (cantidad < 1) {
						pedidos.remove(producto);
						seleccionado = -1;
					} else {
						pedidos.put(producto, cantidad);
					}
					adaptador.notifyDataSetChanged();
				}
			}

		});
		x = (Button) getView().findViewById(R.id.x);
		x.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				if (seleccionado > -1) {
					Producto producto = (Producto) (new ArrayList(pedidos
							.keySet())).get(seleccionado);
					pedidos.remove(producto);
					adaptador.notifyDataSetChanged();
					seleccionado = -1;
				}
			}

		});
		enviar = (Button) getView().findViewById(R.id.enviar);
		enviar.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				enviarPedido();
			}
		});
	}

	public interface ResumenListener {
		public Mesa onEnviar();

		public void onPedidosPendientes(
				PedidosPendientesCamarero pedidosPendientes[]);
	}

	public void setResumenListener(ResumenListener resumenListener) {
		this.resumenListener = resumenListener;
	}

	public void borrarPedidos() {
		pedidos.clear();
		seleccionado = -1;
		adaptador.notifyDataSetChanged();
	}
	public void enviarPedido(){
		final Comanda comanda;
		if (pedidos.size() > 0) {
			comanda = new Comanda(resumenListener.onEnviar(),
					new ArrayList<Pedido>());
			Iterator iterador = pedidos.entrySet().iterator();
			while (iterador.hasNext()) {
				Map.Entry mapa = (Map.Entry) iterador.next();
				comanda.addPedido(new Pedido((Producto) mapa.getKey(),
						(Integer) mapa.getValue()));
			}
			new Thread(new Runnable() {
				public void run() {
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							XMLPedidosComanda xmlEnviarComanda = new XMLPedidosComanda(
									comanda);
							String mensaje = xmlEnviarComanda
									.xmlToString(xmlEnviarComanda
											.getDOM());
							Cliente c = new Cliente(mensaje);
							try {
								c.init();
								PedidosPendientesCamarero pedidosPendientes[] = c
										.getPedidosPendientes()
										.getPedidosPendientes();
								resumenListener.onPedidosPendientes(pedidosPendientes);
								borrarPedidos();
							} catch (NullPointerException e){
								dialog = new AlertDialog.Builder(getView().getContext());
								dialog.setMessage("No se pudo conectar con el servidor¿Reintentar?.");
								dialog.setCancelable(false);
								dialog.setNeutralButton("OK",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog,
													int which) {
												enviarPedido();
												dialog.cancel();
											}
										});
								dialog.show();
							}
							
						}
					});
				}
			}).start();
		}
		else{
			dialog = new AlertDialog.Builder(getActivity());
			dialog.setMessage("No hay pedidos a enviar");
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
}
