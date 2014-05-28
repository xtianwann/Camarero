package prg.pi.restaurantecamarero.decodificador;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;
import prg.pi.restaurantecamarero.restaurante.PedidosPendientesCamarero;
import prg.pi.restaurantecamarero.restaurante.Producto;

public class DecodificadorPendientesAlEncender {
	
	private Document dom;
	private ArrayList<PedidosPendientesCamarero> pendientes;
	
	public DecodificadorPendientesAlEncender(Document dom){
		this.dom = dom;
		pendientes = new ArrayList<PedidosPendientesCamarero>();
		extraerPendientes();
	}
	
	public ArrayList<PedidosPendientesCamarero> getPedidosPendientes(){
		return pendientes;
	}
	
	private void extraerPendientes(){
		String hayPedidos = dom.getElementsByTagName("hayPedidos").item(0).getFirstChild().getNodeValue();
		
		if(hayPedidos.equals("si")){
			NodeList nlPedidos = dom.getElementsByTagName("pedido");
			for(int contadorPedidos = 0; contadorPedidos < nlPedidos.getLength(); contadorPedidos++){
				Node nodePedido = nlPedidos.item(contadorPedidos);
				NodeList atributos = nodePedido.getChildNodes();
				String seccion = atributos.item(0).getFirstChild().getNodeValue();
				String mesa = atributos.item(1).getFirstChild().getNodeValue();
				int comanda = Integer.parseInt(atributos.item(2).getFirstChild().getNodeValue());
				int idMenu = Integer.parseInt(atributos.item(3).getFirstChild().getNodeValue());
				String nomProd = atributos.item(4).getFirstChild().getNodeValue();
				String nomCant = atributos.item(5).getFirstChild().getNodeValue();
				int unidades = Integer.parseInt(atributos.item(6).getFirstChild().getNodeValue());
				int listos = Integer.parseInt(atributos.item(7).getFirstChild().getNodeValue());
				int servidos = Integer.parseInt(atributos.item(8).getFirstChild().getNodeValue());
				listos += servidos;
				pendientes.add(new PedidosPendientesCamarero(seccion, mesa, comanda, new Producto(idMenu, nomProd, nomCant), unidades, listos, servidos));
			}
			
			Log.e("tamaño", pendientes.size()+"");
		}
	}

}
