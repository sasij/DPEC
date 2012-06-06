package dpec.master;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import Util.Util;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MenuPrincipal extends Activity{
	
	private ArrayList<ElementoLista> el;
	private ListView browsingList;
	
	String tiempoSinOptimizar;
	String tiempoOptimizado;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_principal);
		
		Typeface type = Typeface.createFromAsset(getAssets(),"fonts/debussy.ttf");		   
		TextView nombreActivity = (TextView)this.findViewById(R.id.menu_principal_titulo);
		nombreActivity.setTypeface(type);
		
		// Se define la acción asociada al botón de preferencias
		Button btnPreferencias = (Button)this.findViewById(R.id.menu_principal_preferencias);
		btnPreferencias.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent = new Intent(MenuPrincipal.this, Preferencias.class);	 
				Bundle bundle = new Bundle();
				intent.putExtras(bundle);
				startActivity(intent);                        		
			}
		});
		
		el = new ArrayList<ElementoLista>();        
        inicializarElementosLista();
        reload();
	}
	
	/*
	 * Inicializacion del conjunto de elementos que formarán la lista del menu principal
	 */
	private void inicializarElementosLista(){

		try {
			ElementoLista o1 = new ElementoLista();
			o1.setName("Multiplicar matrices");
			o1.setImage(R.drawable.matrix);
			ElementoLista o2 = new ElementoLista();
			o2.setName("Ordenación quicksort");
			o2.setImage(R.drawable.visa);
			ElementoLista o3 = new ElementoLista();
			o3.setName("Procesado de imagenes");
			o3.setImage(R.drawable.gas);
			ElementoLista o4 = new ElementoLista();
			o4.setName("About");
			o4.setImage(R.drawable.about);
			
			el.add(o1);
			el.add(o2);
			el.add(o3);
			el.add(o4);			        
			
		} catch (Exception e) {
			Log.e("BACKGROUND_PROC", e.getMessage());
		}     	

	}
    /**
     * Recarga el contenido de la actividad tomando de nuevo el layout. Se utiliza una vez
     * se han descargado los cupones. Si no se hace así, al llamar por segunda vez, puede
     * dar errores
     */
    public void reload() {
    	AdaptadorLista adaptador = new AdaptadorLista(this);
        browsingList = (ListView)findViewById(R.id.BrowsingList);
        
        browsingList.setOnItemClickListener(new OnItemClickListener() {
        	@Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                //Acciones necesarias al hacer click
            	realizarAccion(position);            	               
            }
        });
        browsingList.setAdapter(adaptador);
    }

	
    /**
     * Define el adaptador de array para crear la lista de cupones. Infla cada uno de los 
     * elementos con el contenido correspondiente. Adem‡s de ello inicializa el vector de
     * referencias a Checkboxes para comprobar los marcados cuando se pulsa la opci—n de descarga
     * 
     * @author javi / Juanjo
     *
     */
	@SuppressWarnings("rawtypes")
	class AdaptadorLista extends ArrayAdapter {
    	
    	Activity context;
    	
    	@SuppressWarnings("unchecked")
		AdaptadorLista(Activity context) {
    		super(context, R.layout.icon_row, el);
    		this.context = context;
    	}
    	
    	@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(R.layout.icon_row, null);
			
			ImageView icon = (ImageView)item.findViewById(R.id.icon);			
			icon.setImageResource(el.get(position).getImage());
			
			TextView lblTitulo = (TextView)item.findViewById(R.id.text);
			lblTitulo.setText(el.get(position).getName());
			
			return(item);
		}
    }
	
	private int realizarAccion(int position){
		SegundoPlano cc = new SegundoPlano(MenuPrincipal.this);
		switch (position) {
         	case 0:  
         		System.out.println("Multiplicacion");         		
    	        cc.execute(new Integer[] {0}); 
         		break;
         	case 1:
         		cc.execute(new Integer[] {1}); 
         		break;
         	case 2:         		
    	        cc.execute(new Integer[] {2}); 
         		break;
         	case 3:         		
         		Intent intent = new Intent(MenuPrincipal.this, MenuPrincipal.class);	 
                Bundle bundle = new Bundle();              	
                intent.putExtras(bundle);
                startActivity(intent);
		 }
		
		
		return 1;
	}
	
	/**
     * Clase encargada de realizar la sincronizacion
     */
    private class SegundoPlano extends AsyncTask<Integer, Void, Integer> {    	
    	
    	ProgressDialog dialog;
    	WeakReference<MenuPrincipal> context;

        public SegundoPlano(MenuPrincipal activity) {
            context = new WeakReference<MenuPrincipal>(activity);
        }
    	@Override
    	protected void onPreExecute() {
    		dialog = new ProgressDialog(MenuPrincipal.this);
    		dialog.setMessage("Realizando operaciones...");
    		dialog.setIndeterminate(true);
    		dialog.setCancelable(false);
    		dialog.show();
    	 }
        @Override
        protected Integer doInBackground(Integer... datos) {        	
        	
        	switch (datos[0]){
        		case 0:
        			return obtenerTiempoMatriz();        		
        		case 1:
        			//return funcion para ordenar
        			break;
        		
        		case 2:
        			//return funcion para procesar imagenes 
        			break;
        	}        	
        	return 0;
        }
        @Override
        protected void onPostExecute(Integer result){
        	
        	//dialog.dismiss();
        	MenuPrincipal activity = context.get();
            if (activity != null && !activity.isFinishing()) {
            	
            	//No se ha producido ningún error y se llama a la Activity de resultados
        		if(result == 1){        			        			
        			dialog.dismiss();
        			
        			Intent intent = new Intent(MenuPrincipal.this, Resultados.class);	 
                    Bundle bundle = new Bundle();
                    bundle.putString("tiempoSinOptimizar", tiempoSinOptimizar);
                    bundle.putString("tiempoOptimizado", tiempoOptimizado);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    
        		}
        		//Se muestra un mensaje en base a los diferentes errores que se puedan producir
        		else if (result == 2){        			
        			Util.showSimpleToast(getBaseContext(), "Error al realizar la operacion");        			
        			dialog.dismiss();
        		}
        		else if (result == 3){        			
        			Util.showSimpleToast(getBaseContext(), "Debe de indicar un tamaño a la matriz");        			
        			dialog.dismiss();
        		}
        		else{
        			Util.showSimpleToast(getBaseContext(), "Hubo otro error");        			
        			dialog.dismiss();
        		}        		
            }else{
            	Util.showSimpleToast(getBaseContext(), "Fucking Error!!");            	
            	dialog.dismiss();
            }
        }
    }//fin de la clase secundaria
    
    /**
     * Función encargada de inicializar y obtener el tiempo del benchmark
     * @return
     */
    public int obtenerTiempoMatriz(){
    	SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    	String size = sharedPrefs.getString("matrixSize", "0");    	
    	
    	if(size.equals("0") || size.equals("") || size == null){
    		return 3;
    	}            	
		
    	int M1[][] = MultiplicarMatrices.inicializarMatriz(Integer.parseInt(size));
    	int M2[][] = MultiplicarMatrices.inicializarMatriz(Integer.parseInt(size));
    	
    	tiempoSinOptimizar = MultiplicarMatrices.producto(M1, M2);
    	tiempoOptimizado = MultiplicarMatrices.productoPermutado(M1, M2);
    	System.out.println("El tiempo sin optimizar es::" + tiempoSinOptimizar);
    	System.out.println("El tiempo optimizado es::" + tiempoOptimizado);
    	
    	return 1;
    }
    
}//Juanjo''ss
