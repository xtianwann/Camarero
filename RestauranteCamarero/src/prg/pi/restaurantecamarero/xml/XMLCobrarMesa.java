package prg.pi.restaurantecamarero.xml;

import XML.XML;
/**
 * 
 * Clase encargada de formar el xml con toda la información para cobrar una mesa.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class XMLCobrarMesa extends XML{
	/**
	 * 
	 * Constructor:
	 * 
	 * @param idMesa [int] Id de la mesa a cobrar.
	 */
	public XMLCobrarMesa(int idMesa){
		init();
		addNodo("tipo","CobrarMesa","paquete");
		addNodo("idMesa",idMesa+"","paquete");
	}
}