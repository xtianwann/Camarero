package prg.pi.restaurantecamarero.decodificador;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.util.Log;
import prg.pi.restaurantecamarero.restaurante.PedidosPendientesCamarero;
import prg.pi.restaurantecamarero.restaurante.Producto;
/**
 * Clase encargada de decodificar el mensaje ResultadoLoginCamarero del servidor.
 * 
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 */
public class DecodificadorResultadoLogin {
	
	private Document dom;
	private String resultado;
	private ArrayList<PedidosPendientesCamarero> pedidosPendientes;
	/**
	 * Constructor:
	 * 
	 * @param dom [Document] DOM del XMl a interpretar.
	 */
	public DecodificadorResultadoLogin(Document dom){
		this.dom = dom;
		extraerResultado();
	}
	/**
	 * Devuelve la respuesta recibida por del servidor.
	 * 
	 * @return [String] Respuesta del servidor.
	 */
	public String getResultado(){
		return resultado;
	}
	/**
	 * Interpreta la respuesta recibida por el servidor.
	 * 
	 */
	private void extraerResultado(){
		resultado = dom.getElementsByTagName("resultado").item(0).getFirstChild().getNodeValue();
	}

}
