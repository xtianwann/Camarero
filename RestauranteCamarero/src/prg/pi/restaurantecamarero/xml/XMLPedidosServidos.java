package prg.pi.restaurantecamarero.xml;

import prg.pi.restaurantecamarero.restaurante.PedidoListo;
import XML.XML;

public class XMLPedidosServidos extends XML{
	
	public XMLPedidosServidos(PedidoListo[] servidos){
		init();
		addNodo("tipo", "PedidosServidos", "paquete");
		addNodo("finalizados", null, "paquete");
		for(int contadorPedidos = 0; contadorPedidos < servidos.length; contadorPedidos++){
			addNodoConAtributos("pedido", new String[]{"idCom"}, new String[]{servidos[contadorPedidos].getIdComanda()+""}, null, "finalizados");
			addNodo("idMenu", servidos[contadorPedidos].getIdMenu()+"", "pedido");
			addNodo("servidos", servidos[contadorPedidos].getListos()+"", "pedido");
		}
	}

}
