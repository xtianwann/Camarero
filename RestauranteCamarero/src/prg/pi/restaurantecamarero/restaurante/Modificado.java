package prg.pi.restaurantecamarero.restaurante;

/**
 * Clase que contiene toda la información sobre pedidos que se han modificado.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Modificado {
	
	private int idComanda;
	private int idMenu;
	private int unidades;
	
	/**
     * Constructor:
     * 
     * @param idComanda [int] Id de la comanda asociada al pedido.
     * @param idMenu [int] Id del menu asociado al pedido.
     * @param unidades [int] Unidades a modificar.
     */
	public Modificado(int idComanda, int idMenu, int unidades){
		this.idComanda = idComanda;
		this.idMenu = idMenu;
		this.unidades = unidades;
	}
	
	/**
     * Permite obtener la id de la comanda.
     * 
     * @return [int] Id de la comanda.
     */
	public int getIdComanda() {
		return idComanda;
	}
	
	/**
     * Permite modificar la id de la comanda.
     * 
     * @param idComanda [int] Id de la comanda a modificar.
     */
	public void setIdComanda(int idComanda) {
		this.idComanda = idComanda;
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
     * Permite obtener las unidades del pedido.
     * 
     * @return [int] Unidades del pedido.
     */
	public int getUnidades() {
		return unidades;
	}
	
	/**
     * Permite modificar las unidades de un pedido.
     * 
     * @param unidades [int] Unidades a modificar.
     */
	public void setUnidades(int unidades) {
		this.unidades = unidades;
	}
	
	

}
