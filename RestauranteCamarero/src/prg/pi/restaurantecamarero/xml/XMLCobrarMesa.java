package prg.pi.restaurantecamarero.xml;

import XML.XML;

/**
 * Clase encargada de formar el xml con toda la informaci�n para cobrar una mesa.
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class XMLCobrarMesa extends XML{
	
	/**
	 * Constructor: genera la estructura del mensaje XML con su contenido
	 * 
	 * @param idMesa [int] Id de la mesa a cobrar.
	 */
	public XMLCobrarMesa(int idMesa){
		init();
		addNodo("tipo","CobrarMesa","paquete");
		addNodo("idMesa",idMesa+"","paquete");
	}
}