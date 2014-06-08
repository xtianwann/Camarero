package prg.pi.restaurantecamarero.decodificador;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import prg.pi.restaurantecamarero.restaurante.PedidoListo;

/**
 * Clase encargada de decodificar el mensaje PedidosListosServer del servidor.
 * 
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 */
public class DecodificadorPedidosListos {
	
	private Document dom;
	private ArrayList<PedidoListo> pedidosListos;
	
	/**
	 * Constructor:
	 * 
	 * @param dom [Document] DOM del XMl a interpretar.
	 */
	public DecodificadorPedidosListos(Document dom){
		this.dom = dom;
		pedidosListos = new ArrayList<PedidoListo>();
		generarPedidos();
	}
	
	/**
	 * Devuelve los pedidos listos recibidos por del servidor.
	 * 
	 * @return [PedidoListo[]] Pedidos listos del servidor.
	 */
	public PedidoListo[] getPedidosListos(){
		return pedidosListos.toArray(new PedidoListo[0]);
	}
	
	/**
	 * Interpreta la respuesta recibida por el servidor y genera los pedidos listos.
	 */
	private void generarPedidos(){
		NodeList nodeListPedidos = dom.getElementsByTagName("pedido");
		for(int pedido = 0; pedido < nodeListPedidos.getLength(); pedido++){
			Node nodePedido = nodeListPedidos.item(pedido);
			Element elementoPedido = (Element) nodePedido;
			int idComanda = Integer.parseInt(elementoPedido.getAttribute("idCom"));
			int idMenu = Integer.parseInt(nodePedido.getChildNodes().item(0).getFirstChild().getNodeValue());
			int listos = Integer.parseInt(nodePedido.getChildNodes().item(1).getFirstChild().getNodeValue());
			pedidosListos.add(new PedidoListo(idComanda, idMenu, listos));
		}
	}
	
}
