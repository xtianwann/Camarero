package prg.pi.restaurantecamarero.preferencias;

import android.app.Activity;
import android.os.Bundle;

/**
 * Actividad encargada de lanzar el fragment de preferencias de la aplicaci�n.
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class PreferenciasSet extends Activity {

 @Override
 protected void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 getFragmentManager().beginTransaction().replace(android.R.id.content,new Preferencias()).commit();
 }

}
