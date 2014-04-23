package prg.pi.restaurantecamarero.decodificador;


import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import prg.pi.restaurantecamarero.restaurante.Pedido;
import prg.pi.restaurantecamarero.restaurante.Producto;

/**
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 */
public class DecodificadorResumenMesa {
    
    private Document DOMRespuesta;
    private ArrayList<Pedido> pedidos;

    public DecodificadorResumenMesa(Document dom) {
        this.DOMRespuesta = dom;
        this.pedidos = new ArrayList<Pedido>();
        generarPedidos();
    }
    
    public ArrayList<Pedido> getResumen(){
        return pedidos;
    }
    
    private void generarPedidos(){
        NodeList nodeListPedidos = DOMRespuesta.getElementsByTagName("pedido");
        for(int contadorPedidos = 0; contadorPedidos < nodeListPedidos.getLength(); contadorPedidos++){
            Node nodePedido = nodeListPedidos.item(contadorPedidos);
            Element elementoPedido = (Element) nodePedido;
            int idMenu = Integer.parseInt(elementoPedido.getAttribute("idMenu"));
            NodeList datosPedido = nodePedido.getChildNodes();
            String nombreProducto = datosPedido.item(0).getChildNodes().item(0).getNodeValue();
            String nombreCantidad = datosPedido.item(1).getChildNodes().item(0).getNodeValue();
            int unidades = Integer.parseInt(datosPedido.item(2).getChildNodes().item(0).getNodeValue());
            String estado = datosPedido.item(3).getChildNodes().item(0).getNodeValue();
            
            pedidos.add(new Pedido(new Producto(idMenu, nombreProducto), unidades, estado));
        }
    }
    
}
