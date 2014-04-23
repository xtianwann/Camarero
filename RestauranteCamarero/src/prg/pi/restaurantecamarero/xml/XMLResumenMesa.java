package prg.pi.restaurantecamarero.xml;

import XML.XML;

/**
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 */
public class XMLResumenMesa extends XML{

    public XMLResumenMesa(int idMesa) {
        init();
        addNodo("tipo", "ResumenMesa", "paquete");
        addNodo("idMes", idMesa+"", "paquete");
    }
    
}
