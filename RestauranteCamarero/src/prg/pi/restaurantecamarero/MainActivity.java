package prg.pi.restaurantecamarero;

import prg.pi.restaurantecamarero.MainActivity;
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
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.ActionBarDrawerToggle;

public class MainActivity extends FragmentActivity implements CantidadListener,
		ProductoListener,ResumenListener,SeccionesMesasListener {
	private DrawerLayout drawerLayout;
	ActivityPedidosPendientes fragmentPedidosPendientes;
	FragmentSeccionMesas fragmentSeccionMesas;
	FragmentResumen fragmentResumen;
	FragmentProductos fragmentProductos;
	FragmentCantidades fragmentCategorias;
	private Servidor servidor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		//Remove title bar
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);

	    //Remove notification bar
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
		servidor = new Servidor(MainActivity.this);
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

	public void addPedidosListos(PedidoListo[] pedidosListos) {
		fragmentPedidosPendientes.addPedidosListos(pedidosListos);
	}

	@Override
	public void onEnviarPedidosSinEnviar() {
		fragmentResumen.enviarPedido();
	}

	@Override
	public void onIniciarHilos() {
		fragmentSeccionMesas.iniciarHilo();
		fragmentCategorias.iniciarHilo();
	}

	@Override
	public Cantidad[] onHiloTerminado() {
		return fragmentSeccionMesas.getDecoTodo().getCantidades().toArray(new Cantidad[0]);
	}
}
