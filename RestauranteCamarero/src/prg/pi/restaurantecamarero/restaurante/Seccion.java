package prg.pi.restaurantecamarero.restaurante;

import java.util.ArrayList;

/*
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 */
public class Seccion {
    
    private String nombre;
    private ArrayList<Mesa> mesas;

    public Seccion(String nombre, ArrayList<Mesa> mesas){
        this.nombre = nombre;
        this.mesas = mesas;
    }

    public ArrayList<Mesa> getMesas() {
        return mesas;
    }

    public void setMesas(ArrayList<Mesa> mesas) {
        this.mesas = mesas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void addMesa(Mesa mesa){
        mesas.add(mesa);
    }
}
