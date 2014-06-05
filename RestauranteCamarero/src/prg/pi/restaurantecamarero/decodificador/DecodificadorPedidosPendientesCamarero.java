package prg.pi.restaurantecamarero.decodificador;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;
import prg.pi.restaurantecamarero.restaurante.PedidosPendientesCamarero;
import prg.pi.restaurantecamarero.restaurante.Producto;

/**
 * Clase encargada de decodificar el mensaje PedidosPendientesCamareroServer del servidor.
 * 
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 */
public class DecodificadorPedidosPendientesCamarero {
	
	private Document dom;
	private ArrayList<PedidosPendientesCamarero> pedidosPendientes;
	/**
	 * Constructor:
	 * 
	 * @param dom [Document] DOM del XMl a interpretar.
	 */
	public DecodificadorPedidosPendientesCamarero(Document dom){
		this.dom = dom;
		pedidosPendientes = new ArrayList<PedidosPendientesCamarero>();
		generarPedidos();
	}
	/**
	 * Devuelve la respuesta del servidor y genera los pedidos pendientes del camarero.
	 * 
	 * @return [PedidosPendientesCamarero[]] Pedidos pendientes del camarero.
	 */
	public PedidosPendientesCamarero[] getPedidosPendientes(){
		if(pedidosPendientes.size() == 0)
			return null;
		return pedidosPendientes.toArray(new PedidosPendientesCamarero[0]);
	}
	/**
	 * Interpreta la respuesta recibida por el servidor y genera los pedidos pendientes.
	 * 
	 */
	private void generarPedidos(){
		/* Obtenemos los datos de la mesa */
		NodeList nodeListMesa = dom.getElementsByTagName("mesa");
		Node nodeMesa = nodeListMesa.item(0);
		String nombreMesa = nodeMesa.getFirstChild().getNodeValue();
		if(!nombreMesa.equals("nopuedeshacerunpedido")){
			Log.e("deco", "entro en el if");
			NodeList nodeListSeccion = dom.getElementsByTagName("seccion");
			Node nodeSeccion = nodeListSeccion.item(0);
			String nombreSeccion = nodeSeccion.getFirstChild().getNodeValue();
			
			/* Extraemos la información de los pedidos */
			NodeList nodeListPedidos = dom.getElementsByTagName("pedidos");
			Node nodePedidos = nodeListPedidos.item(0);
			Element elementoPedidos = (Element) nodePedidos;
			int idComanda = Integer.parseInt(elementoPedidos.getAttribute("idCom"));
			
			NodeList listaPedidos = dom.getElementsByTagName("pedido");
			for(int contadorPedidos = 0; contadorPedidos < listaPedidos.getLength(); contadorPedidos++){
				Node nodePedido = listaPedidos.item(contadorPedidos);
				Element elementoPedido = (Element) nodePedido;
				int idMenu = Integer.parseInt(elementoPedido.getAttribute("idMenu"));
				NodeList nodeListDatos = nodePedido.getChildNodes();
				String nombreProducto = nodeListDatos.item(0).getFirstChild().getNodeValue();
	            String nombreCantidad = nodeListDatos.item(1).getFirstChild().getNodeValue();
	            int unidades = Integer.parseInt(nodeListDatos.item(2).getFirstChild().getNodeValue());
	            int listos = Integer.parseInt(nodeListDatos.item(3).getFirstChild().getNodeValue());
	            int servidos = Integer.parseInt(nodeListDatos.item(4).getFirstChild().getNodeValue());
	            
	            pedidosPendientes.add(new PedidosPendientesCamarero(nombreSeccion, nombreMesa, idComanda, new Producto(idMenu, nombreProducto, nombreCantidad),unidades, listos, servidos));
			}
		}
	}
}
