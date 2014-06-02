package prg.pi.restaurantecamarero.restaurante;

import java.util.ArrayList;
/**
 * 
 * Clase que contiene toda la información sobre secciones.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Seccion {
    
    private String nombre;
    private ArrayList<Mesa> mesas;
    /**
     * 
     * Constructor:
     * 
     * @param nombre [String] Nombre de la sección.
     * @param mesas [ArrayList<Mesa>] Lista de mesas de la sección.
     */
    public Seccion(String nombre, ArrayList<Mesa> mesas){
        this.nombre = nombre;
        this.mesas = mesas;
    }
    /**
     * Permite obtener la lista de mesas.
     * 
     * @return [ArrayList<Mesa>] Lista de mesas.
     */
    public ArrayList<Mesa> getMesas() {
        return mesas;
    }
    /**
     * Permite modificar la lista de mesas de la sección.
     * 
     * @param mesas [ArrayList<Mesa>] Lista de mesas a modificar.
     */
    public void setMesas(ArrayList<Mesa> mesas) {
        this.mesas = mesas;
    }
    /**
     * Permite obtener nombre de la sección.
     * 
     * @return [String] Nombre de la sección.
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * Permite modificar el nombre de la sección.
     * 
     * @param nombre [String] Nombre de la sección a modificar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    /**
     * Permite añadir una mesa a la lista de mesas de la sección.
     * 
     * @param mesa [Mesa] Mesa a añadir.
     */
    public void addMesa(Mesa mesa){
        mesas.add(mesa);
    }
}
