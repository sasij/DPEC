package dpec.master;

/**
 * Clase que agrupa las funciones para la multiplicaci�n de matrices. Los m�todos son 
 * est�ticos de forma que no hay que instanciar la clase para hacer las llamadas
 * a los m�todos
 * 
 * @author Javi
 *
 */
public class MultiplicarMatrices {


	/**
	 * Funci�n est�tica que inicializa una matriz con n�meros enteros entre 1 y 25
	 * @param  size entero con el tama�o de la matriz que ser� cuadrada
	 * @return matriz inicializada con valores aleatorios
	 */
	public static int[][] inicializarMatriz(int SIZE){

		// crear la matriz e iniciarla
		int M[][] = new int [SIZE][SIZE];
		
		Long T = System.currentTimeMillis();
		for(int i = 0; i<SIZE; i++)
			for(int j = 0; j<SIZE; j++)
				M[i][j] = 0;	
		
		// devolver el resultado indicando el tiempo tardado
		System.out.println("Tiempo en asignaci�n sin permutar: " + Long.toString(System.currentTimeMillis() - T));
		return M;	
	}
	
	/**
	 * Funci�n que se encarga de iniciar una matriz con los bucles permutados que
	 * deber�a de dar m�s fallos que el caso anterior
	 */
	public static int[][] iniciarMatrizPermutado(int SIZE) {
		
		// crear la matriz
		int M[][] = new int [SIZE][SIZE];
		
		Long T = System.currentTimeMillis();
		for(int j=0; j<SIZE; j++)
			for(int i=0; i<SIZE; i++)
				M[i][j] = 0;	
		
		// devolver el resultado indicando el tiempo tardado
		System.out.println("Tiempo en asignaci�n permutada: " + Long.toString(System.currentTimeMillis() - T));
		return M;
	}


	/**
	 * Funci�n encargada de devolver multiplicar dos matrices devolviendo el tiempo que
	 * se ha tardado en llevar a cabo la operaci�n. Se hace la multiplicaci�n con las
	 * matrices del derecho reduciendo los fallos de cach�
	 * @param a es la primera de las matrices para el producto
	 * @param b es la segunda de las matrices para el producto
	 * @return string con que se ha tardado en multiplicar en milisegundos
	 */
	public static String producto (int A[][], int B[][]){  

		// tomar el tiempo inicial y crear la resultante
		int result[][] = inicializarMatriz(A.length);
		Long T1 = System.currentTimeMillis();

		// ejecutar la multiplicaci�n
		for(int i = 0; i < A.length; i++)
			for(int j = 0; j < B.length; j++)
				for(int k = 0; k < B.length; k++) 
					result[i][j] += A[i][k] * B[k][j];  

		// creaci�n de la segunda multiplicaci�n
		return Long.toString((System.currentTimeMillis() - T1));  
	}


	/**
	 * Funci�n de multiplicaci�n con los bucles permutados. Es exactamente la misma 
	 * operaci�n que el m�todo de producto pero permuta los bucles para producir menos
	 * fallos de cach� lo que debe llevar a un mayor tiempo de ejecuci�n
	 * @param a es la primera de las matrices para el producto
	 * @param b es la segunda de las matrices para el producto
	 * @return string con que se ha tardado en multiplicar en milisegundos
	 */
	public static String productoPermutado (int A[][], int B[][]) {  

		// tomar el tiempo inicial y crear la resultante
		int result[][] = inicializarMatriz(A.length);
		Long T1 = System.currentTimeMillis();

		// ejecutar la multiplicaci�n
		for(int i = 0; i < A.length; i++)
			for(int k = 0; k < B.length; k++) 
				for(int j = 0; j < B.length; j++)	
					result[i][j] += A[i][k] * B[k][j];  

		// creaci�n de la segunda multiplicaci�n
		return Long.toString((System.currentTimeMillis() - T1));  
	}
	
	
	/**
	 * Funci�n que realiza la multiplicaci�n teniendo en cuenta la optimizaci�n de tiling.
	 * Dado que se asume un tama�o de bloque de 64B y el procesador es de 32 bits, se 
	 * estiman 16 palabras por bloque, esto es un tama�o de tile de 16
	 * @param a es la primera de las matrices para el producto
	 * @param b es la segunda de las matrices para el producto
	 * @return string con que se ha tardado en multiplicar en milisegundos
	 */
	public static String productoTiling (int A[][], int B[][]) {  

		// tomar el tiempo inicial y crear la resultante		
		int result[][] = inicializarMatriz(A.length);
		Long T1 = System.currentTimeMillis();
		
		// fijar el tama�o de tile y ejecutar la multiplicaci�n con tiling
		int TILE = 16;
		for (int i = 0; i < A.length; i += TILE)
		    for (int j = 0; j < B.length; j += TILE)
		        for (int k = 0; k < B.length; k += TILE)
		            for (int ii = i; ii < i + TILE; ii++)
		                for (int jj = j; jj < j + TILE; jj++) 
		                    for (int kk = k; kk < k + TILE; kk++)
		                    	result[ii][jj] += A[ii][kk] * B[kk][jj];
	
		// creaci�n de la segunda multiplicaci�n
		return Long.toString((System.currentTimeMillis() - T1));  
	}
	

	/**
	 * Funci�n que seg�n el tama�o de las matrices intenta hacer un unrolling en la
	 * operaci�n de multiplicaci�n de hasta un m�ximo de 32 unrolls.
	 */
	public static String productoUnrolling (int A[][], int B[][]) {

		// tomar el tiempo inicial y crear la resultante		
		int result[][] = new int[A.length][B.length];
		
		
		// primero almacenar el tama�o
				int N = A.length;
		

		


		if (N%16 == 0) {
			Long T1 = System.currentTimeMillis();
			for(int i = 0; i < N; i++)
				for(int j = 0; j < N; j++) {
					result[i][j] = 0;
					for(int k = 0; k < N; k+=16) {
						result[i][j] += A[i][k] * B[k][j];
						result[i][j] += A[i][k+1] * B[k+1][j];
						result[i][j] += A[i][k+2] * B[k+2][j];
						result[i][j] += A[i][k+3] * B[k+3][j];
						result[i][j] += A[i][k+4] * B[k+4][j];
						result[i][j] += A[i][k+5] * B[k+5][j];
						result[i][j] += A[i][k+6] * B[k+6][j];
						result[i][j] += A[i][k+7] * B[k+7][j];
						result[i][j] += A[i][k+8] * B[k+8][j];
						result[i][j] += A[i][k+9] * B[k+9][j];
						result[i][j] += A[i][k+10] * B[k+10][j];
						result[i][j] += A[i][k+11] * B[k+11][j];
						result[i][j] += A[i][k+12] * B[k+12][j];
						result[i][j] += A[i][k+13] * B[k+13][j];
						result[i][j] += A[i][k+14] * B[k+14][j];
						result[i][j] += A[i][k+15] * B[k+15][j];
					}

				}
			// creaci�n de la segunda multiplicaci�n
			return Long.toString((System.currentTimeMillis() - T1));
			  
		}

		else return "NACK";
	}
}
