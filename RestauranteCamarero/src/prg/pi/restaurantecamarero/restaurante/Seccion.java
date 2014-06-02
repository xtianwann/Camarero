package prg.pi.restaurantecamarero.restaurante;

import java.util.ArrayList;
/**
 * 
 * Clase que contiene toda la informaci�n sobre secciones.
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class Seccion {
    
    private String nombre;
    private ArrayList<Mesa> mesas;
    /**
     * 
     * Constructor:
     * 
     * @param nombre [String] Nombre de la secci�n.
     * @param mesas [ArrayList<Mesa>] Lista de mesas de la secci�n.
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
     * Permite modificar la lista de mesas de la secci�n.
     * 
     * @param mesas [ArrayList<Mesa>] Lista de mesas a modificar.
     */
    public void setMesas(ArrayList<Mesa> mesas) {
        this.mesas = mesas;
    }
    /**
     * Permite obtener nombre de la secci�n.
     * 
     * @return [String] Nombre de la secci�n.
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * Permite modificar el nombre de la secci�n.
     * 
     * @param nombre [String] Nombre de la secci�n a modificar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    /**
     * Permite a�adir una mesa a la lista de mesas de la secci�n.
     * 
     * @param mesa [Mesa] Mesa a a�adir.
     */
    public void addMesa(Mesa mesa){
        mesas.add(mesa);
    }
}
