package prg.pi.restaurantecamarero.xml;

import prg.pi.restaurantecamarero.restaurante.Comanda;
import XML.XML;

/**
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 */
public class XMLPedidosComanda extends XML {

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
