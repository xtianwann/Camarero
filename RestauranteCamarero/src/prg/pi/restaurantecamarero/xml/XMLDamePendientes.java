package prg.pi.restaurantecamarero.xml;

import prg.pi.restaurantecamarero.MainActivity;
import XML.XML;

public class XMLDamePendientes extends XML{
	
	public XMLDamePendientes(){
		init();
		addNodo("tipo", "DamePendientes", "paquete");
		addNodo("usuario", MainActivity.getUsuarioActual(), "paquete");
	}

}
