package prg.pi.restaurantecamarero.xml;

import XML.XML;
/**
 * 
 * Clase encargada de formar el xml con toda la información para imprimir una comanda.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class XMLImprimir extends XML{
	/**
	 * 
	 * Constructor:
	 * 
	 * @param idMesa [int] Id de la mesa de la comanda a imprimir.
	 */
	public XMLImprimir(int idMesa){
		init();
		addNodo("tipo","ImprimirTicket","paquete");
		addNodo("idMesa",idMesa+"","paquete");
	}
}
