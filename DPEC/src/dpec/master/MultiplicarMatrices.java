package dpec.master;

public class MultiplicarMatrices {
	
	/**
	 * Función encargada de devolver el tiempo que tarda la multiplicacion de dos matrices
	 * @param A int[][]
	 * @param B int [][]
	 * @return String con el tiempo que ha tardado el sistema
	 */
	public static String producto(int A[][], int B[][]){  
		int suma = 0;  
		int result[][] = new int[A.length][B.length];
		
		//Creamos la primera marca de tiempo
		Long T1 = System.currentTimeMillis();
		
		for(int i = 0; i < A.length; i++){  
			for(int j = 0; j < B.length; j++){  
				suma = 0;  
				for(int k = 0; k < B.length; k++){  
					result[i][j] += A[i][k] * B[k][j];  
				}				  
			}  
		}
		//Creamos la segunda marca de tiempo
		Long T2 = System.currentTimeMillis();		
		Long T = (Long)(T2 - T1);
		
		return Long.toString(T);  
	}
	
	/**
	 * Función encargada de devolver el tiempo que tarda la multiplicacion de dos matrices con permutacion de bucles
	 * @param A int[][]
	 * @param B int [][]
	 * @return String con el tiempo que ha tardado el sistema
	 */
	public static String productoPermutado(int A[][], int B[][]){  
		int suma = 0;  
		int result[][] = new int[A.length][B.length];
		
		//Creamos la primera marca de tiempo
		Long T1 = System.currentTimeMillis();
		
		for(int j = 0; j < B.length; j++){  
			for(int i = 0; i < A.length; i++){  
				suma = 0;  
				for(int k = 0; k < B.length; k++){  
					result[i][j] += A[i][k] * B[k][j];  
				}				 
			}  
		}
		//Creamos la segunda marca de tiempo
		Long T2 = System.currentTimeMillis();		
		Long T = (Long)(T2 - T1);
		
		return Long.toString(T);  
	}
	
	/**
	 * Función que inicializa una matriz con enteros entre 1 y 25
	 * @param SIZE int con el tamaño de la matriz
	 * @return matriz de SIZE*SIZE
	 */
	public static int[][] inicializarMatriz(int SIZE){
		
		int M[][] = new int [SIZE][SIZE];
		
		for(int i = 0; i<SIZE; i++){
			for(int j = 0; j<SIZE; j++){
				M[i][j] = (int) (Math.random() * 25 + 1);				
			}
		}
		
		return M;	
	}	
	

}//Juanjos''s
