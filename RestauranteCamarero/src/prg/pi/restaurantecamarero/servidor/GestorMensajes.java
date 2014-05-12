package prg.pi.restaurantecamarero.servidor;

import Conexion.Conexion;
import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import prg.pi.restaurantecamarero.MainFragments;
import prg.pi.restaurantecamarero.decodificador.DecodificadorPedidosListos;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class GestorMensajes extends Thread {

	private Socket socket;
	Conexion conn;
	private String mensaje;
	private MainFragments principal;

	public GestorMensajes(Socket socket, MainFragments principal) {
		this.principal = principal;
		this.socket = socket;
		conn = new Conexion(socket);
		do {
			this.mensaje = conn.leerMensaje();
		} while (this.mensaje.length() == 0);
	}

	public void run() {
		try {
			System.out.println("GestorMesaje: Mensaje!");
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder;
			mensaje = mensaje.substring(mensaje.indexOf("<"));
			builder = factory.newDocumentBuilder();
			final Document dom = builder.parse(new InputSource(new StringReader(
					mensaje)));

			NodeList nodo = dom.getElementsByTagName("tipo");
			String tipo = nodo.item(0).getChildNodes().item(0).getNodeValue();

			if (tipo.equals("PedidosListos")) {
				new Thread(new Runnable() {

					public void run() {
						principal.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								DecodificadorPedidosListos pedidos = new DecodificadorPedidosListos(dom);
								principal.addPedidosListos(pedidos.getPedidosListos());
							}
						});
					}
				}).start();
			}
			if(tipo.equals("ModificacionCB")){
				new Thread(new Runnable() {

					public void run() {
						principal.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								DecodificadorPedidosListos pedidos = new DecodificadorPedidosListos(dom);
								principal.addPedidosListos(pedidos.getPedidosListos());
							}
						});
					}
				}).start();
			}

		} catch (SAXException ex) {
			Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (ParserConfigurationException ex) {
			Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}
}
