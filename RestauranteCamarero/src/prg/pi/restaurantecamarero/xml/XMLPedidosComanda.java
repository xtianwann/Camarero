package prg.pi.restaurantecamarero.xml;

import prg.pi.restaurantecamarero.restaurante.Comanda;
import XML.XML;

/**
 * Clase encargada de formar el xml con toda la informaci�n para enviar una comanda.
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class XMLPedidosComanda extends XML {
	
	/**
	 * Constructor: genera la estructura del mensaje XML con su contenido
	 * 
	 * @param comanda [Comanda] Comanda a enviar.
	 */
    public XMLPedidosComanda(Comanda comanda) {
        init();
        addNodo("tipo", "PedidosComanda", "paquete");
        addNodo("idMes", comanda.getMesa().getId() + "", "paquete");
        addNodo("pedidos", null, "paquete");
        for (int contadorPedidos = 0; contadorPedidos < comanda.getPedidos().size(); contadorPedidos++) {
            addNodo("pedido", null, "pedidos");
            String idMenu = comanda.getPedidos().get(contadorPedidos).getProducto().getIdMenu() + "";
            addNodo("menu", idMenu, "pedido");
            String unidades = comanda.getPedidos().get(contadorPedidos).getUnidades() + "";
            addNodo("unidades", unidades, "pedido");
        }
    }
}
