package prg.pi.restaurantecamarero.restaurante;

/**
 * Clase que contiene toda la información sobre los pedidos listos.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class PedidoListo {
	
	private int idComanda;
	private int idMenu;
	private int listos;
	
	/**
     * Constructor:
     * 
     * @param idComanda [int] Id de la comanda asociada al pedido.
     * @param idMenu [int] Id del menu asociado al pedido.
     * @param listos [int] Unidades listas a modificar.
     */
	public PedidoListo(int idComanda, int idMenu, int listos){
		this.idComanda = idComanda;
		this.idMenu = idMenu;
		this.listos = listos;
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
     * Permite obtener las unidades listas del pedido.
     * 
     * @return [int] Unidades del pedido.
     */
	public int getListos() {
		return listos;
	}
	
	/**
     * Permite modificar las unidades listas de un pedido.
     * 
     * @param listos [int] Unidades listas a modificar.
     */
	public void setListos(int listos) {
		this.listos = listos;
	}
}
