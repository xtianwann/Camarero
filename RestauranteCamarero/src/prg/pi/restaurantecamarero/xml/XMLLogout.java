package prg.pi.restaurantecamarero.xml;

import XML.XML;

/**
 * Clase encargada de formar el xml con toda la informaci�n hacer logout a un camarero.
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class XMLLogout extends XML{
	
	/**
	 * Constructor: genera la estructura del mensaje XML con su contenido
	 * 
	 * @param usuario [String] Nombre del usuario.
	 */
	public XMLLogout(String usuario){
		init();
		addNodo("tipo", "LogoutCamarero", "paquete");
		addNodo("usuario", usuario, "paquete");
	}

}
