package prg.pi.restaurantecamarero.conexion;

import java.io.IOException;
import java.net.ConnectException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.util.Log;
import prg.pi.restaurantecamarero.decodificador.DecodificadorAcuseRecibo;
import prg.pi.restaurantecamarero.decodificador.DecodificadorDameloTodo;
import prg.pi.restaurantecamarero.decodificador.DecodificadorPedidosPendientesCamarero;
import prg.pi.restaurantecamarero.decodificador.DecodificadorPendientesAlEncender;
import prg.pi.restaurantecamarero.decodificador.DecodificadorResultadoLogin;
import Conexion.Conexion;
import XML.XML;

/**
 * Clase encargada de el envío,recepción e interpretación de los mensajes que se comunican con el servidor.
 * 
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 */
public class Cliente {
	private Conexion conn;
	private String mensaje;
	private String respuesta;
	private DecodificadorDameloTodo todo;
	private DecodificadorPedidosPendientesCamarero pedidosPendientes;
	private DecodificadorResultadoLogin resultadoLogin;
	private DecodificadorPendientesAlEncender pendientesAlEncender;
	private DecodificadorAcuseRecibo decoAcuse;
	private String ipServidor;
	
	/**
	 * Constructor
	 * 
	 * @param mensaje [String] mensaje que se le enviará al servidor
	 * @param ipServidor [String] ip del servidor
	 */
	public Cliente(String mensaje,String ipServidor) {
		this.ipServidor = ipServidor;
		respuesta = "";
		this.mensaje = mensaje;
	}
	
	/**
	 * Método encargado de enviar el mensaje, obtener la respuesta e interpretarla
	 */
	public void init() {
		try {
			enviarMensaje(mensaje);
			respuesta = recibirMensaje();
		} catch (NullPointerException e){
			throw new NullPointerException();
		} catch (IOException e) {
			throw new NullPointerException();
		}
		if (respuesta != null && respuesta.length() > 0) {
			Document dom = XML.stringToXml(respuesta);
			NodeList nodeListTipo = dom.getElementsByTagName("tipo");
			String tipo = nodeListTipo.item(0).getChildNodes().item(0)
					.getNodeValue();

			/* Tipos de respuesta */
			if (tipo.equals("AcuseRecibo")) {
				decoAcuse = new DecodificadorAcuseRecibo(
						dom);
			}
			if (tipo.equals("DameloTodo")) {
				todo = new DecodificadorDameloTodo(dom);
			}
			if (tipo.equals("PedidosPendientesCamarero")) {
				pedidosPendientes = new DecodificadorPedidosPendientesCamarero(
						dom);
			}
			if(tipo.equals("ResultadoLoginCamarero")) {
				resultadoLogin = new DecodificadorResultadoLogin(dom);
			}
			if(tipo.equals("ResultadoLogoutCamarero")) {
				resultadoLogin = new DecodificadorResultadoLogin(dom);
			}
			if(tipo.equals("PendientesAlEncender")){
				pendientesAlEncender = new DecodificadorPendientesAlEncender(dom);
			}
			try {
				conn.cerrarConexion();
			} catch (NullPointerException e){
			} catch (IOException e) {
			}

		} else {
			try {
				conn.cerrarConexion();
			} catch (NullPointerException e){
				
			} catch (IOException e) {
			}
			System.out.println("Agotado tiempo de espera...");
		}
	}

	/**
	 * Establece conexión con el servidor y envía el mensaje pasado por
	 * parámetro
	 * 
	 * @param msg [String] mensaje a enviar
	 * @throws IOException excepción lanzada en caso de que hubiera algún tipo de error en la entrada o salida de datos
	 * @throws ConnectException excepción lanzada en caso de haber algún tipo de error en la conexión
	 */
	public void enviarMensaje(String msg) throws IOException,
			NullPointerException {
		conexion();
		conn.escribirMensaje(msg);
	}

	/**
	 * Espera un mensaje del servidor durante cinco segundos
	 * 
	 * @return [String] respuesta del servidor, null si excede el límite de tiempo
	 */
	public String recibirMensaje() throws IOException, NullPointerException {
		String respuesta = null;
		int intento = 0;
		do {
			respuesta = conn.leerMensaje();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			intento++;
			Log.e("cliente", "longitud " + respuesta.length());
		} while (respuesta.length() == 0 && intento < 10);
		return respuesta;
	}

	/**
	 * Establece conexión con el servidor
	 * 
	 * @throws IOException, ConnectException
	 */
	private void conexion() throws IOException, NullPointerException {
		conn = new Conexion(ipServidor, 27000);
	}
	
	/**
	 * Devuelve el decodificador que interpreta el mensaje DameloTodo.
	 * 
	 * @return [DecodificadorDameloTodo] Decodificador de mensaje DameloTodo.
	 */
	public DecodificadorDameloTodo getTodo() {
		return todo;
	}
	
	/**
	 * Modifica el decodificador que interpreta el mensaje DameloTodo.
	 * 
	 * @param todo [DecodificadorDameloTodo] Decodificador de mensaje DameloTodo.
	 */
	public void setTodo(DecodificadorDameloTodo todo) {
		this.todo = todo;
	}
	
	/**
	 * Devuelve el decodificador que interpreta el mensaje PedidosPendientesCamarero.
	 * 
	 * @return [DecodificadorPedidosPendientesCamarero] Decodificador de mensaje PedidosPendientesCamarero.
	 */
	public DecodificadorPedidosPendientesCamarero getPedidosPendientes() {
		return pedidosPendientes;
	}
	
	/**
	 * Devuelve el decodificador que interpreta el mensaje ResultadoLoginCamarero.
	 * 
	 * @return [DecodificadorResultadoLogin] Decodificador de mensaje ResultadoLoginCamarero.
	 */
	public DecodificadorResultadoLogin getResultadoLogin(){
		return resultadoLogin;
	}
	
	/**
	 * Devuelve el decodificador que interpreta el mensaje PendientesCamareroAlEncender.
	 * 
	 * @return [DecodificadorPendientesAlEncender] Decodificador de mensaje PendientesCamareroAlEncender.
	 */
	public DecodificadorPendientesAlEncender getDecoPendientes(){
		return pendientesAlEncender;
	}
	
	/**
	 * Devuelve el decodificador que interpreta el mensaje AcuseReciboServer.
	 * 
	 * @return [DecodificadorAcuseRecibo] Decodificador de mensaje AcuseReciboServer.
	 */
	public DecodificadorAcuseRecibo getDecoAcuse(){
		return decoAcuse;
	}
}
