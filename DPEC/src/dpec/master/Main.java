package dpec.master;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

public class Main extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		TareaSegundoPlano tareaSegundoPlano = new TareaSegundoPlano(this);
		tareaSegundoPlano.execute(new String[] { "" });
	}

	@Override
	public void onRestart(){
		super.onRestart();    

		TareaSegundoPlano tareaSegundoPlano = new TareaSegundoPlano(this);
		tareaSegundoPlano.execute(new String[] { "" });
	}

	/**
	 * Permite realizar tareas en segundo plano de forma más sencilla que usando Threads
	 * Param: Vector de datos que recibe/Vector de datos que usa la funcion/Vector de datos que devuelve
	 */
	private class TareaSegundoPlano extends AsyncTask<String, Void, Void> {

		int progress = 0;
		//ProgressDialog dialog;
		WeakReference<Main> context;

		public TareaSegundoPlano(Main activity) {
			context = new WeakReference<Main>(activity);
		}

		@Override
		protected void onPreExecute() { progress = 0; }

		@Override
		protected Void doInBackground(String... urls) {
			while(!isCancelled() && progress < 5){
				progress++;
				Log.d("SALIDA", "" + progress);
				SystemClock.sleep(100);        		    
			}
			return null;        
		}

		@Override
		protected void onPostExecute(Void result) {

			Main activity = context.get();
			if (activity != null && !activity.isFinishing()) {
				
				// Activo el menú principal pasando el array list de contenedores
				Intent intent = new Intent(Main.this, MenuPrincipal.class);            			
				Bundle bundle = new Bundle();				
				intent.putExtras(bundle);
				startActivity(intent);

			}        	
		}
	}
}//Juanjos''s