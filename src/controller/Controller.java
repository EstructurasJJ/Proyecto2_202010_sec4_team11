package controller;

import java.util.ArrayList;
import java.util.Scanner;

import model.data_structures.Node;
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

			int nHeap;
			String lista;

			int capIni;
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
				System.out.println("\t Arbol Rojo-Negro: " + "NO LA SABEMOS HACER :/ " + "\n---------");
				

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
