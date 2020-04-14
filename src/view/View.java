package view;

import model.logic.Modelo;

public class View 
{
	    /**
	     * Metodo constructor
	     */
	    public View()
	    {
	    	
	    }
	    
		public void printMenu()
		{
			System.out.println("1. Cargar datos.");
			System.out.println("2. Obtener los M comparendos con mayor gravedad.");
			System.out.println("3. Buscar los comparendos por mes y día de la semana.");
			System.out.println("4. Buscar los comparendos que tienen una fecha-hora en un rango y que son de una localidad dada.");
			System.out.println("5. Buscar los M comparendos más cercanos a la estación de policía del Campín");
			System.out.println("6. Buscar los comparendos por medio de deteccón, clase de vehículo, tipo de servicio y localidad");
			
			System.out.println("8. Visualizar Datos en una Tabla ASCII.");
			System.out.println("11. Exit");
			System.out.println("Dar el numero de opcion a resolver, luego oprimir tecla Return: (e.g., 1):");
		}

		public void printMessage(String mensaje) 
		{
			System.out.println(mensaje);
		}		
		
		public void printModelo(Modelo modelo)
		{
			
		}
}
