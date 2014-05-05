package prg.pi.restaurantecamarero.xml;

import prg.pi.restaurantecamarero.restaurante.PedidoListo;
import XML.XML;

public class XMLCancelarPedido extends XML{
	
	/* Borrar: me dijiste pedidoPendiente, pero creo que lo que necesito es pedidoListo
	 * como se va a hacer de uno en uno solo se le pasa idComanda y idMenu 
	 */
	
	public XMLCancelarPedido(PedidoListo pedido){
		init();
		addNodo("tipo", "CancelarPedido", "paquete");
		addNodo("idCom", pedido.getIdComanda()+"", "paquete");
		addNodo("idMenu", pedido.getIdMenu()+"", "paquete");
		addNodo("unidades", pedido.getListos()+"", "paquete");
	}

}
