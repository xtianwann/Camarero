package prg.pi.restaurantecamarero.restaurante;
/**
 * 
 * Clase que contiene toda la informaci�n sobre productos.
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class Producto {

    private int idMenu;
    private String nombreProducto;
    private String cantidadPadre;
    /**
     * 
     * Constructor:
     * 
     * @param idMenu [int] Id del menu del producto.
     * @param nombreProducto [String] Nombre del producto.
     */
    public Producto(int idMenu, String nombreProducto) {
        this.idMenu = idMenu;
        this.nombreProducto = nombreProducto;
    }
    /**
     * 
     * Constructor:
     * 
     * @param idMenu [int] Id del menu del producto.
     * @param nombreProducto [String] Nombre del producto.
     * @param cantidadPadre [String] Cantidad asociada al producto.
     */
    public Producto(int idMenu, String nombreProducto,String cantidadPadre) {
        this.idMenu = idMenu;
        this.nombreProducto = nombreProducto;
        this.cantidadPadre = cantidadPadre;
    }
    /**
     * Permite obtener el id del menu.
     * 
     * @return [int] Id del menu.
     */
    public int getIdMenu() {
        return idMenu;
    }
    /**
     * Permite modificar el id del menu.
     * 
     * @param idMenu [int] Id del menu a modificar.
     */
    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }
    /**
     * Permite obtener el nombre del producto.
     * 
     * @return [String] Nombre del producto.
     */
    public String getNombreProducto() {
        return nombreProducto;
    }
    /**
     * Permite modificar el nombre del producto.
     * 
     * @param nombreProducto [String] Nombre del producto a modificar.
     */
    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }
    /**
     * Permite obtener el nombre de la cantidad asociada al producto.
     * 
     * @return [Mesa] Nombre de la cantidad asociada al producto.
     */
	public String getCantidadPadre() {
		return cantidadPadre;
	}
	/**
     * Permite modificar el nombre de la cantidad asociada al producto.
     * 
     * @param cantidadPadre [String] Nombre de la cantidad a modificar.
     */
	public void setCantidadPadre(String cantidadPadre) {
		this.cantidadPadre = cantidadPadre;
	}
}
