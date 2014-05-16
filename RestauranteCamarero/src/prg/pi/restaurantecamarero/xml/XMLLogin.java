package prg.pi.restaurantecamarero.xml;

import XML.XML;

public class XMLLogin extends XML{
	
	public XMLLogin(String usuario){
		init();
		addNodo("tipo", "LoginCamarero","paquete");
		addNodo("usuario", usuario, "paquete");
	}

}
