package prg.pi.restaurantecamarero.xml;

import XML.XML;

public class XMLCerrarMesa extends XML{
	public XMLCerrarMesa(int idMesa){
		init();
		addNodo("tipo","CerrarMesa","paquete");
		addNodo("idMesa",idMesa+"","paquete");
	}
}
