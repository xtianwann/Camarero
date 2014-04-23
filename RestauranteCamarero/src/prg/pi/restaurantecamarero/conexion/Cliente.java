package prg.pi.restaurantecamarero.conexion;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import prg.pi.restaurantecamarero.decodificador.DecodificadorAcuseRecibo;
import prg.pi.restaurantecamarero.decodificador.DecodificadorDameloTodo;
import prg.pi.restaurantecamarero.decodificador.DecodificadorResumenMesa;
import prg.pi.restaurantecamarero.restaurante.Pedido;

import XML.XML;
import android.util.Log;

import Conexion.Conexion;

/**
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 */
public class Cliente extends Thread {
	private Conexion conn;
	private String mensaje;
	private String respuesta;
	private DecodificadorDameloTodo todo;

	public Cliente(String mensaje) {
		respuesta = "";
		this.mensaje = mensaje;
	}

	public void run() {

		enviarMensaje(mensaje);
		respuesta = recibirMensaje();
		if (respuesta.length() > 0) {
			Document dom = XML.stringToXml(respuesta);
			NodeList nodeListTipo = dom.getElementsByTagName("tipo");
			String tipo = nodeListTipo.item(0).getChildNodes().item(0)
					.getNodeValue();

			if (tipo.equals("AcuseRecibo")) {
				DecodificadorAcuseRecibo acuseRecibo = new DecodificadorAcuseRecibo(
						dom);
				System.out.println("Respuesta: "
						+ acuseRecibo.getRespuesta()[0] + ", "
						+ acuseRecibo.getRespuesta()[1]);
			}
			if (tipo.equals("DameloTodo")) {
				todo = new DecodificadorDameloTodo(dom);
			}
			if (tipo.equals("ResumenMesa")) {
				DecodificadorResumenMesa resumenMesa = new DecodificadorResumenMesa(
						dom);
				for (Pedido p : resumenMesa.getResumen()) {
					System.out.println(p.getProducto().getNombreProducto()
							+ " --- " + p.getUnidades() + " --- "
							+ p.getEstado());
				}
			}

		} else {
			System.out.println("Agotado tiempo de espera...");
		}
	}

	/**
	 * Establece conexión con el servidor y envía el mensaje pasado por
	 * parámetro
	 * 
	 * @param msg
	 *            mensaje a enviar
	 */
	public void enviarMensaje(String msg) {
		conexion();
		conn.escribirMensaje(msg);
	}

	/**
	 * Espera un mensaje del servidor durante cinco segundos
	 * 
	 * @return String de respuestas del servidor
	 * @return null si excede el límite de tiempo
	 */
	public String recibirMensaje() {
		String respuesta = null;
		long espera = System.currentTimeMillis() + 10000;
		do {
			respuesta = conn.leerMensaje();
		} while (respuesta.length() == 0 || espera < System.currentTimeMillis());

		return respuesta;
	}

	/**
	 * Establece conexión con el servidor
	 */
	private void conexion() {
		try {
			conn = new Conexion("192.168.20.5", 27014);
		} catch (IOException ex) {
			Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

	public DecodificadorDameloTodo getTodo() {
		return todo;
	}

	public void setTodo(DecodificadorDameloTodo todo) {
		this.todo = todo;
	}
}
