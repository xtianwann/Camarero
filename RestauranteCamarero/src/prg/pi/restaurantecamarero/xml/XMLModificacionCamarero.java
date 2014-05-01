package prg.pi.restaurantecamarero.xml;

import prg.pi.restaurantecamarero.restaurante.Modificado;
import XML.XML;

public class XMLModificacionCamarero extends XML{
	
	public XMLModificacionCamarero(Modificado[] modificados){
		init();
		addNodo("tipo", "ModificacionCamarero", "paquete");
		addNodo("modificados", null, "paquete");
		for(int modificado = 0; modificado < modificados.length; modificado++){
			addNodo("modificado", null, "modificados");
			addNodo("idCom", modificados[modificado].getIdComanda()+"", "modificado");
			addNodo("idMenu", modificados[modificado].getIdMenu()+"", "modificado");
			addNodo("unidades", modificados[modificado].getUnidades()+"", "modificado");
		}
	}

}
