package prg.pi.restaurantecamarero.restaurante;

import java.util.ArrayList;

/**
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 */
public class Comanda {

    private Mesa mesa;
    private ArrayList<Pedido> pedidos;

    public Comanda(Mesa mesa, ArrayList<Pedido> pedidos) {
        this.mesa = mesa;
        this.pedidos = pedidos;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public ArrayList<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(ArrayList<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public void addPedido(Pedido pedido) {
        pedidos.add(pedido);
    }
}
