package prg.pi.restaurantecamarero.xml;

import XML.XML;

/**
 * Clase encargada de formar el xml para pedir toda la información necesaria del restaurante.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class XMLDameloTodo extends XML{
	
	/**
	 * Constructor: genera la estructura del mensaje XML con su contenido
	 */
    public XMLDameloTodo(){
        init();
        addNodo("tipo", "DameloTodo", "paquete");
    }
    
}
