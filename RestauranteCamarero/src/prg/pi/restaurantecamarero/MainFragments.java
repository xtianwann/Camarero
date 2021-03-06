package prg.pi.restaurantecamarero;

import java.util.ArrayList;

import prg.pi.restaurantecamarero.MainFragments;
import prg.pi.restaurantecamarero.servidor.Servidor;
import prg.pi.restaurantecamarero.FragmentProductos.ProductoListener;
import prg.pi.restaurantecamarero.FragmentCantidades.CantidadListener;
import prg.pi.restaurantecamarero.FragmentResumen.ResumenListener;
import prg.pi.restaurantecamarero.FragmentSeccionMesas.SeccionesMesasListener;
import prg.pi.restaurantecamarero.restaurante.Cantidad;
import prg.pi.restaurantecamarero.restaurante.Mesa;
import prg.pi.restaurantecamarero.restaurante.PedidoListo;
import prg.pi.restaurantecamarero.restaurante.PedidosPendientesCamarero;
import prg.pi.restaurantecamarero.restaurante.Producto;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;

/**
 * Clase intermediaria entre la comunicaci�n de los fragments de la aplicaci�n.
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class MainFragments extends FragmentActivity implements CantidadListener,
		ProductoListener, ResumenListener, SeccionesMesasListener {
	private DrawerLayout drawerLayout;
	ActivityPedidosPendientes fragmentPedidosPendientes;
	FragmentSeccionMesas fragmentSeccionMesas;
	FragmentResumen fragmentResumen;
	FragmentProductos fragmentProductos;
	FragmentCantidades fragmentCategorias;
	private Servidor servidor;
	private AlertDialog.Builder dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		fragmentSeccionMesas = (FragmentSeccionMesas) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentSeccionMesas);

		fragmentSeccionMesas.setSeccionesMesasListener(this);

		fragmentResumen = (FragmentResumen) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentResumen);

		fragmentResumen.setResumenListener(this);

		fragmentProductos = (FragmentProductos) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentProductos);

		fragmentProductos.setProductoListener(this);

		fragmentCategorias = (FragmentCantidades) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentCategorias);

		fragmentCategorias.setCantidadListener(this);

		fragmentPedidosPendientes = (ActivityPedidosPendientes) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentPedidosPendientes);

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		servidor = new Servidor(MainFragments.this);
		
		Toast.makeText(MainFragments.this, "Bienvenido " + MainActivity.getUsuarioActual(), Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onDestroy() {
		servidor.getHiloPrincipal().parar();
		servidor = null;
		super.onDestroy();
	}

	@Override
	public void onCantidadSeleccionada(Cantidad cantidad) {
		fragmentProductos.rellenarProductos(cantidad);
	}

	@Override
	public void onProductoSeleccionado(Producto producto) {
		fragmentResumen.apuntarPedido(producto);

	}

	@Override
	public Mesa onEnviar() {
		Mesa mesa = fragmentSeccionMesas.getMesaSeleccionada();
		fragmentSeccionMesas.addMesaActiva(mesa);
		return mesa;
	}

	@Override
	public boolean onExistenPedidos() {
		return fragmentResumen.getPedido().size() > 0;
	}

	@Override
	public void onPedidosPendientes(
			PedidosPendientesCamarero[] pedidosPendientes) {
		fragmentPedidosPendientes.addPedidosPendientes(pedidosPendientes);

	}
	
	/**
     * Comunica al fragment encargado de los pedidos pendientes los pedidos listos obtenidos.
     * 
     * @param pedidosListos [PedidoListo[]] Pedidos listos a a�adir.
     */
	public void addPedidosListos(PedidoListo[] pedidosListos) {
		fragmentPedidosPendientes.addPedidosListos(pedidosListos);
	}

	@Override
	public void onEnviarPedidosSinEnviar() {
		fragmentResumen.enviarPedido();
	}

	@Override
	public void onIniciarHiloCantidad() {
		fragmentCategorias.iniciarHilo();
	}

	@Override
	public Cantidad[] onHiloTerminado() {
		return fragmentSeccionMesas.getDecoTodo().getCantidades()
				.toArray(new Cantidad[0]);
	}
	
	/**
     * Comunica al fragment encargado de los pedidos pendientes los pedidos pendientes obtenidos.
     * 
     * @param pendientes [ArrayList<PedidosPendientesCamarero>] Pedidos pendientes a a�adir.
     */
	public void addPedidosPendientes(ArrayList<PedidosPendientesCamarero> pendientes){
		fragmentPedidosPendientes.addPedidosPendientesEncendido(pendientes);
	}

	@Override
	public void onTerminarComanda(int idComanda) {
		fragmentPedidosPendientes.terminarComanda(idComanda);
	}
}
