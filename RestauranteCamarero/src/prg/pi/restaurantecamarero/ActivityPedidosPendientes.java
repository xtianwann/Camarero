package prg.pi.restaurantecamarero;

import java.util.ArrayList;
import prg.pi.restaurantecamarero.conexion.Cliente;
import prg.pi.restaurantecamarero.decodificador.DecodificadorPendientesAlEncender;
import prg.pi.restaurantecamarero.restaurante.PedidoListo;
import prg.pi.restaurantecamarero.restaurante.PedidosPendientesCamarero;
import prg.pi.restaurantecamarero.xml.XMLCancelarPedido;
import prg.pi.restaurantecamarero.xml.XMLDamePendientes;
import prg.pi.restaurantecamarero.xml.XMLModificacionCamarero;
import prg.pi.restaurantecamarero.xml.XMLPedidosServidos;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

/**
 * Fragment encargado de controlar las unidades servidas,modificadas y devueltas
 * de las comandas pedidas por el camarero.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class ActivityPedidosPendientes extends Fragment {
	private ListView pedidos;
	private Button limpiar, cambiar, mas, menos, devolver, enviar;
	private Calculadora calculadora;
	public ArrayList<PedidosPendientesCamarero> pedidosPendientes = new ArrayList<PedidosPendientesCamarero>();
	public ArrayList<PedidosPendientesCamarero> pedidosServidos = new ArrayList<PedidosPendientesCamarero>();
	public ArrayList<PedidosPendientesCamarero> pedidosPendientesServidos = new ArrayList<PedidosPendientesCamarero>();
	private int seleccionado = -1;
	private int corregido = -1;
	private AdaptadorResumen adaptador;
	int unidadesAnterior;
	int servidoAnterior;
	private AlertDialog.Builder dialog;
	private DecodificadorPendientesAlEncender decoPendientes;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.pedidos_pendientes, container, false);
	}

	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);
		prepararListeners();

		decoPendientes = null;
		new Thread(new Runnable() {
			public void run() {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						XMLDamePendientes xmlPendientes = new XMLDamePendientes();
						String mensaje = xmlPendientes
								.xmlToString(xmlPendientes.getDOM());
						Cliente c = new Cliente(mensaje, MainActivity
								.getIpServidor());
						try {
							c.init();
							decoPendientes = c.getDecoPendientes();
							pedidosPendientesServidos.clear();
							pedidosPendientes.clear();
							// REPETIDO EN ADDPEDIDOSENCENCIDO
							for (PedidosPendientesCamarero pedido : decoPendientes
									.getPedidosPendientes()) {
								if (pedido.isServido())
									pedidosPendientesServidos.add(pedido);
								else
									pedidosPendientes.add(pedido);
							}
							adaptador.notifyDataSetChanged();
						} catch (NullPointerException e) {

						}
					}
				});
			}
		}).start();

	}

	/**
	 * Clase encargada de mostrar las modificaciones de los datos y los colores
	 * de la lista de comandas pendientes.
	 * 
	 * @author Juan G. Pérez Leo
	 * @author Cristian Marín Honor
	 */

	public class AdaptadorResumen extends BaseAdapter {
		private LayoutInflater mInflater;

		/**
		 * Constructor:
		 * 
		 * @param context [Context] Contexto en el que se encuentra el adaptador.
		 */
		public AdaptadorResumen(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return pedidosPendientes.size();
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
			PedidoPendienteText pedidoText;
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.pedidos_pendientes_list, null);
				pedidoText = new PedidoPendienteText(convertView);
				convertView.setTag(pedidoText);
			} else {
				pedidoText = (PedidoPendienteText) convertView.getTag();
			}
			if (corregido > -1) {
				enviar.setText("Deshacer");
			} else {
				enviar.setText("Enviar");
			}
			PedidosPendientesCamarero pedidoPendiente = pedidosPendientes
					.get(position);
			pedidoText
					.addTexto(pedidoPendiente.getNombreSeccion(),
							pedidoPendiente.getNombreMesa(), pedidoPendiente
									.getUnidades(), pedidoPendiente
									.getProducto().getCantidadPadre()
									+ " "
									+ pedidoPendiente.getProducto()
											.getNombreProducto(),
							pedidoPendiente.getListos(), pedidoPendiente
									.getServidos());
			if (seleccionado == position) {
				pedidoText.cambiaColor(Color.parseColor("#F6A421"));
			} else {
				if (corregido == position) {
					pedidoText.cambiaColor(Color.parseColor("#FF0000"));
				} else {

					if (pedidoPendiente.existenListos()) {
						pedidoText.cambiaColor(Color.parseColor("#0EA7F4"));
					} else {
						pedidoText.cambiaColor(Color.TRANSPARENT);
					}
				}

			}

			return convertView;
		}
	}

	/**
	 * Encargado de iniciar el listener de la lista de comandas pendientes,su
	 * adaptador y todos los listener de los botones de la interfaz.
	 */
	private void prepararListeners() {
		pedidos = (ListView) getView().findViewById(R.id.pedidosPendientes);
		adaptador = new AdaptadorResumen(getView().getContext());
		pedidos.setAdapter(adaptador);
		
		pedidos.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> list, View view, int pos,
					long id) {
				TextView seccion = (TextView) view
						.findViewById(R.id.seccionPendiente);
				ColorDrawable color = (ColorDrawable) seccion.getBackground();
				int codigoColor = color.getColor();
				if (codigoColor == (Color.parseColor("#0EA7F4"))
						|| isServido(pos)) {
					if (seleccionado > -1) {
						if (pedidosPendientes.get(seleccionado).getServidos() == servidoAnterior) {
							if (isServido(seleccionado)) {
								pedidosServidos.remove(pedidosPendientes
										.get(seleccionado));
							}
						}
					}
					seleccionado = pos;
					corregido = -1;
					servidoAnterior = pedidosPendientes.get(seleccionado)
							.getServidos();
					adaptador.notifyDataSetChanged();
				}
			}
		});
		
		pedidos.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (corregido > -1) {
					notificacionDeshacer();
					pedidosPendientes.get(corregido).setUnidades(
							unidadesAnterior);
					corregido = -1;
					seleccionado = -1;
				} else {
					corregido = arg2;
					unidadesAnterior = pedidosPendientes.get(corregido)
							.getUnidades();
					seleccionado = -1;
				}
				adaptador.notifyDataSetChanged();
				return false;
			}
		});
		
		calculadora = new Calculadora(
				new int[] { R.id.c0, R.id.c1, R.id.c2, R.id.c3, R.id.c4,
						R.id.c5, R.id.c6, R.id.c7, R.id.c8, R.id.c9 }, R.id.ce,
				R.id.total, getView());
		
		/* Botón cambiar */
		cambiar = (Button) getView().findViewById(R.id.cambiar);
		cambiar.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				if (seleccionado > -1) {
					PedidosPendientesCamarero pedido = pedidosPendientes
							.get(seleccionado);
					int numeroCalculadora = Integer.parseInt(calculadora.total
							.getText() + "");
					if (numeroCalculadora <= pedido.getListos()) {
						pedido.setServidos(numeroCalculadora);
						addServido();
						adaptador.notifyDataSetChanged();
					}
					calculadora.total.setText("0");
				}
				if (corregido > -1) {
					PedidosPendientesCamarero pedido = pedidosPendientes
							.get(corregido);
					int numeroCalculadora = Integer.parseInt(calculadora.total
							.getText() + "");
					if (numeroCalculadora <= unidadesAnterior) {
						pedido.setUnidades(numeroCalculadora);
						adaptador.notifyDataSetChanged();
					}
					calculadora.total.setText("0");
				}
			}

		});
		
		/* Botón + */
		mas = (Button) getView().findViewById(R.id.mas);
		mas.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				if (seleccionado > -1) {
					PedidosPendientesCamarero pedido = pedidosPendientes
							.get(seleccionado);
					int suma = pedido.getServidos() + 1;
					if (suma <= pedido.getListos()) {
						pedido.setServidos(pedido.getServidos() + 1);
						addServido();
						adaptador.notifyDataSetChanged();
					}
				}
				if (corregido > -1) {
					PedidosPendientesCamarero pedido = pedidosPendientes
							.get(corregido);
					int suma = pedido.getUnidades() + 1;
					if (suma <= unidadesAnterior) {
						pedido.setUnidades(suma);
						adaptador.notifyDataSetChanged();
					}
				}
			}

		});
		
		/* Botón - */
		menos = (Button) getView().findViewById(R.id.menos);
		menos.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				if (seleccionado > -1) {
					PedidosPendientesCamarero pedido = pedidosPendientes
							.get(seleccionado);
					if (pedido.getServidos() > 0) {
						pedido.setServidos(pedido.getServidos() - 1);
						addServido();
						adaptador.notifyDataSetChanged();
					}
				}
				if (corregido > -1) {
					PedidosPendientesCamarero pedido = pedidosPendientes
							.get(corregido);
					int resta = pedido.getUnidades() - 1;
					if (resta > -1) {
						pedido.setUnidades(resta);
						adaptador.notifyDataSetChanged();
					}
				}
			}

		});
		
		/* Botón enviar */
		enviar = (Button) getView().findViewById(R.id.enviar);
		enviar.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				if (enviar.getText().equals("Enviar")) {
					if (pedidosServidos.size() > 0) {
						new Thread(new Runnable() {
							public void run() {
								getActivity().runOnUiThread(new Runnable() {
									@Override
									public void run() {
										XMLPedidosServidos xmlPedidosServidos = new XMLPedidosServidos(
												pedidosServidos
														.toArray(new PedidosPendientesCamarero[0]));
										String mensaje = xmlPedidosServidos
												.xmlToString(xmlPedidosServidos
														.getDOM());
										Cliente c = new Cliente(mensaje,
												MainActivity.getIpServidor());
										try {
											c.init();
											for (PedidosPendientesCamarero pedido : pedidosServidos) {
												if (pedido.isServido()) {
													pedidosPendientes
															.remove(pedido);
													pedidosPendientesServidos
															.add(pedido);
												}
											}
											pedidosServidos.clear();
											pedidos.invalidateViews();
											seleccionado = -1;
											adaptador.notifyDataSetChanged();
										} catch (NullPointerException e) {
											dialog = new AlertDialog.Builder(
													getView().getContext());
											dialog.setMessage("No se pudo conectar con el servidor.");
											dialog.setCancelable(false);
											dialog.setNeutralButton(
													"OK",
													new DialogInterface.OnClickListener() {
														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
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
				} else {
					if (corregido > -1) {
						if (pedidosPendientes.get(corregido).getUnidades() >= pedidosPendientes
								.get(corregido).getServidos()) {
							new Thread(new Runnable() {
								public void run() {
									getActivity().runOnUiThread(new Runnable() {
										@Override
										public void run() {
											PedidosPendientesCamarero modificado = pedidosPendientes
													.get(corregido);
											XMLModificacionCamarero xmlModificacionCamarero = new XMLModificacionCamarero(
													new PedidosPendientesCamarero[] { modificado });
											String mensaje = xmlModificacionCamarero
													.xmlToString(xmlModificacionCamarero
															.getDOM());
											Cliente c = new Cliente(mensaje,
													MainActivity
															.getIpServidor());
											try {
												c.init();
												if (modificado.getUnidades() < modificado
														.getListos()) {
													modificado
															.setListos(modificado
																	.getUnidades());
													if (modificado
															.getUnidades() < modificado
															.getServidos()) {
														modificado
																.setServidos(modificado
																		.getUnidades());
													}
												}
												if (modificado.isServido()) {
													pedidosPendientes
															.remove(modificado);
													if (modificado
															.getUnidades() > 0) {
														pedidosPendientesServidos
																.add(modificado);
													}
												}
												corregido = -1;
												pedidos.invalidateViews();
												adaptador
														.notifyDataSetChanged();
											} catch (NullPointerException e) {
												dialog = new AlertDialog.Builder(
														getView().getContext());
												dialog.setMessage("No se puede conectar con el servidor.");
												dialog.setCancelable(false);
												dialog.setNeutralButton(
														"OK",
														new DialogInterface.OnClickListener() {
															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {
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
							dialog = new AlertDialog.Builder(
									getView().getContext());
							dialog.setMessage("No se puede deshacer pedidos servidos, para eso use Devolver.");
							dialog.setCancelable(false);
							dialog.setNeutralButton(
									"OK",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.cancel();
										}
									});
							dialog.show();
						}
					}
				}
			}

		});
		
		/* Botón devolver */
		devolver = (Button) getView().findViewById(R.id.devolver);
		devolver.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				if (corregido > -1) {
					if (pedidosPendientes.get(corregido).getUnidades() <= pedidosPendientes
							.get(corregido).getServidos() && pedidosPendientes.get(corregido).getUnidades() > 0) {
						new Thread(new Runnable() {
							public void run() {
								getActivity().runOnUiThread(new Runnable() {
									@Override
									public void run() {
										PedidosPendientesCamarero modificado = pedidosPendientes
												.get(corregido);
										XMLCancelarPedido xmlCancelarPedido = new XMLCancelarPedido(
												new PedidoListo(modificado
														.getIdComanda(),
														modificado
																.getProducto()
																.getIdMenu(),
														modificado
																.getUnidades()));
										String mensaje = xmlCancelarPedido
												.xmlToString(xmlCancelarPedido
														.getDOM());
										Cliente c = new Cliente(mensaje,
												MainActivity.getIpServidor());
										c.init();
										modificado.setListos(modificado
												.getListos()
												- modificado.getUnidades());
										modificado.setServidos(modificado
												.getServidos()
												- modificado.getUnidades());
										modificado.setUnidades(unidadesAnterior
												- modificado.getUnidades());
										if (modificado.isServido()) {
											pedidosPendientes
													.remove(modificado);
										}
										corregido = -1;
										pedidos.invalidateViews();
										adaptador.notifyDataSetChanged();
									}
								});
							}
						}).start();
					} else {
						dialog = new AlertDialog.Builder(getView().getContext());
						dialog.setMessage("No se pueden devolver pedidos que no estan servidos.");
						dialog.setCancelable(false);
						dialog.setNeutralButton("OK",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										pedidosPendientes.get(corregido).setUnidades(unidadesAnterior);
										adaptador.notifyDataSetChanged();
										corregido = -1;
										dialog.cancel();
									}
								});
						dialog.show();
					}
				}
			}

		});
	}

	/**
	 * Clase encargada de almacenar los datos y colores en los textos de la
	 * lista de comandas pendientes.
	 * 
	 * @author Juan G. Pérez Leo
	 * @author Cristian Marín Honor
	 */
	public class PedidoPendienteText {
		private TextView seccionTexto;
		private TextView mesaTexto;
		private TextView cantidadTexto;
		private TextView productoTexto;
		private TextView listoTexto;
		private TextView servidoTexto;

		/**
		 * Constructor:
		 * 
		 * @param view [View] Vista a modificar.
		 */
		public PedidoPendienteText(View view) {
			cantidadTexto = (TextView) view
					.findViewById(R.id.cantidadPendiente);
			productoTexto = (TextView) view
					.findViewById(R.id.productoPendiente);
			seccionTexto = (TextView) view.findViewById(R.id.seccionPendiente);
			mesaTexto = (TextView) view.findViewById(R.id.mesaPendiente);
			listoTexto = (TextView) view.findViewById(R.id.listoPendiente);
			servidoTexto = (TextView) view.findViewById(R.id.servidoPendiente);
		}

		/**
		 * Modifica el color de todos los textos de la lista de comandas
		 * pendientes
		 * 
		 * @param color [int] Color a modificar.
		 */
		public void cambiaColor(int color) {
			cantidadTexto.setBackgroundColor(color);
			productoTexto.setBackgroundColor(color);
			seccionTexto.setBackgroundColor(color);
			mesaTexto.setBackgroundColor(color);
			listoTexto.setBackgroundColor(color);
			servidoTexto.setBackgroundColor(color);
		}

		/**
		 * Añade el contenido de los textos de la lista de comandas pendientes.
		 * 
		 * @param seccion [String] Nombre de la seccion.
		 * @param mesa [String] Nombre de la mesa.
		 * @param cantidad [int] Cantidad de productos.
		 * @param producto [String] Nombre del producto.
		 * @param listo [String] Cantidad de productos listos.
		 * @param servido [String] Cantidad de productos servidos.
		 */
		public void addTexto(String seccion, String mesa, int cantidad,
				String producto, int listo, int servido) {
			cantidadTexto.setText(cantidad + "");
			productoTexto.setText(producto);
			seccionTexto.setText(seccion);
			mesaTexto.setText(mesa);
			listoTexto.setText(listo + "");
			servidoTexto.setText(servido + "");
		}
	}

	/**
	 * Añade los pedidos de la comanda tomada por el camarero a la lista de
	 * pedidos pendientes.
	 * 
	 * @param pedidosAdd [PedidosPendientesCamarero[]] Pedidos tomados por el camarero
	 */
	public void addPedidosPendientes(PedidosPendientesCamarero[] pedidosAdd) {
		boolean encontrado;
		for (PedidosPendientesCamarero pedido : pedidosAdd) {
			encontrado = false;
			for (PedidosPendientesCamarero pedidoP : pedidosPendientes) {
				if (pedido.getIdComanda() == pedidoP.getIdComanda()
						&& pedido.getProducto().getIdMenu() == pedidoP
								.getProducto().getIdMenu()) {
					pedidoP.setUnidades(pedido.getUnidades()
							+ pedidoP.getUnidades());
					encontrado = true;
					break;
				}
			}
			if (!encontrado) {
				for (PedidosPendientesCamarero ped : pedidosPendientesServidos) {
					if (pedido.getIdComanda() == ped.getIdComanda()
							&& pedido.getProducto().getIdMenu() == ped
									.getProducto().getIdMenu()) {
						pedido.setUnidades(pedido.getUnidades()
								+ ped.getUnidades());
						pedido.setListos(ped.getListos());
						pedido.setServidos(ped.getServidos());
						pedidosPendientesServidos.remove(ped);
						break;
					}
				}
				pedidosPendientes.add(pedido);
			}

		}
		pedidos.invalidateViews();
		adaptador.notifyDataSetChanged();
	}

	/**
	 * Añade los pedidos de todas las comanda pendientes del camarero en el
	 * momento que enciende el dispositivo.
	 * 
	 * @param pedidosAdd [PedidosPendientesCamarero[]] Todos los pedidos pendientes del camarero
	 */
	public void addPedidosPendientesEncendido(
			ArrayList<PedidosPendientesCamarero> pedidosAdd) {
		for (PedidosPendientesCamarero pedido : pedidosAdd) {
			if (pedido.isServido())
				pedidosPendientesServidos.add(pedido);
			else
				pedidosPendientes.add(pedido);
		}
		adaptador.notifyDataSetChanged();
	}

	/**
	 * Añade la cantidad de pedidos que están listos en cocina.
	 * 
	 * @param pedidosListos [PedidoListo[ ]] Pedidos listos en cocina.
	 */
	public void addPedidosListos(PedidoListo[] pedidosListos) {
		for (PedidoListo pedidoListo : pedidosListos) {
			for (PedidosPendientesCamarero pedido : pedidosPendientes) {
				if (pedido.getIdComanda() == pedidoListo.getIdComanda()
						&& pedido.getProducto().getIdMenu() == pedidoListo
								.getIdMenu()) {
					pedido.setListos(pedidoListo.getListos());
				}
			}
		}
		pedidos.invalidateViews();
		adaptador.notifyDataSetChanged();
	}

	/**
	 * Comprueba si el pedido se encuentra en la lista de pedidos servidos a
	 * enviar.
	 * 
	 * @param pedido [int] Posicion del pedido en la lista.
	 * @return [boolean] Pedido encontrado.
	 */
	public boolean isServido(int pedido) {
		boolean listo = false;
		for (PedidosPendientesCamarero pedidoServido : pedidosServidos) {
			if (pedidoServido.equals(pedidosPendientes.get(pedido))) {
				listo = true;
				break;
			}
		}
		return listo;
	}

	/**
	 * Añade y borra pedidos servidos de la lista de pedidos servidos a enviar.
	 */
	public void addServido() {
		if (!isServido(seleccionado)) {
			pedidosServidos.add(pedidosPendientes.get(seleccionado));
		} else {
			if (pedidosPendientes.get(seleccionado).getServidos() < 1) {
				pedidosServidos.remove(pedidosPendientes.get(seleccionado));
			}
		}
	}

	/**
	 * Lanza una notificación cuando no se realiza la acción de modificar los
	 * pedidos pendientes correctamente.
	 */
	private void notificacionDeshacer() {
		dialog = new AlertDialog.Builder(getView().getContext());
		dialog.setMessage("No se ha terminado la acción de deshacer.");
		dialog.setCancelable(false);
		dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		dialog.show();
	}

	/**
	 * Borra los pedidos servidos de una comanda pagada o cerrada.
	 * 
	 * @param idComanda [int] Id de la comanda a borrar.
	 */
	public void terminarComanda(int idComanda) {
		ArrayList<PedidosPendientesCamarero> pedidosBorrar = new ArrayList<PedidosPendientesCamarero>();
		ArrayList<PedidosPendientesCamarero> pedidosBorrarPendientes = new ArrayList<PedidosPendientesCamarero>();
		for (PedidosPendientesCamarero pedido : pedidosPendientes) {
			if (pedido.getIdComanda() == idComanda)
				pedidosBorrarPendientes.add(pedido);
		}
		for (PedidosPendientesCamarero pedido : pedidosBorrarPendientes) {
			pedidosPendientes.remove(pedido);
		}
		for (PedidosPendientesCamarero pedido : pedidosPendientesServidos) {
			if (pedido.getIdComanda() == idComanda)
				pedidosBorrar.add(pedido);
		}
		for (PedidosPendientesCamarero pedido : pedidosBorrar)
			pedidosPendientesServidos.remove(pedido);
		pedidosBorrar = null;
		pedidosBorrarPendientes = null;
		pedidos.invalidateViews();
		adaptador.notifyDataSetChanged();
	}
}
