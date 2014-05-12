package prg.pi.restaurantecamarero.preferencias;

import prg.pi.restaurantecamarero.R;
import prg.pi.restaurantecamarero.R.xml;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;

public class Preferencias extends PreferenceFragment{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferencias);
	    final ListPreference listaUsuarios = (ListPreference) findPreference("listaUsuarios");
	 // THIS IS REQUIRED IF YOU DON'T HAVE 'entries' and 'entryValues' in your XML
	    setListPreferenceData(listaUsuarios, new String[]{"------Selecciona un usuario------","Pepe"}, 
	    		new String[]{"Pepe","------Selecciona un usuario------"}, 
	    		"Pepe");

	    listaUsuarios.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	        public boolean onPreferenceClick(Preference preference) {

	            return false;
	        }
	    });    
	}
	public static void setListPreferenceData(ListPreference lista,String usuarios[],String idUsuarios[],String usuarioDefecto) {
	    lista.setEntries(usuarios);
	    lista.setEntryValues(idUsuarios);
	    lista.setDefaultValue("Pepe");
	}
}
