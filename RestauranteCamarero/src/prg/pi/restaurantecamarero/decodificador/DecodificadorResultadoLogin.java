package prg.pi.restaurantecamarero.decodificador;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.util.Log;
import prg.pi.restaurantecamarero.restaurante.PedidosPendientesCamarero;
import prg.pi.restaurantecamarero.restaurante.Producto;

public class DecodificadorResultadoLogin {
	
	private Document dom;
	private String resultado;
	private ArrayList<PedidosPendientesCamarero> pedidosPendientes;
	
	public DecodificadorResultadoLogin(Document dom){
		this.dom = dom;
		extraerResultado();
	}
	
	public String getResultado(){
		return resultado;
	}
	
	private void extraerResultado(){
		resultado = dom.getElementsByTagName("resultado").item(0).getFirstChild().getNodeValue();
	}

}
