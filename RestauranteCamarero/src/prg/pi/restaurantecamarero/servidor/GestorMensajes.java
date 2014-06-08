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
import prg.pi.restaurantecamarero.decodificador.DecodificadorPendientesAlEncender;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Interpreta la cabecera del mensaje y ejecuta la acci�n que le corresponde
 * para tratarlo.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class GestorMensajes extends Thread {

	private Socket socket;
	private Conexion conn;
	private String mensaje;
	private MainFragments principal;

	public GestorMensajes(Socket socket, MainFragments principal) {
		this.principal = principal;
		this.socket = socket;
		try {
			conn = new Conexion(socket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		do {
			try {
				this.mensaje = conn.leerMensaje();
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} while (this.mensaje.length() == 0);
	}

	public void run() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			mensaje = mensaje.substring(mensaje.indexOf("<"));
			builder = factory.newDocumentBuilder();
			final Document dom = builder.parse(new InputSource(new StringReader(
					mensaje)));

			NodeList nodo = dom.getElementsByTagName("tipo");
			String tipo = nodo.item(0).getChildNodes().item(0).getNodeValue();
			
			/* Tipos de mensaje que puede recibir */
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
			
			if(tipo.equals("PendientesAlEncender")){
				new Thread(new Runnable() {

					public void run() {
						principal.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								DecodificadorPendientesAlEncender pedidos = new DecodificadorPendientesAlEncender(dom);
								principal.addPedidosPendientes(pedidos.getPedidosPendientes());
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
