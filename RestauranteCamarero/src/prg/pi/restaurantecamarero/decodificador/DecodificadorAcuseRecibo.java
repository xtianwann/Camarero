package prg.pi.restaurantecamarero.decodificador;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 */
public class DecodificadorAcuseRecibo {

	private Document DOMRespuesta;
	String aceptado = "";
	String explicacion = "";

	public DecodificadorAcuseRecibo(Document dom) {
		this.DOMRespuesta = dom;
		interpretarRespuesta();
	}

	private void interpretarRespuesta() {
		NodeList nodeListAceptado = DOMRespuesta
				.getElementsByTagName("respuesta");
		aceptado = nodeListAceptado.item(0).getChildNodes().item(0)
				.getNodeValue();
		NodeList nodeListExplicacion = DOMRespuesta
				.getElementsByTagName("explicacion");
		try{
		explicacion = nodeListExplicacion.item(0).getChildNodes().item(0)
				.getNodeValue();
		} catch (NullPointerException e){
			explicacion = "";
		}
	}

	public String[] getRespuesta() {
		return new String[] { aceptado, explicacion };
	}
}
