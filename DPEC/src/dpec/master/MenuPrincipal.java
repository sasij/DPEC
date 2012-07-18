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

	// declaración de los tiempos optimizado y no optimizado
	String tiempoSinOptimizar;
	String tiempoOptimizado;


	/**
	 * Función Android que se encarga ejecuta en la creación de la actividad creando el layout
	 * inicial e iniciando la lista del menú principal y las preferencias de la aplicación
	 * @param Bundle instancia salvada que se pasa por defecto
	 * @return none
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_principal);

		Typeface type = Typeface.createFromAsset(getAssets(),"fonts/debussy.ttf");		   
		TextView nombreActivity = (TextView)this.findViewById(R.id.menu_principal_titulo);
		nombreActivity.setTypeface(type);

		// Se define la acción asociada al botón de preferencias
		Button btnPreferencias = (Button)this.findViewById(R.id.menu_principal_preferencias);
		btnPreferencias.setOnClickListener(new View.OnClickListener(){
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


	/**
	 * Inicializacion del conjunto de elementos que formarán la lista del menu principal
	 * @param none
	 * @return none
	 */
	private void inicializarElementosLista(){

		try {
			// fijar los elementos para la lista del menú principal
			ElementoLista o1 = new ElementoLista();
			o1.setName("Permutación de bucles");
			o1.setImage(R.drawable.matrix);
			ElementoLista o2 = new ElementoLista();
			o2.setName("Loop Tiling");
			o2.setImage(R.drawable.visa);
			ElementoLista o3 = new ElementoLista();
			o3.setName("Loop unrolling");
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
	 * se ha fijado la lista y asocia los listener para llevar a cabo cada una de las
	 * acciones asociadas a las opciones
	 * @param none
	 * @return none
	 */
	public void reload() {
		AdaptadorLista adaptador = new AdaptadorLista(this);
		browsingList = (ListView)findViewById(R.id.BrowsingList);
		browsingList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				// acciones necesarias al hacer click
				realizarAccion(position);            	               
			}
		});
		browsingList.setAdapter(adaptador);
	}


	/**
	 * Define el adaptador de array para crear la lista de opciones. Infla cada uno de los 
	 * elementos con el contenido correspondiente.
	 * 
	 * @author javi / juanjo
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


	/**
	 * Función que lleva a cabo la acción indicada por una posición de elemento
	 * dentro del menú de opciones
	 * @param position posición del menú que se ha seleccionado
	 * @return none
	 */
	private void realizarAccion(int position){
		SegundoPlano cc = new SegundoPlano(MenuPrincipal.this);
		switch (position) {
		case 0:  
			System.out.println("Se ha seleccionado 'Permutación de Bucles'");         		
			cc.execute(new Integer[] {0}); 
			break;
		case 1:
			System.out.println("Se ha seleccionado 'Loop Tiling'");  
			cc.execute(new Integer[] {1}); 
			break;
		case 2:        
			System.out.println("Se ha seleccionado 'Loop Unrolling'");  
			cc.execute(new Integer[] {2}); 
			break;
		case 3:         		
			Intent intent = new Intent(MenuPrincipal.this, MenuPrincipal.class);	 
			Bundle bundle = new Bundle();              	
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}


	/**
	 * Clase que define una tarea en segundo plano que se encarga de realizar las
	 * operaciones para la opción que se ha seleccionado en cada caso. Tiene métodos
	 * para indicar el trabajo a hacer antes, durante y después de su ejecución real
	 */
	private class SegundoPlano extends AsyncTask<Integer, Void, Integer> {    	

		ProgressDialog dialog;
		WeakReference<MenuPrincipal> context;

		// obtener el contexto de la tarea al contruirla
		public SegundoPlano(MenuPrincipal activity) {
			context = new WeakReference<MenuPrincipal>(activity);
		}

		// fijar el mensaje de ejecución
		protected void onPreExecute() {
			dialog = new ProgressDialog(MenuPrincipal.this);
			dialog.setMessage("Realizando operaciones...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.show();
		}

		// indicar el trabajo a realizar en segundo plano
		protected Integer doInBackground(Integer... datos) {        	

			switch (datos[0]){

			// caso de haber seleccionado la permutación de bucles
			case 0:
				return permutacionBucles(); 

				// caso de selección de la opción de tiling
			case 1:
				return loopTiling();        	
			case 2:
				return loopUnrolling();
			}
			return 0;
		}

		// una vez terminada la ejecución de la tarea en segundo plano llama a los resultados
		protected void onPostExecute(Integer result) {

			// obtener el contexto de la actividad y ver si está finalizando
			MenuPrincipal activity = context.get();
			if (activity != null && !activity.isFinishing()) {

				// caso de que no se ha producido ningún error: se llama a resultados
				if(result == 1){        			        			
					dialog.dismiss();
					Intent intent = new Intent(MenuPrincipal.this, Resultados.class);	 
					Bundle bundle = new Bundle();
					bundle.putString("tiempoSinOptimizar", tiempoSinOptimizar);
					bundle.putString("tiempoOptimizado", tiempoOptimizado);
					intent.putExtras(bundle);
					startActivity(intent);

				}
				// se muestra un mensaje en base a los diferentes errores que se puedan producir
				else if (result == 2){        			
					Util.showSimpleToast(getBaseContext(), "Error al realizar la operacion");        			
					dialog.dismiss();
				}
				else if (result == 3){        			
					Util.showSimpleToast(getBaseContext(), "Debe de indicar un tamaño a la matriz");        			
					dialog.dismiss();
				}
				else if (result == 4){
					Util.showSimpleToast(getBaseContext(), "El tamaño de matriz debe ser múltiplo de 16 para optimizar con tiling");        			
					dialog.dismiss();
				}        		
			} else{
				Util.showSimpleToast(getBaseContext(), "Fucking Error!!");            	
				dialog.dismiss();
			}
		}
	}


	/**
	 * Función que realiza la multiplicación simple de dos matrices obteniendo los tiempos
	 * luego repite la función pero realizando una optimización mediante la permutación
	 * de bucles, lo que debería dar mejor resultado
	 * @param no tiene parámetros, las matrices se reservan en ejecución
	 * @return 1 si todo marcha correctamente o 3 en caso de que no se haya indicado
	 * un tamaño para las matrices
	 */
	public int permutacionBucles() {

		// obtener el tamaño de las matrices de las preferencias
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String size = sharedPrefs.getString("matrixSize", "0");    	

		// si el tamaño no es correcto devuelve el error correspondiente
		if(size.equals("0") || size.equals("") || size == null)
			return 3;

		// reserva la memoria e inicia dos matrices del tamaño indicado
		int M1[][] = MultiplicarMatrices.inicializarMatriz(Integer.parseInt(size));
		int M2[][] = MultiplicarMatrices.inicializarMatriz(Integer.parseInt(size));

		// lleva a cabo las dos multiplicaciones e imprime los resultados
		tiempoSinOptimizar = MultiplicarMatrices.producto(M1, M2);
		System.out.println("El tiempo sin optimizar es::" + tiempoSinOptimizar);
		tiempoOptimizado = MultiplicarMatrices.productoPermutado(M1, M2);
		System.out.println("El tiempo optimizado con permutación de bucles es:" + tiempoOptimizado);

		// devolver que la operación es correcta
		return 1;
	}
	

	/**
	 * Función que realiza la multiplciación simple de dos matrices obteniendo los tiempos. 
	 * Luego repite la multiplicación pero llevando a cabo una optimización de tiling con el
	 * tamaño de tile de 16 indicado en la función. Previamente comprueba que los tamaños sean
	 * los correctos
	 * @param no tiene parámetros, las matrices se reservan en ejecución
	 * @return 1 si todo va correctamente, 3 en caso de error por no haber indicado el tamaño
	 * de matriz en las preferencias o 4 en caso de que el tamaño no sea el tamaño de tile
	 */
	public int loopTiling() {

		// obtener el tamaño de las matrices de las preferencias
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String size = sharedPrefs.getString("matrixSize", "0");    	

		// si el tamaño no es correcto devuelve el error correspondiente
		if(size.equals("0") || size.equals("") || size == null)
			return 3;
		
		// si el tamaño no es multiplo del tamaño de tile (16) devuelve error
		if (Integer.parseInt(size)%16 != 0) 
			return 4;

		// reserva la memoria e inicia dos matrices del tamaño indicado
		int M1[][] = MultiplicarMatrices.inicializarMatriz(Integer.parseInt(size));
		int M2[][] = MultiplicarMatrices.inicializarMatriz(Integer.parseInt(size));

		// lleva a cabo las dos multiplicaciones e imprime los resultados
		tiempoSinOptimizar = MultiplicarMatrices.producto(M1, M2);
		System.out.println("El tiempo sin optimizar es::" + tiempoSinOptimizar);
		tiempoOptimizado = MultiplicarMatrices.productoTiling(M1, M2);
		System.out.println("El tiempo optimizado con tiling es:" + tiempoOptimizado);

		// devolver que la operación es correcta
		return 1;
	}
	
	
	
	
	
	public int loopUnrolling() {

		// obtener el tamaño de las matrices de las preferencias
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String size = sharedPrefs.getString("matrixSize", "0");    	

		// si el tamaño no es correcto devuelve el error correspondiente
		if(size.equals("0") || size.equals("") || size == null)
			return 3;

		// reserva la memoria e inicia dos matrices del tamaño indicado
		int M1[][] = MultiplicarMatrices.inicializarMatriz(Integer.parseInt(size));
		int M2[][] = MultiplicarMatrices.inicializarMatriz(Integer.parseInt(size));

		// lleva a cabo las dos multiplicaciones e imprime los resultados
		tiempoSinOptimizar = MultiplicarMatrices.producto(M1, M2);
		System.out.println("El tiempo sin optimizar es::" + tiempoSinOptimizar);
		tiempoOptimizado = MultiplicarMatrices.productoUnrolling(M1, M2);
		System.out.println("El tiempo optimizado con unrolling es:" + tiempoOptimizado);

		// devolver que la operación es correcta
		return 1;
	}
}
