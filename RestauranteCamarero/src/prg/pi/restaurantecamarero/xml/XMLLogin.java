package prg.pi.restaurantecamarero.xml;

import XML.XML;
/**
 * 
 * Clase encargada de formar el xml con toda la informaci�n para loguear a un camarero.
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class XMLLogin extends XML{
	/**
	 * 
	 * Constructor:
	 * 
	 * @param usuario [String] Nombre del usuario.
	 */
	public XMLLogin(String usuario){
		init();
		addNodo("tipo", "LoginCamarero","paquete");
		addNodo("usuario", usuario, "paquete");
	}

}
