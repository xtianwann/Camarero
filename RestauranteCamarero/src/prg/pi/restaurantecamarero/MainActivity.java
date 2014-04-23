package prg.pi.restaurantecamarero;

import prg.pi.restaurantecamarero.FragmentProductos.ProductoListener;
import prg.pi.restaurantecamarero.FragmentCantidades.CantidadListener;
import prg.pi.restaurantecamarero.FragmentResumen.ResumenListener;
import prg.pi.restaurantecamarero.restaurante.Cantidad;
import prg.pi.restaurantecamarero.restaurante.Mesa;
import prg.pi.restaurantecamarero.restaurante.Producto;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.support.v4.app.FragmentActivity;

public class MainActivity extends FragmentActivity implements CantidadListener,
		ProductoListener,ResumenListener {

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
		
		FragmentSeccionMesas fragmentSeccionMesas = (FragmentSeccionMesas) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentSeccionMesas);
		
		FragmentResumen fragmentResumen = (FragmentResumen) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentResumen);
		
		fragmentResumen.setResumenListener(this);

		FragmentProductos fragmentProductos = (FragmentProductos) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentProductos);

		fragmentProductos.setProductoListener(this);

		FragmentCantidades fragmentCategorias = (FragmentCantidades) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentCategorias);

		fragmentCategorias.setCantidadListener(this);
	}

	@Override
	public void onCantidadSeleccionada(Cantidad cantidad) {
			((FragmentProductos) getSupportFragmentManager().findFragmentById(
					R.id.fragmentProductos)).rellenarProductos(cantidad);
	}

	@Override
	public void onProductoSeleccionado(Producto producto) {
		((FragmentResumen) getSupportFragmentManager().findFragmentById(
				R.id.fragmentResumen)).apuntarPedido(producto);
		
	}

	@Override
	public Mesa onEnviar() {
		Mesa mesa = ((FragmentSeccionMesas) getSupportFragmentManager().findFragmentById(
				R.id.fragmentSeccionMesas)).getMesaSeleccionada();
		((FragmentSeccionMesas) getSupportFragmentManager().findFragmentById(
				R.id.fragmentSeccionMesas)).addMesaActiva(mesa);
		return mesa;
	}

}
