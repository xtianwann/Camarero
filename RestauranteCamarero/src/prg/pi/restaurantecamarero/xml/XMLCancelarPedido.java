package prg.pi.restaurantecamarero.xml;

import prg.pi.restaurantecamarero.restaurante.PedidoListo;
import XML.XML;

/**
 * Clase encargada de formar el xml con toda la informaci�n para cancelar un pedido.
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class XMLCancelarPedido extends XML{
	
	/**
	 * Constructor: genera la estructura del mensaje XML con su contenido
	 * 
	 * @param pedido [PedidoListo] Pedido a cancelar.
	 */
	public XMLCancelarPedido(PedidoListo pedido){
		init();
		addNodo("tipo", "CancelarPedido", "paquete");
		addNodo("idCom", pedido.getIdComanda()+"", "paquete");
		addNodo("idMenu", pedido.getIdMenu()+"", "paquete");
		addNodo("unidades", pedido.getListos()+"", "paquete");
	}

}
