package prg.pi.restaurantecamarero.restaurante;

import java.util.ArrayList;

/**
 * Clase que contiene toda la información sobres las cantidades y los productos del restaurante.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Cantidad {

    private String nombre;
    private ArrayList<Producto> productos;
    
    /**
     * Constructor:
     * 
     * @param nombre [String] Nombre de la cantidad.
     * @param productos [ArrayList<Producto>] Lista de productos de la cantidad.
     */
    public Cantidad(String nombre, ArrayList<Producto> productos) {
        this.nombre = nombre;
        this.productos = productos;
    }
    
    /**
     * Permite obtener el nombre de la cantidad.
     * 
     * @return [String] Nombre de la cantidad
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Permite modificar la nombre de una cantidad
     * 
     * @param nombre [String] Nombre de la cantidad
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    /**
     * Permite obtener la lista de productos.
     * 
     * @return [ArrayList<Producto>] Lista de productos.
     */
    public ArrayList<Producto> getProductos() {
        return productos;
    }
    
    /**
     * Permite modificar la lista de productos.
     * 
     * @param productos [ArrayList<Producto>] Lista de productos.
     */
    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }
}
