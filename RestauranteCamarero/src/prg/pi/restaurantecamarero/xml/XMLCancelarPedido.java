package prg.pi.restaurantecamarero.xml;

import prg.pi.restaurantecamarero.restaurante.PedidoListo;
import XML.XML;

public class XMLCancelarPedido extends XML{
	
	/* Borrar: me dijiste pedidoPendiente, pero creo que lo que necesito es pedidoListo */
	
	public XMLCancelarPedido(PedidoListo pedido){
		init();
		addNodo("tipo", "CancelarPedido", "paquete");
		addNodo("idCom", pedido.getIdComanda()+"", "paquete");
		addNodo("idMenu", pedido.getIdMenu()+"", "paquete");
		addNodo("cantidad", pedido.getListos()+"", "paquete");
	}

}
