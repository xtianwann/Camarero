package prg.pi.restaurantecamarero.decodificador;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DecodificadorInfoAcumulada {
	
	private Document dom;
	private ArrayList<XXXX> pedidosActualizados;
	
	public DecodificadorInfoAcumulada(Document dom){
		this.dom = dom;
		pedidosActualizados = new ArrayList<XXXX>();
		extraerInfo();
	}
	
	public ArrayList<XXXX> getInfoActualizada(){
		return pedidosActualizados;
	}
	
	private void extraerInfo(){
		NodeList listaPedidos = dom.getElementsByTagName("pedido");
		for(int pedido = 0; pedido < listaPedidos.getLength(); pedido++){
			NodeList atributos = listaPedidos.item(pedido).getChildNodes();
			int idMenu = Integer.parseInt(atributos.item(0).getFirstChild().getNodeValue());
			int idComanda = Integer.parseInt(atributos.item(1).getFirstChild().getNodeValue());
			String nombreSeccion = atributos.item(2).getFirstChild().getNodeValue();
			String nombreMesa = atributos.item(3).getFirstChild().getNodeValue();
			String nombrePeducto = atributos.item(4).getFirstChild().getNodeValue();
			String nombreCantidad = atributos.item(5).getFirstChild().getNodeValue();
			int unidades = Integer.parseInt(atributos.item(6).getFirstChild().getNodeValue());
			int udPedido = Integer.parseInt(atributos.item(7).getFirstChild().getNodeValue());
			int udListo = Integer.parseInt(atributos.item(8).getFirstChild().getNodeValue());
			int udServido = Integer.parseInt(atributos.item(9).getFirstChild().getNodeValue());
			
			// genera aqu� el objeto
			pedidosActualizados.add(new XXXX(/*aqu� los datos reci�n obtenidos del pedido*/));
		}
	}

}
