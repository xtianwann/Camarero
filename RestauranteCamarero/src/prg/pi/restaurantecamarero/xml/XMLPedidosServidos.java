package prg.pi.restaurantecamarero.xml;

import prg.pi.restaurantecamarero.restaurante.PedidosPendientesCamarero;
import XML.XML;

/**
 * Clase encargada de formar el xml con toda la informaci�n para servir un pedido.
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class XMLPedidosServidos extends XML{
	
	/**
	 * Constructor: genera la estructura del mensaje XML con su contenido
	 * 
	 * @param servidos [PedidosPendientesCamarero[]] Pedidos a servir.
	 */
	public XMLPedidosServidos(PedidosPendientesCamarero[] servidos){
		init();
		addNodo("tipo", "PedidosServidos", "paquete");
		addNodo("finalizados", null, "paquete");
		for(int contadorPedidos = 0; contadorPedidos < servidos.length; contadorPedidos++){
			addNodoConAtributos("pedido", new String[]{"idCom"}, new String[]{servidos[contadorPedidos].getIdComanda()+""}, null, "finalizados");
			addNodo("idMenu", servidos[contadorPedidos].getProducto().getIdMenu()+"", "pedido");
			addNodo("servidos", servidos[contadorPedidos].getServidos()+"", "pedido");
		}
	}

}
