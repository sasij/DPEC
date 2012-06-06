package dpec.master;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import dpec.master.chart.PieChartView;

/**
 * Clase encargada de pintar en pantalla los resultados obtenidos después de ejecutar el test
 * @author Juanjo
 *
 */
public class Resultados extends Activity{


	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resultados);
		
		String tiempoSinOptimizar = getIntent().getStringExtra("tiempoSinOptimizar");
		String tiempoOptimizado = getIntent().getStringExtra("tiempoOptimizado");
		
        PieChartView pcv = (PieChartView)findViewById(R.id.grafico);
        pcv.setDataCount(2);
        pcv.setColor(new int[]{Color.RED,Color.BLUE});
        pcv.setData(new float[]{Float.parseFloat(tiempoOptimizado),Float.parseFloat(tiempoSinOptimizar)});
        //pcv.setBackgroundColor(Color.GREEN);
        //pcv.setSpecial(4);
        
        TextView tso = (TextView)findViewById(R.id.NoOptimizado);
        tso.setText(tiempoSinOptimizar + " ms.");
        
        TextView to = (TextView)findViewById(R.id.Optimizado);
        to.setText(tiempoOptimizado + " ms.");
        
        TextView tt = (TextView)findViewById(R.id.tiempoTotal);
        int t = Integer.parseInt(tiempoOptimizado) + Integer.parseInt(tiempoSinOptimizar);
        tt.setText( t + " ms.");
      

	}

}//Juanjo''s
