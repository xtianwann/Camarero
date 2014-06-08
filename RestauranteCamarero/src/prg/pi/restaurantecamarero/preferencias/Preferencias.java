package prg.pi.restaurantecamarero.preferencias;

import prg.pi.restaurantecamarero.R;
import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Fragment encargado de mostrar las preferencias de la aplicaci�n.
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class Preferencias extends PreferenceFragment{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferencias);
	}
}
