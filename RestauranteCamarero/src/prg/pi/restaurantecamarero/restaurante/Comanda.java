package prg.pi.restaurantecamarero.restaurante;

import java.util.ArrayList;

/**
 * Clase que contiene toda la informaci�n sobre comandas.
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class Comanda {

    private Mesa mesa;
    private ArrayList<Pedido> pedidos;
    
    /**
     * Constructor:
     * 
     * @param mesa [Mesa] Mesa de la comanda.
     * @param pedidos [ArrayList<Pedido>] Lista de pedidos de la comanda.
     */
    public Comanda(Mesa mesa, ArrayList<Pedido> pedidos) {
        this.mesa = mesa;
        this.pedidos = pedidos;
    }
    
    /**
     * Permite obtener la mesa de la comanda.
     * 
     * @return [Mesa] Mesa de la comanda.
     */
    public Mesa getMesa() {
        return mesa;
    }
    
    /**
     * Permite modificar la mesa de la comanda.
     * 
     * @param mesa [Mesa] Mesa a modificar.
     */
    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }
    
    /**
     * Permite obtener lista de pedidos de la comanda.
     * 
     * @return [ArrayList<Pedido>] Lista de pedidos de la comanda.
     */
    public ArrayList<Pedido> getPedidos() {
        return pedidos;
    }
    
    /**
     * Permite modificar la lista de pedidos de la comanda.
     * 
     * @param pedidos [ArrayList<Pedido>] Lista de pedidos a modificar.
     */
    public void setPedidos(ArrayList<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
    
    /**
     * A�ade un pedido a la lista de pedidos de la comanda.
     * 
     * @param pedido [Pedido] Pedido a a�adir a la lista de pedidos.
     */
    public void addPedido(Pedido pedido) {
        pedidos.add(pedido);
    }
}
