package prg.pi.restaurantecamarero.restaurante;

public class Modificado {
	
	private int idComanda;
	private int idMenu;
	private int unidades;
	
	public Modificado(int idComanda, int idMenu, int unidades){
		this.idComanda = idComanda;
		this.idMenu = idMenu;
		this.unidades = unidades;
	}

	public int getIdComanda() {
		return idComanda;
	}

	public void setIdComanda(int idComanda) {
		this.idComanda = idComanda;
	}

	public int getIdMenu() {
		return idMenu;
	}

	public void setIdMenu(int idMenu) {
		this.idMenu = idMenu;
	}

	public int getUnidades() {
		return unidades;
	}

	public void setUnidades(int unidades) {
		this.unidades = unidades;
	}
	
	

}
