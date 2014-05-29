package prg.pi.restaurantecamarero;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import prg.pi.restaurantecamarero.FragmentResumen.Calculadora;
import prg.pi.restaurantecamarero.conexion.Cliente;
import prg.pi.restaurantecamarero.decodificador.DecodificadorPendientesAlEncender;
import prg.pi.restaurantecamarero.restaurante.Mesa;
import prg.pi.restaurantecamarero.restaurante.Pedido;
import prg.pi.restaurantecamarero.restaurante.PedidoListo;
import prg.pi.restaurantecamarero.restaurante.PedidosPendientesCamarero;
import prg.pi.restaurantecamarero.restaurante.Producto;
import prg.pi.restaurantecamarero.xml.XMLCancelarPedido;
import prg.pi.restaurantecamarero.xml.XMLDamePendientes;
import prg.pi.restaurantecamarero.xml.XMLModificacionCamarero;
import prg.pi.restaurantecamarero.xml.XMLPedidosComanda;
import prg.pi.restaurantecamarero.xml.XMLPedidosServidos;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import android.widget.AdapterView.OnItemLongClickListener;

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
		pedidosPendientes = new ArrayList<PedidosPendientesCamarero>();

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
							//REPETIDO EN ADDPEDIDOSENCENCIDO
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

	private class AdaptadorResumen extends BaseAdapter {
		private LayoutInflater mInflater;

		public AdaptadorResumen(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return pedidosPendientes.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

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

	public void limpiarPedidos() {
		pedidosPendientes.clear();
		pedidos.invalidateViews();
	}

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
				R.id.total);
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
											dialog = new AlertDialog.Builder(getView().getContext());
											dialog.setMessage("No se pudo conectar con el servidor.");
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
									}
								});
							}
						}).start();
					}
				} else {
					if (corregido > -1) {
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
												MainActivity.getIpServidor());
										try {
											c.init();
											if (modificado.getUnidades() < modificado
													.getListos()) {
												modificado.setListos(modificado
														.getUnidades());
												if (modificado.getUnidades() < modificado
														.getServidos()) {
													modificado
															.setServidos(modificado
																	.getUnidades());
												}
											}
											if (modificado.isServido()){
												pedidosPendientes
														.remove(modificado);
												if(modificado.getUnidades() > 0){
													pedidosPendientesServidos.add(modificado);
												}
											}
											corregido = -1;
											pedidos.invalidateViews();
											adaptador.notifyDataSetChanged();
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
					}
				}
			}

		});
		devolver = (Button) getView().findViewById(R.id.devolver);
		devolver.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				if (corregido > -1) {
					if (pedidosPendientes.get(corregido).getUnidades() <= pedidosPendientes
							.get(corregido).getServidos()) {
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
										if (modificado.isServido()){
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
										dialog.cancel();
									}
								});
						dialog.show();
						corregido = -1;
					}
				}
			}

		});
		// Trabajo
		// pedidosPendientes.add(new PedidosPendientesCamarero("Abajo",
		// "Rincon",
		// 1, new Producto(1, "Chocos", "Racion"), 3, 2, 2));
		// pedidosPendientes.add(new PedidosPendientesCamarero("Arriba",
		// "Centro",
		// 2, new Producto(2, "Huevas", "Tapa"), 4, 2, 2));
		// ////////////////////////////////////////////////

	}

	public class PedidoPendienteText {
		private TextView seccionTexto;
		private TextView mesaTexto;
		private TextView cantidadTexto;
		private TextView productoTexto;
		private TextView listoTexto;
		private TextView servidoTexto;

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

		public void cambiaColor(int color) {
			cantidadTexto.setBackgroundColor(color);
			productoTexto.setBackgroundColor(color);
			seccionTexto.setBackgroundColor(color);
			mesaTexto.setBackgroundColor(color);
			listoTexto.setBackgroundColor(color);
			servidoTexto.setBackgroundColor(color);
		}

		public void addTexto(String seccion, String mesa, int cantidad,
				String producto, int listo, int servido) {
			cantidadTexto.setText(cantidad + "");
			productoTexto.setText(producto);
			seccionTexto.setText(seccion);
			mesaTexto.setText(mesa);
			listoTexto.setText(listo + "");
			servidoTexto.setText(servido + "");
		}

		public TextView getSeccionTexto() {
			return seccionTexto;
		}

		public void setSeccionTexto(TextView seccionTexto) {
			this.seccionTexto = seccionTexto;
		}

		public TextView getMesaTexto() {
			return mesaTexto;
		}

		public void setMesaTexto(TextView mesaTexto) {
			this.mesaTexto = mesaTexto;
		}

		public TextView getCantidadTexto() {
			return cantidadTexto;
		}

		public void setCantidadTexto(TextView cantidadTexto) {
			this.cantidadTexto = cantidadTexto;
		}

		public TextView getProductoTexto() {
			return productoTexto;
		}

		public void setProductoTexto(TextView productoTexto) {
			this.productoTexto = productoTexto;
		}

		public TextView getListoTexto() {
			return listoTexto;
		}

		public void setListoTexto(TextView listoTexto) {
			this.listoTexto = listoTexto;
		}

		public TextView getServidoTexto() {
			return servidoTexto;
		}

		public void setServidoTexto(TextView servidoTexto) {
			this.servidoTexto = servidoTexto;
		}
	}

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
			if (!encontrado){
				for(PedidosPendientesCamarero ped : pedidosPendientesServidos){
					if(pedido.getIdComanda() == ped.getIdComanda() && pedido.getProducto().getIdMenu() == ped.getProducto().getIdMenu()){
						pedido.setUnidades(pedido.getUnidades()+ped.getUnidades());
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

	public void addServido() {
		if (!isServido(seleccionado)) {
			pedidosServidos.add(pedidosPendientes.get(seleccionado));
		} else {
			if (pedidosPendientes.get(seleccionado).getServidos() < 1) {
				pedidosServidos.remove(pedidosPendientes.get(seleccionado));
			}
		}
	}

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
}
