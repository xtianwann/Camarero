package prg.pi.restaurantecamarero;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import prg.pi.restaurantecamarero.conexion.Cliente;
import prg.pi.restaurantecamarero.restaurante.Comanda;
import prg.pi.restaurantecamarero.restaurante.Mesa;
import prg.pi.restaurantecamarero.restaurante.PedidosPendientesCamarero;
import prg.pi.restaurantecamarero.restaurante.Producto;
import prg.pi.restaurantecamarero.restaurante.Pedido;
import prg.pi.restaurantecamarero.xml.XMLCerrarMesa;
import prg.pi.restaurantecamarero.xml.XMLCobrarMesa;
import prg.pi.restaurantecamarero.xml.XMLImprimir;
import prg.pi.restaurantecamarero.xml.XMLPedidosComanda;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * Fragment encargado de mostrar,modificar,enviar,cobrar y cerrar la comanda
 * actual.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class FragmentResumen extends Fragment {
	private ListView resumen;
	private Button cambiar, mas, menos, x, enviar, cobrar;
	private Calculadora calculadora;
	public HashMap<Producto, Integer> pedidos = new HashMap<Producto, Integer>();
	private int seleccionado = -1;
	private AdaptadorResumen adaptador;
	private AlertDialog.Builder dialog;
	private ResumenListener resumenListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_resumen, container, false);
	}

	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);
		prepararListeners();
	}

	/**
	 * 
	 * Clase encargada de mostrar los productos pedidos de la comanda actual.
	 * 
	 * @author Juan G. Pérez Leo
	 * @author Cristian Marín Honor
	 */

	private class AdaptadorResumen extends BaseAdapter {
		private LayoutInflater mInflater;

		/**
		 * Constructor:
		 * 
		 * @param context
		 *            [Context] Contexto en el que se encuentra el adaptador.
		 */
		public AdaptadorResumen(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return pedidos.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
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

		/**
		 * 
		 * 
		 * Clase encargada de almacenar los datos en los textos de la lista de
		 * productos pedidos de la comanda actual.
		 * 
		 * @author Juan G. Pérez Leo
		 * @author Cristian Marín Honor
		 */
		public class Pedido {
			TextView cantidadTexto;
			TextView productoTexto;
		}
	}

	/**
	 * Añade a la lista del resumen el producto seleccionado.
	 * 
	 * @param producto
	 *            [Producto] Producto seleccionado.
	 */

	public void apuntarPedido(Producto producto) {
		seleccionado = -1;
		if (pedidos.containsKey(producto)) {
			pedidos.put(producto, pedidos.get(producto) + 1);
		} else {
			pedidos.put(producto, 1);
		}
		resumen.invalidateViews();
	}

	/**
	 * Limpia todos los productos pedidos en la comanda actual.
	 * 
	 */

	public void limpiarPedidos() {
		pedidos.clear();
		seleccionado = -1;
		resumen.invalidateViews();
	}

	/**
	 * 
	 * Encargado de iniciar el listener de la lista de los productos de la
	 * comanda actual,su adaptador y todos los listener de los botones de la
	 * interfaz.
	 * 
	 */

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
				R.id.total, getView());
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
		cobrar = (Button) getView().findViewById(R.id.cobrar);
		cobrar.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				cobrarPedido();
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

	public interface ResumenListener {
		/**
		 * Devuelve la mesa de la comanda actual.
		 * 
		 * @return [Mesa] Mesa de la comanda actual.
		 */
		public Mesa onEnviar();

		/**
		 * Comunica el id de la comanda a cobrar o cerrar.
		 * 
		 * @param cantidad
		 *            [Cantidad] Cantidad seleccionada.
		 */
		public void onTerminarComanda(int idComanda);

		/**
		 * Comunica la lista de pedidos de la comanda actual.
		 * 
		 * @param pedidosPendientes
		 *            [pedidosPendientesCamarero[]] Mesa de la comanda actual.
		 */
		public void onPedidosPendientes(
				PedidosPendientesCamarero pedidosPendientes[]);
	}

	/**
	 * Permite modificar el listener.
	 * 
	 * @param resumenListener
	 *            [ResumenListener] Listener asignado.
	 */

	public void setResumenListener(ResumenListener resumenListener) {
		this.resumenListener = resumenListener;
	}

	/**
	 * Borra la lista de pedidos de la comanda actual.
	 * 
	 */

	public void borrarPedidos() {
		pedidos.clear();
		seleccionado = -1;
		adaptador.notifyDataSetChanged();
	}

	/**
	 * Envia la lista de pedidos de la comanda actual al servidor.
	 * 
	 */

	public void enviarPedido() {
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
									.xmlToString(xmlEnviarComanda.getDOM());
							Cliente c = new Cliente(mensaje, MainActivity
									.getIpServidor());
							try {
								c.init();
								PedidosPendientesCamarero pedidosPendientes[] = c
										.getPedidosPendientes()
										.getPedidosPendientes();
								Log.e("PedidosPendientes", pedidosPendientes
										+ "");
								resumenListener
										.onPedidosPendientes(pedidosPendientes);
								borrarPedidos();
							} catch (NullPointerException e) {
								dialog = new AlertDialog.Builder(getView()
										.getContext());
								dialog.setMessage("No se pudo conectar con el servidor¿Reintentar?.");
								dialog.setCancelable(false);
								dialog.setNeutralButton("OK",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
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
		} else {
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

	/**
	 * Imprime,cobra o cierra la comanda de la mesa actual.
	 * 
	 */

	public void cobrarPedido() {
		final int idMesa = resumenListener.onEnviar().getId();
		dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle("Acciones");
		dialog.setItems(new CharSequence[] { "Imprimir", "Cobrar", "Cerrar",
				"Cancelar" }, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int posicion) {
				boolean enviar = true;
				// The 'which' argument contains the index position
				// of the selected item
				String mensaje = "";
				switch (posicion) {
				case 0:
					// Enviar xml imprimir
					XMLImprimir imprimir = new XMLImprimir(idMesa);
					mensaje = imprimir.xmlToString(imprimir.getDOM());
					break;
				case 1:
					XMLCobrarMesa cobrar = new XMLCobrarMesa(idMesa);
					mensaje = cobrar.xmlToString(cobrar.getDOM());
					break;
				case 2:
					XMLCerrarMesa cerrar = new XMLCerrarMesa(idMesa);
					mensaje = cerrar.xmlToString(cerrar.getDOM());
					break;
				case 3:
					enviar = false;
					dialog.cancel();
					break;
				}
				if (enviar) {
					final String mensajeEnviar = mensaje;
					new Thread(new Runnable() {
						public void run() {
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Cliente c = new Cliente(mensajeEnviar,
											MainActivity.getIpServidor());
									try {
										c.init();
										try {
											Thread.sleep(2000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										String respuesta[] = c.getDecoAcuse()
												.getRespuesta();
										if (respuesta[0].equals("NO")) {
											AlertDialog.Builder dialogo;
											dialogo = new AlertDialog.Builder(
													getView().getContext());
											dialogo.setMessage(respuesta[1]);
											dialogo.setCancelable(false);
											dialogo.setNeutralButton(
													"OK",
													new DialogInterface.OnClickListener() {
														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															dialog.cancel();
														}
													});
											dialogo.show();
										} else {
											if (!respuesta[1].equals("")) {
												int idCom = Integer
														.parseInt(respuesta[1]);
												resumenListener
														.onTerminarComanda(idCom);
											}
										}
									} catch (NullPointerException e) {
										AlertDialog.Builder dialogo;
										dialogo = new AlertDialog.Builder(
												getView().getContext());
										dialogo.setMessage("No se pudo conectar con el servidor");
										dialogo.setCancelable(false);
										dialogo.setNeutralButton(
												"OK",
												new DialogInterface.OnClickListener() {
													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														dialog.cancel();
													}
												});
										dialogo.show();
									}

								}
							});
						}
					}).start();
				}
				dialog.cancel();
			}
		});
		dialog.create().show();
	}

	/**
	 * Devuelve los productos con sus unidades.
	 * 
	 * @return [HashMap<Producto, Integer>] Hashmap de Productos con sus
	 *         unidades
	 */
	public HashMap<Producto, Integer> getPedido() {
		return pedidos;
	}
}
