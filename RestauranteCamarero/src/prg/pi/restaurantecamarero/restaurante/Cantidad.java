package prg.pi.restaurantecamarero.restaurante;

import java.util.ArrayList;

/*
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 */
public class Cantidad {

    private String nombre;
    private ArrayList<Producto> productos;

    public Cantidad(String nombre, ArrayList<Producto> productos) {
        this.nombre = nombre;
        this.productos = productos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }
}
