package prg.pi.restaurantecamarero.xml;

import XML.XML;
/**
 * 
 * Clase encargada de formar el xml para pedir toda la informaci�n necesaria del restaurante.
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class XMLDameloTodo extends XML{
	/**
	 * 
	 * Constructor:
	 * 
	 */
    public XMLDameloTodo(){
        init();
        addNodo("tipo", "DameloTodo", "paquete");
    }
    
}
