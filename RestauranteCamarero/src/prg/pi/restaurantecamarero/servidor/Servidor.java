package prg.pi.restaurantecamarero.servidor;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import prg.pi.restaurantecamarero.MainFragments;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


/**
 * Arranca el servidor
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class Servidor extends Service{
    
    private HiloPrincipal hiloPrincipal;
    private MainFragments principal;
    
    /**
     * Constructor: lanza el hilo principal y guarda una instancia de la actividad principal
     * 
     * @param principal [MainFragments] instancia de la actividad principal
     */
    public Servidor(MainFragments principal){
    	this.principal = principal;
        try {
            hiloPrincipal = new HiloPrincipal(27000);
            hiloPrincipal.start();
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Obtiene la instancia de hilo principal
     * 
     * @return [HiloPrincipal] instancia del hilo principal
     */
    public HiloPrincipal getHiloPrincipal(){
    	return hiloPrincipal;
    }
    
    /**
	 *  Hilo principal del servidor
	 *  
	 *  @author Juan G. P�rez Leo
	 *  @author Cristian Mar�n Honor
	 */
    public class HiloPrincipal extends Thread{
        
        private ServerSocket socketServidor;
        private Dispatcher dispatcher;
        private boolean parado;
        
        /**
         * Constructor de HiloPrincipal
         * 
         * @param puerto [int] puerto de escucha
         * @throws IOException excepci�n lanzada en caso de no poder crear el ServerSocket en
         * el puerto pasado por par�metro
         */
        public HiloPrincipal(int puerto) throws IOException{
            this.socketServidor = new ServerSocket(puerto);
            this.parado = true;
        }
        
        public void run(){
            Socket cliente;
            this.parado = false;
            dispatcher = new Dispatcher(this,principal);
            dispatcher.start();
            
            while(!parado){
                try {
                    cliente = this.socketServidor.accept();
                    dispatcher.addSocket(cliente);
                } catch (IOException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        /**
         * Para la ejecuci�n del hilo
         */
        public void parar(){
            parado = true;
            dispatcher.setParado(true);
            try {
                socketServidor.close();
            } catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
    
}