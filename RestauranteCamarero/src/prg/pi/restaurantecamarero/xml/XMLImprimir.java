package prg.pi.restaurantecamarero.xml;

import XML.XML;

public class XMLImprimir extends XML{
	public XMLImprimir(int idMesa){
		init();
		addNodo("tipo","ImprimirTicket","paquete");
		addNodo("idMesa",idMesa+"","paquete");
	}
}
