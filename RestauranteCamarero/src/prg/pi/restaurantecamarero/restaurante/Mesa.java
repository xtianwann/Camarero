package prg.pi.restaurantecamarero.restaurante;

/*
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 */
public class Mesa {
    
    private int id;
    private String nombre;
    
    public Mesa(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
