package prg.pi.restaurantecamarero.restaurante;
/**
 * 
 * Clase que contiene toda la información sobre pedidos pendientes del camarero.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class PedidosPendientesCamarero {
	
	private String nombreSeccion;
	private String nombreMesa;
	private int idComanda;
	private Producto producto;
	private int unidades;
	private int listos;
	private int servidos;
	/**
     * 
     * Constructor:
     * 
     * @param nombreSeccion [String] Nombre de la sección asociada al pedido.
     * @param nombreMesa [String] Nombre de la mesa asociada al pedido.
     * @param idComanda [int] Id de la comanda asociada al pedido.
     * @param producto [Producto] Producto asociado al pedido.
     * @param unidades [int] Unidades del producto asociado al pedido.
     */
	public PedidosPendientesCamarero(String nombreSeccion, String nombreMesa, int idComanda, Producto producto, int unidades,int listos, int servidos){
		this.nombreSeccion= nombreSeccion;
		this.nombreMesa = nombreMesa;
		this.idComanda = idComanda;
		this.producto = producto;
		this.unidades = unidades;
		this.listos = listos;
		this.servidos = servidos;
	}
	/**
     * Permite obtener el nombre de la sección.
     * 
     * @return [String] Nombre de la sección.
     */
	public String getNombreSeccion() {
		return nombreSeccion;
	}
	/**
     * Permite modificar el nombre de la sección.
     * 
     * @param nombreSeccion [String] Nombre de la sección a modificar.
     */
	public void setNombreSeccion(String nombreSeccion) {
		this.nombreSeccion = nombreSeccion;
	}
	/**
     * Permite obtener el nombre de la mesa.
     * 
     * @return [String] Nombre de la mesa.
     */
	public String getNombreMesa() {
		return nombreMesa;
	}
	/**
     * Permite modificar el nombre de la mesa.
     * 
     * @param nombreMesa [String] Nombre de la mesa a modificar.
     */
	public void setNombreMesa(String nombreMesa) {
		this.nombreMesa = nombreMesa;
	}
	/**
     * Permite obtener el id de la comanda.
     * 
     * @return [int] Id de la comanda.
     */
	public int getIdComanda() {
		return idComanda;
	}
	/**
     * Permite modificar el id de la comanda.
     * 
     * @param idComanda [int] Id de la comanda a modificar.
     */
	public void setIdComanda(int idComanda) {
		this.idComanda = idComanda;
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
     * Permite modificar el producto.
     * 
     * @param producto [Producto] Producto a modificar.
     */
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	/**
     * Permite obtener la cantidad de pedidos listos.
     * 
     * @return [int] Cantidad pedidos listos.
     */
	public int getListos() {
		return listos;
	}
	/**
     * Permite modificar la cantidad de pedidos listos.
     * 
     * @param listos [int] Número de pedidos listos a modificar.
     */
	public void setListos(int listos) {
		this.listos = listos;
	}
	/**
     * Permite obtener la cantidad de pedidos servidos.
     * 
     * @return [int] Cantidad pedidos servidos.
     */
	public int getServidos() {
		return servidos;
	}
	/**
     * Permite modificar la cantidad de pedidos servidos.
     * 
     * @param servidos [int] Número de pedidos servidos a modificar.
     */
	public void setServidos(int servidos) {
		this.servidos = servidos;
	}
	/**
     * Devuelve si hay mas pedidos listos que servidos
     * 
     * @return [boolean] true si existen mas listos que servidos y false en caso contrario.
     */
	public boolean existenListos(){
		return listos > servidos;
	}
	/**
     * Devuelve si se han servido todos los pedidos
     * 
     * @return [boolean] true si se han servido todos los pedidos y false en caso contrario.
     */
	public boolean isServido(){
		return unidades <= servidos;
	}
	/**
     * Permite obtener la cantidad de pedidos.
     * 
     * @return [int] Cantidad de pedidos.
     */
	public int getUnidades() {
		return unidades;
	}
	/**
     * Permite modificar la cantidad de pedidos.
     * 
     * @param unidades [int] Número de pedidos a modificar.
     */
	public void setUnidades(int unidades) {
		this.unidades = unidades;
	}
}
