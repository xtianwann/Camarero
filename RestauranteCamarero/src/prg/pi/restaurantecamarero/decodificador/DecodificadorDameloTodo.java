package prg.pi.restaurantecamarero.decodificador;


import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import prg.pi.restaurantecamarero.restaurante.Cantidad;
import prg.pi.restaurantecamarero.restaurante.Mesa;
import prg.pi.restaurantecamarero.restaurante.Producto;
import prg.pi.restaurantecamarero.restaurante.Seccion;

/**
 * Clase encargada de decodificar el mensaje DameloTodoServer del servidor.
 * 
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 */
public class DecodificadorDameloTodo {
    
    private Document DOMRespuesta;
    private ArrayList<Seccion> secciones;
    private ArrayList<Cantidad> cantidades;
    
    /**
	 * Constructor:
	 * 
	 * @param dom [Document] DOM del XMl a interpretar.
	 */
    public DecodificadorDameloTodo(Document dom){
        secciones = new ArrayList<Seccion>();
        cantidades = new ArrayList<Cantidad>();
        this.DOMRespuesta = dom;
        generarSecciones();
        generarCantidades();
    }
    
    /**
	 * Interpreta el mensaje y genera las secciones del restaurante.
	 */
    private void generarSecciones(){
        NodeList nodeListSecciones = DOMRespuesta.getElementsByTagName("secciones").item(0).getChildNodes();
        for(int contadorSecciones = 0; contadorSecciones < nodeListSecciones.getLength(); contadorSecciones++){
            /* Obtenemos el nombre de la sección */
            Node nodoSeccion = nodeListSecciones.item(contadorSecciones);
            Element elementoSeccion = (Element) nodoSeccion;
            String nombreSeccion = elementoSeccion.getAttribute("nomSec");
            
            /* Obtenemos todas las mesas de la sección */
            ArrayList<Mesa> listaMesas = new ArrayList<Mesa>();
            NodeList nodeListMesas = nodoSeccion.getChildNodes();
            for(int contadorMesas = 0; contadorMesas < nodeListMesas.getLength(); contadorMesas++){
                Node nodoMesa = nodeListMesas.item(contadorMesas);
                Element elementoMesa = (Element) nodoMesa;
                int idMesa = Integer.parseInt(elementoMesa.getAttribute("idMes"));
                String nombreMesa = elementoMesa.getChildNodes().item(0).getNodeValue();
                listaMesas.add(new Mesa(idMesa, nombreMesa));
            }
            
            /* Añadimos la sección al ArrayList correspondiente */
            secciones.add(new Seccion(nombreSeccion, listaMesas));
        }
    }
    
    /**
	 * Interpreta el mensaje y genera las cantidades del restaurante.
	 */
    private void generarCantidades(){
        NodeList nodeListCantidades = DOMRespuesta.getElementsByTagName("cantidades").item(0).getChildNodes();
        for(int contadorCantidades = 0; contadorCantidades < nodeListCantidades.getLength(); contadorCantidades++){
            /* Obtenemos el nombre de la cantidad */
            Node nodoCantidad = nodeListCantidades.item(contadorCantidades);
            Element elementoCantidad = (Element) nodoCantidad;
            String nombreCantidad = elementoCantidad.getAttribute("nomCant");
            
            /* Obtenemos todos los productos que tienen esa cantidad */
            ArrayList<Producto> listaProductos = new ArrayList<Producto>();
            NodeList nodeListProductos = nodoCantidad.getChildNodes();
            for(int contadorProductos = 0; contadorProductos < nodeListProductos.getLength(); contadorProductos++){
                Node nodoProducto = nodeListProductos.item(contadorProductos);
                Element elementoProducto = (Element) nodoProducto;
                int idMenu = Integer.parseInt(elementoProducto.getAttribute("idMenu"));
                String nombreProducto = elementoProducto.getChildNodes().item(0).getNodeValue();
                listaProductos.add(new Producto(idMenu, nombreProducto));
            }
            
            /* Añadimos la cantidad al ArrayList correspondiente */
            cantidades.add(new Cantidad(nombreCantidad, listaProductos));
        }
    }
    
    /**
	 * Devuelve las secciones del restaurante.
	 * 
	 * @return [ArrayList<Seccion>] Secciones del restaurante.
	 */
    public ArrayList<Seccion> getSecciones(){
        return secciones;
    }
    
    /**
	 * Devuelve las cantidades del restaurante.
	 * 
	 * @return [ArrayList<Cantidad>] Cantidades del restaurante.
	 */
    public ArrayList<Cantidad> getCantidades(){
        return cantidades;
    }
    
}
