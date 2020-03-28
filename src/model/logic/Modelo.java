package model.logic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import model.data_structures.ListaEnlazadaQueue;
import model.data_structures.MaxColaCP;
import model.data_structures.MaxHeapCP;
import model.data_structures.Node;
import model.data_structures.TablaHashEncSeparado;
import model.data_structures.TablaHashSondeoLineal;


public class Modelo 
{
	private String parteDelComparendo; 
	private Comparendo compaAgregar;
	private boolean coordenadas = false;

	private double minLatitud = 1000000000;
	private double minLongitud = 1000000000;
	private double maxLatitud = -1000000000;
	private double maxLongitud = -1000000000;
	
	//////////////////////////
	///////ESTRUCTURAS////////
	//////////////////////////
	
	private TablaHashSondeoLineal<String, Comparendo> HSLBobi = new TablaHashSondeoLineal<String, Comparendo>(2);
	private TablaHashEncSeparado<String, Comparendo> HSCJuanjo = new TablaHashEncSeparado<String, Comparendo>(2);
	private MaxHeapCP<Comparendo> datosHeap  = new MaxHeapCP<Comparendo>();
	private MaxColaCP<Comparendo> datosCola = new MaxColaCP<Comparendo>();
	private ListaEnlazadaQueue<Comparendo> booty = new ListaEnlazadaQueue<Comparendo>();
	
	//////////////////////////
	//////////////////////////
	
	
	public Modelo()
	{
		parteDelComparendo = "";
	}

	public double darMinLatitud()
	{
		return minLatitud;
	}
	public double darMinLongitud()
	{
		return minLongitud;
	}
	public double darMaxLatitud()
	{
		return maxLatitud;
	}
	public double darMaxLongitud()
	{
		return maxLongitud;
	}

