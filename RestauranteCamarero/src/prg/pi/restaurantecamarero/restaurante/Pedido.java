package prg.pi.restaurantecamarero.restaurante;

/**
 * Clase que contiene toda la informaci�n sobre pedidos.
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class Pedido {

    private Producto producto;
    private int unidades;
    private String estado;
    
    /**
     * Constructor:
     * 
     * @param producto [Producto] Producto a a�adir al pedido.
     * @param unidades [int] Unidades del producto a a�adir al pedido.
     */
    public Pedido(Producto producto, int unidades) {
        this.producto = producto;
        this.unidades = unidades;
        this.estado = null;
    }
    
    /**
     * Constructor:
     * 
     * @param producto [Producto] Producto a a�adir al pedido.
     * @param unidades [int] Unidades del producto a a�adir al pedido.
     * @param estado [String] Estado del pedido.
     */
    public Pedido(Producto producto, int unidades, String estado) {
        this.producto = producto;
        this.unidades = unidades;
        this.estado = estado;
    }
    
    /**
     * Permite obtener el estado del pedido.
     * 
     * @return [String] Estado del pedido.
     */
    public String getEstado() {
        return estado;
    }
    
    /**
     * Permite modificar el estado del pedido.
     * 
     * @param estado [String] Estado del pedido a modificar.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    /**
     * Permite obtener el producto del pedido.
     * 
     * @return [Producto] Producto del pedido.
     */
    public Producto getProducto() {
        return producto;
    }
    
    /**
     * Permite modificar el producto del pedido.
     * 
     * @param producto [Producto] Producto del pedido a modificar.
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    /**
     * Permite obtener las unidades del pedido.
     * 
     * @return [int] Unidades del pedido.
     */
    public int getUnidades() {
        return unidades;
    }
    
    /**
     * Permite modificar las unidades del pedido.
     * 
     * @param unidades [int] Unidades del pedido a modificar.
     */
    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }
}
