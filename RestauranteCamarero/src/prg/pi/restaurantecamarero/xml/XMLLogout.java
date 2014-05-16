package prg.pi.restaurantecamarero.xml;

import XML.XML;

public class XMLLogout extends XML{
	
	public XMLLogout(String usuario){
		init();
		addNodo("tipo", "LogoutCamarero", "paquete");
		addNodo("usuario", usuario, "paquete");
	}

}
