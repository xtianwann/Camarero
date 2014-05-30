package prg.pi.restaurantecamarero.xml;

import XML.XML;

public class XMLCobrarMesa extends XML{
	public XMLCobrarMesa(int idMesa){
		init();
		addNodo("tipo","CobrarMesa","paquete");
		addNodo("idMesa",idMesa+"","paquete");
	}
}