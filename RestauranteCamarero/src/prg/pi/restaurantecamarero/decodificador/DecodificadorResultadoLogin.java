package prg.pi.restaurantecamarero.decodificador;

import org.w3c.dom.Document;

/**
 * Clase encargada de decodificar el mensaje ResultadoLoginCamarero del servidor.
 * 
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 */
public class DecodificadorResultadoLogin {
	
	private Document dom;
	private String resultado;
	
	/**
	 * Constructor:
	 * 
	 * @param dom [Document] DOM del XMl a interpretar.
	 */
	public DecodificadorResultadoLogin(Document dom){
		this.dom = dom;
		extraerResultado();
	}
	
	/**
	 * Devuelve la respuesta recibida por del servidor.
	 * 
	 * @return [String] Respuesta del servidor.
	 */
	public String getResultado(){
		return resultado;
	}
	
	/**
	 * Interpreta la respuesta recibida por el servidor.
	 * 
	 */
	private void extraerResultado(){
		resultado = dom.getElementsByTagName("resultado").item(0).getFirstChild().getNodeValue();
	}

}
