package prg.pi.restaurantecamarero.xml;

import prg.pi.restaurantecamarero.restaurante.Modificado;
import prg.pi.restaurantecamarero.restaurante.PedidosPendientesCamarero;
import XML.XML;

public class XMLModificacionCamarero extends XML{
	
	public XMLModificacionCamarero(PedidosPendientesCamarero[] modificados){
		init();
		addNodo("tipo", "ModificacionCamarero", "paquete");
		addNodo("modificados", null, "paquete");
		for(int modificado = 0; modificado < modificados.length; modificado++){
			addNodo("modificado", null, "modificados");
			addNodo("idCom", modificados[modificado].getIdComanda()+"", "modificado");
			addNodo("idMenu", modificados[modificado].getProducto().getIdMenu()+"", "modificado");
			addNodo("unidades", modificados[modificado].getUnidades()+"", "modificado");
		}
	}

}
