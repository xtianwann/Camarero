package prg.pi.restaurantecamarero.restaurante;

/**
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 */
public class Pedido {

    private Producto producto;
    private int unidades;
    private String estado;
    
    public Pedido(Producto producto, int unidades) {
        this.producto = producto;
        this.unidades = unidades;
        this.estado = null;
    }

    public Pedido(Producto producto, int unidades, String estado) {
        this.producto = producto;
        this.unidades = unidades;
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }
}
