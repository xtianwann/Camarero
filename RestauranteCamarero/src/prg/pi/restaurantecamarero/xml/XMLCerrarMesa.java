package prg.pi.restaurantecamarero.xml;

import XML.XML;
/**
 * 
 * Clase encargada de formar el xml con toda la información para cerrar una mesa.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class XMLCerrarMesa extends XML{
	/**
	 * 
	 * Constructor:
	 * 
	 * @param int [idMesa] Id de la mesa a cerrar.
	 */
	public XMLCerrarMesa(int idMesa){
		init();
		addNodo("tipo","CerrarMesa","paquete");
		addNodo("idMesa",idMesa+"","paquete");
	}
}
