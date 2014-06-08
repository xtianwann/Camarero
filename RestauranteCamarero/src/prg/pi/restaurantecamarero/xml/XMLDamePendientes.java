package prg.pi.restaurantecamarero.xml;

import prg.pi.restaurantecamarero.MainActivity;
import XML.XML;

/**
 * Clase encargada de formar el xml para recibir los pedidos pendientes de un camarero.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class XMLDamePendientes extends XML{
	
	/**
	 * Constructor: genera la estructura del mensaje XML con su contenido
	 */
	public XMLDamePendientes(){
		init();
		addNodo("tipo", "DamePendientes", "paquete");
		addNodo("usuario", MainActivity.getUsuarioActual(), "paquete");
	}

}
