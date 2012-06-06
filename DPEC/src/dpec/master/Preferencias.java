package dpec.master;

import android.os.Bundle;
import android.preference.PreferenceActivity;
 
public class Preferencias extends PreferenceActivity {
     
    @Override
    public void onCreate(Bundle savedInstanceState) {       
        super.onCreate(savedInstanceState);       
        addPreferencesFromResource(R.xml.preferences);       
    }
}//~Juanjos''s