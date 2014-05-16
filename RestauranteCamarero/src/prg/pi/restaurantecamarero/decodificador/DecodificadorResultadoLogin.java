package prg.pi.restaurantecamarero.decodificador;

import org.w3c.dom.Document;

public class DecodificadorResultadoLogin {
	
	private Document dom;
	private String resultado;
	
	public DecodificadorResultadoLogin(Document dom){
		this.dom = dom;
		extraerResultado();
	}
	
	public String getResultado(){
		return resultado;
	}
	
	private void extraerResultado(){
		resultado = dom.getElementsByTagName("resultado").item(0).getFirstChild().getNodeValue();
	}

}