	public void leerGeoJson(String pRuta) 
	{	
		JsonParser parser = new JsonParser();
		FileReader fr = null;

		try 
		{
			fr = new FileReader(pRuta);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		JsonElement datos = parser.parse(fr);
		dumpJSONElement(datos);

	}

	private void dumpJSONElement(JsonElement elemento) 
	{


		if (elemento.isJsonObject()) 
		{

			JsonObject obj = elemento.getAsJsonObject();

			java.util.Set<java.util.Map.Entry<String,JsonElement>> entradas = obj.entrySet();
			java.util.Iterator<java.util.Map.Entry<String,JsonElement>> iter = entradas.iterator();

			while (iter.hasNext()) 
			{
				java.util.Map.Entry<String,JsonElement> entrada = iter.next();
				componentesDelComparendo(entrada.getKey());	            

				dumpJSONElement(entrada.getValue());
			}

		}
		else if (elemento.isJsonArray()) 
		{			
			JsonArray array = elemento.getAsJsonArray();
			java.util.Iterator<JsonElement> iter = array.iterator();

			while (iter.hasNext()) 
			{
				JsonElement entrada = iter.next();
				dumpJSONElement(entrada);
			}

		} 
		else if (elemento.isJsonPrimitive()) 
		{
			JsonPrimitive valor = elemento.getAsJsonPrimitive();

			if(compaAgregar == null)
			{
				compaAgregar = new Comparendo();
			}

			if(parteDelComparendo.equals("OBJECTID"))
			{
				compaAgregar.asignarObjectid(valor.getAsInt());
				//System.out.println(valor);
				parteDelComparendo = "";
			}
			else if (parteDelComparendo.equals("FECHA_HORA"))
			{
				compaAgregar.asignarFecha_Hora(valor.getAsString());
				//System.out.println(compaAgregar.darFecha_Hora().toString());
				parteDelComparendo = "";
			}
			else if (parteDelComparendo.equals("MEDIO_DETECCION"))
			{
				compaAgregar.asignarMedio_Dete(valor.getAsString());
				//System.out.println(valor);
				parteDelComparendo = "";
			}
			else if (parteDelComparendo.equals("CLASE_VEHICULO"))
			{
				compaAgregar.asignarClase_Vehi(valor.getAsString());
				//System.out.println(valor);
				parteDelComparendo = "";
			}
			else if (parteDelComparendo.equals("TIPO_SERVICIO"))
			{
				compaAgregar.asignarTipo_Servicio(valor.getAsString());
				//System.out.println(valor);
				parteDelComparendo = "";
			}
			else if (parteDelComparendo.equals("INFRACCION"))
			{
				compaAgregar.asignarInfraccion(valor.getAsString());
				//System.out.println(valor);
				parteDelComparendo = "";
			}
			else if (parteDelComparendo.equals("DES_INFRACCION"))
			{
				compaAgregar.asignarDes_Infrac(valor.getAsString());
				//System.out.println(valor);
				parteDelComparendo = "";

			}
			else if (parteDelComparendo.equals("LOCALIDAD"))
			{				
				compaAgregar.asignarLocalidad(valor.getAsString());
				//System.out.println(valor);	
				parteDelComparendo = "";
			}
			else if (parteDelComparendo.equals("MUNICIPIO"))
			{				
				compaAgregar.asignarMunicipio(valor.getAsString());
				//System.out.println(valor);	
				parteDelComparendo = "";
			}
			else if (parteDelComparendo.equals("coordinates"))
			{
				agregarCoordenada(valor.getAsDouble());				
			}

		} 
		else if (elemento.isJsonNull()) 
		{
			System.out.println("Es NULL");
		} 
		else 
		{
			System.out.println("Es otra cosa");
		}

	}

	public void componentesDelComparendo(String palabra)
	{
		if (palabra.equals("OBJECTID"))
		{
			parteDelComparendo = "OBJECTID";
		}
		else if (palabra.equals("FECHA_HORA"))
		{
			parteDelComparendo = "FECHA_HORA";
		}
		else if (palabra.equals("MEDIO_DETECCION"))
		{
			parteDelComparendo = "MEDIO_DETECCION";
		}
		else if (palabra.equals("CLASE_VEHICULO"))
		{
			parteDelComparendo = "CLASE_VEHICULO";
		}
		else if (palabra.equals("TIPO_SERVICIO"))
		{
			parteDelComparendo = "TIPO_SERVICIO";
		}
		else if (palabra.equals("INFRACCION"))
		{
			parteDelComparendo = "INFRACCION";
		}
		else if (palabra.equals("DES_INFRACCION"))
		{
			parteDelComparendo = "DES_INFRACCION";
		}
		else if (palabra.equals("LOCALIDAD"))
		{
			parteDelComparendo = "LOCALIDAD";
		}
		else if (palabra.equals("MUNICIPIO"))
		{
			parteDelComparendo = "MUNICIPIO";
		}
		else if (palabra.equals("coordinates"))
		{
			parteDelComparendo = "coordinates";
		}
	}

	public void agregarCoordenada(double pCor)
	{
		if(coordenadas == false)
		{
			compaAgregar.asignarLongitud(pCor);

			if (pCor < minLongitud)
			{
				minLongitud = pCor;
			}
			else if (pCor > maxLongitud)
			{
				maxLongitud = pCor;
			}

			coordenadas = true;
		}

		else
		{
			compaAgregar.asignarLatitud(pCor);
			//System.out.println("Latitud: " + pCor);

			if (pCor < minLatitud)
			{
				minLatitud = pCor;
			}
			else if (pCor > maxLatitud)
			{
				maxLatitud = pCor;
			}

			coordenadas = false;
			parteDelComparendo = "";
			
			////////////////////////////////////
			///////CARGAR LAS ESTRUCTURAS///////
			////////////////////////////////////
	
			String keyBob = getFechaModBobi(compaAgregar.darFecha_Hora());	
			String keyJuanJo = compaAgregar.darMedio_Dete() + "-" + compaAgregar.darClase_Vehi() + "-" + compaAgregar.darTipo_Servicio() + "-" + compaAgregar.darLocalidad();
			
			HSLBobi.putInSet(keyBob, compaAgregar);
			HSCJuanjo.putInSet(keyJuanJo, compaAgregar);
			datosHeap.añadir(compaAgregar);
			datosCola.agregar(compaAgregar);
			booty.enqueue(compaAgregar);
			
			keyBob = "";
			keyJuanJo = "";
			
			////////////////////////////////////
			////////////////////////////////////
			
			compaAgregar = null;


		}
	}
	
	public String getFechaModBobi(Date fechaMod)
	{
	    SimpleDateFormat sf = new SimpleDateFormat("MM-dd");
	    return sf.format(fechaMod);
	}

	////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////TODO PROYECTO///////
	////////////////////////////////////////////////////////////////////////////////////
	
	//////////////////////////////
	////////DAR ESTRUCTURAS///////
	//////////////////////////////
	
	public TablaHashSondeoLineal<String, Comparendo> darHashLineal()
	{
		return HSLBobi;
	}
	
	public TablaHashEncSeparado<String, Comparendo> darHashEncadenado()
	{
		return HSCJuanjo;
	}
	
	public MaxHeapCP<Comparendo> darMaxHeapCP()
	{
		return datosHeap;
	}

	public MaxColaCP<Comparendo> darMaxCola()
	{
		return datosCola;
	}
	
	public ListaEnlazadaQueue<Comparendo> darListEnlzadaCola()
	{
		return booty;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////TODO BOBBY////////
	/////////////////////////////////////////////////////////////////////////////////////
	
	public MaxHeapCP<Comparendo> darMComparendosGravedad(int m)
	{
		return null;
	}
	
	public void buscarPorMesYDia(int mes, String dia)
	{
		
	}
	
	public void buscarFechaHoraLocalidad (String FechaHora, String Localidad)
	{
		
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////TODO JUANJO///////
	/////////////////////////////////////////////////////////////////////////////////////
	
	public MaxColaCP<Comparendo> darMComparendosPolicia(int m)
	{
		return null;
	}
	
	public void buscarMedioClaseTipoLoca(String Medio, String Clase, String Tipo, String Localidad)
	{
		
	}
	
	public void buscarLatitudTipo (String Latitud, String Tipo)
	{
		
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////TODO AMBOS////////
	/////////////////////////////////////////////////////////////////////////////////////
	
	public ArrayList<String> aclararInfoPorRangos (int N)
	{
		return null;
	}
	
	public ArrayList<String> costosTotalesMetodoViejoYAburrido()
	{
		return null;
	}
	
	public void nuevoModeloQueEsMejorYAhorraPlata()
	{
		
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////EXTRA////////
	/////////////////////////////////////////////////////////////////////////////////////
	
	public static boolean less(Comparable compi1, Comparable compi2)
	{
		return compi1.compareTo(compi2) < 0;
	}
	
	public static void exchange(Comparable[] copia, int pos1, int pos2)
	{
		Comparable tempo = copia[pos1];
		copia[pos1] = copia[pos2];
		copia[pos2] = tempo;
	}
	
	public Comparable[] shell_sort_Fecha(Comparendo[] copia)
	{
		int N = copia.length;
		int h = 1;

		while(h < N/3)
		{
			h = 3*h +1;
		}

		while(h>=1)
		{
			for (int i = h; i < N; i++)
			{
				for(int j = i; j>=h && less(copia[j], copia[j-h]); j = j -h)
				{
					exchange(copia,j,j-h);
				}
			}

			h = h/3;
		}

		return copia;

	}

}






