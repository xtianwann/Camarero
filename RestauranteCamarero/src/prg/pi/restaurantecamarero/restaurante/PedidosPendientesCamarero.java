package prg.pi.restaurantecamarero.restaurante;

public class PedidosPendientesCamarero {
	
	private String nombreSeccion;
	private String nombreMesa;
	private int idComanda;
	private Producto producto;
	private int unidades;
	private int listos;
	private int servidos;
	
	public PedidosPendientesCamarero(String nombreSeccion, String nombreMesa, int idComanda, Producto producto, int unidades,int listos, int servidos){
		this.nombreSeccion= nombreSeccion;
		this.nombreMesa = nombreMesa;
		this.idComanda = idComanda;
		this.producto = producto;
		this.unidades = unidades;
		this.listos = listos;
		this.servidos = servidos;
	}

	public String getNombreSeccion() {
		return nombreSeccion;
	}

	public void setNombreSeccion(String nombreSeccion) {
		this.nombreSeccion = nombreSeccion;
	}

	public String getNombreMesa() {
		return nombreMesa;
	}

	public void setNombreMesa(String nombreMesa) {
		this.nombreMesa = nombreMesa;
	}

	public int getIdComanda() {
		return idComanda;
	}

	public void setIdComanda(int idComanda) {
		this.idComanda = idComanda;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public int getListos() {
		return listos;
	}

	public void setListos(int listos) {
		this.listos = listos;
	}

	public int getServidos() {
		return servidos;
	}

	public void setServidos(int servidos) {
		this.servidos = servidos;
	}
	public boolean existenListos(){
		return listos > servidos;
	}
	public boolean isServido(){
		return unidades == servidos;
	}

	public int getUnidades() {
		return unidades;
	}

	public void setUnidades(int unidades) {
		this.unidades = unidades;
	}
}
