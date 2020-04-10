package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import model.logic.Comparendo;
import model.logic.Modelo;
import view.View;

public class Controller {

	private Modelo modelo;
	private View view;
	public final static String RUTAGEOJASON = "./data/Comparendos_DEI_2018_Bogotá_D.C.geojson";
	public final static String JUEGUEMOS = "./data/Comparendos_DEI_2018_Bogotá_D.C_small.geojson";
	public final static String COTEJO = "./data/Comparendos_DEI_2018_Bogotá_D.C_50000_.geojson";
	

	public Controller ()
	{
		view = new View();
		modelo = new Modelo();
	}

	public void run() 
	{
		Scanner lector = new Scanner(System.in);
		boolean fin = false;
		String dato = "";
		String respuesta = "";

		Comparendo[] arreglo = null;

		while( !fin )
		{
			view.printMenu();

			int option = lector.nextInt();

			switch(option)
			{
			case 1:

				//Cargar el archivo

				modelo.leerGeoJson(COTEJO);

				view.printMessage("Archivo GeoJSon Cargado");
				view.printMessage("Numero total de comparendos en: ");
				
				System.out.println("\t MaxColaCP: " + modelo.darMaxCola().darSize());
				System.out.println("\t MaxHeapCP: " + modelo.darMaxHeapCP().darTamaño());
				System.out.println("\t TablaHashEncSeparando: " + modelo.darHashEncadenado().darNumElementos());
				System.out.println("\t TablaHashSondeoLineal: " + modelo.darHashLineal().darDatos());
				System.out.println("\t Arbol Rojo-Negro (Double)" + modelo.darArbolJuanjo().size());				
				System.out.println("\t Arbol Rojo-Negro (Date): " + modelo.darArbolBobi().size()  + "\n---------");
				
				break;
				
			case 2:
				 
				System.out.println("Por favor ingrese la cantidad 'm' a buscar.");
				int m = Integer.parseInt(lector.next());
				System.out.println("---------");
				
				ArrayList<Comparendo> listica = modelo.darMComparendosGravedad(m);
				
				for(int i = 0; i < listica.size(); i++)
				{
					Comparendo compi = listica.get(i);
					
					System.out.println(compi.darObjectid());
					System.out.println(compi.darFecha_Hora());
					System.out.println(compi.darInfraccion());
					System.out.println(compi.darClase_Vehi());
					System.out.println(compi.darTipo_Servicio());
					System.out.println(compi.darLocalidad() + "\n---------");
				}

				break;
				
			case 3:
				 
				System.out.println("Por favor ingrese el numero del mes (1-12).");
				String mes = lector.next();
				System.out.println("Por favor ingrese el día de la semana (L, M, I, J, V, S, D).");
				String dia = lector.next();
				System.out.println("---------");
				
				Iterator<Comparendo> lista = modelo.buscarPorMesYDia(mes, dia);
				
				while(lista.hasNext())
				{
					Comparendo compi = lista.next();
					
					System.out.println(compi.darObjectid());
					System.out.println(compi.darFecha_Hora());
					System.out.println(compi.darInfraccion());
					System.out.println(compi.darClase_Vehi());
					System.out.println(compi.darTipo_Servicio());
					System.out.println(compi.darLocalidad() + "\n---------");
				}
			
			break;
			
			case 4:
				 
				System.out.println("Ingrese la fecha hora limite_bajo (“YYYY/MM/DD-HH:MM:ss”).");
				String fechaMin = lector.next();
				System.out.println("---------");
				
				System.out.println("Ingrese la fecha hora limite_alto (“YYYY/MM/DD-HH:MM:ss”).");
				String fechaMax = lector.next();
				System.out.println("---------");
				
				String a="";	
				System.out.println("Por favor ingrese la localidad del comparendo a buscar. Si son palabras separadas, por favor escriba en una línea diferente cada una");

				a =lector.next();

				if (a.equals("SANTA") || a.equals("BARRIOS") || a.equals("CIUDAD")|| a.equals("SAN") || a.equals("BOGOTA")||a.equals("RAFAEL")||a.equals("PUENTE")||a.equals("ANTONIO"))
				{
					a=a+ " "+lector.next();
				}
				
				listica = modelo.buscarFechaHoraLocalidad(fechaMin, fechaMax, a);
				
				for(int i = 0; i < listica.size(); i++)
				{
					Comparendo compi = listica.get(i);
					
					System.out.println(compi.darObjectid());
					System.out.println(compi.darFecha_Hora());
					System.out.println(compi.darInfraccion());
					System.out.println(compi.darClase_Vehi());
					System.out.println(compi.darTipo_Servicio());
					System.out.println(compi.darLocalidad() + "\n---------");
				}

				break;

			case 6:

				view.printMessage("--------- \n Hasta pronto !! \n---------"); 
				lector.close();
				fin = true;

				break;	

			default: 

				view.printMessage("--------- \n Opción Invalida !! \n---------");

				break;

			}
		}

	}	
}
