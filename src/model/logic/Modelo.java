package model.logic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import model.data_structures.ArbolRojoNegro;
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
	
	private ArbolRojoNegro<Date, Comparendo> arbolBobi = new ArbolRojoNegro<Date, Comparendo>();
	private ArbolRojoNegro<Double, Comparendo> arbolJuanjo = new ArbolRojoNegro<Double, Comparendo>();
	
	static Comparator<Comparendo> fecha = new OrdenarComparendoFecha();
	
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

	private void componentesDelComparendo(String palabra)
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

	private void agregarCoordenada(double pCor)
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
			keyBob = "";
			keyJuanJo = "";
			
			
			//datosHeap.añadir(compaAgregar);
			//datosCola.agregar(compaAgregar);
			
			booty.enqueue(compaAgregar);
			
			arbolBobi.put(compaAgregar.darFecha_Hora(), compaAgregar);
			arbolJuanjo.put(compaAgregar.darLatitud(), compaAgregar);
			

			
			////////////////////////////////////
			////////////////////////////////////
			
			compaAgregar = null;


		}
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
	
	public ArbolRojoNegro<Date, Comparendo> darArbolBobi()
	{
		return arbolBobi;
	}
	
	public ArbolRojoNegro<Double, Comparendo> darArbolJuanjo()
	{
		return arbolJuanjo;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////TODO BOBBY////////
	/////////////////////////////////////////////////////////////////////////////////////
	
	public ArrayList<Comparendo> darMComparendosGravedad(int m)
	{
		ArrayList listica = new ArrayList<Comparendo>();
		
		int i = 0;
		
		while (i < m)
		{
			Comparendo compi = datosHeap.devolverMax();
			listica.add(compi);
			
			i++;
		}
		
		return listica;
		
	}
	
	public Iterator<Comparendo> buscarPorMesYDia(String mes, String dia)
	{
		String mesi = corregirMes(mes);
		String llave = mesi + "-" + dia;
		
		Iterator<Comparendo> lista = HSLBobi.getSet(llave);
		return lista;
	}
	
	public ArrayList<Comparendo> buscarFechaHoraLocalidad (String FechaHoraMin, String FechaHoraMax, String Localidad)
	{
		
		Date min = generarFecha(FechaHoraMin);
		Date max = generarFecha(FechaHoraMax);
		
		Iterator<Comparendo> lista = arbolBobi.values(min, max).iterator();
		ArrayList<Comparendo> listaFinal = new ArrayList<Comparendo>();
		
		while(lista.hasNext())
		{
			Comparendo compi = lista.next();
			
			if(compi.darLocalidad().equals(Localidad))
			{
				listaFinal.add(compi);
			}
		}
		
		return listaFinal;
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
	
	public void aclararInfoPorRangos (int N, int valor)
	{
		ArrayList<String> listaFechas = generarListaDeFechas();
		TablaHashSondeoLineal<String, Comparendo> bobi = pasarComparendoATablaBobi();
		
		System.out.println("Rango de fechas          |   Comparendos durante el año");
		System.out.println("-------------------------------------------------------");
		
		int i = 0;
		while (i < listaFechas.size())
		{
			int j = 0;
			int total = 0;
			
			String fecha2 = "";
			String fecha1 = listaFechas.get(i);
			
			while(j < N && i < listaFechas.size())
			{
				Iterator subConjunto = bobi.getSet(listaFechas.get(i));
				int contador = numeroComparendosEnPosTabla(subConjunto);
				
				total = total + contador;
				fecha2 = listaFechas.get(i);
				
				i++;
				j++;
			}
			
			String asteriscos = generarAsteriscos(valor, total);
			System.out.println(fecha1 + "-" + fecha2 + "    |   " + asteriscos);
			
		}

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
	
	private String getFechaModBobi(Date fechaMod)
	{
		Calendar calndr1 = (Calendar) Calendar.getInstance();
        calndr1.setTime(fechaMod);

        int dia = calndr1.get(Calendar.DAY_OF_WEEK);
        String diaLetra = diaNumAdiaLetra(dia);
	    SimpleDateFormat sf = new SimpleDateFormat("MM");
	    
	    String sfecha = sf.format(fechaMod) + "-" + diaLetra;
	    
	    return sfecha;
	    
	}
	
	private String diaNumAdiaLetra (int dia)
	{
		String dias = "";
		
		if(dia == 1) 		dias = "D";
		else if (dia == 2)	dias = "L";
		else if (dia == 3) 	dias = "M";
		else if (dia == 4) 	dias = "I";
		else if (dia == 5) 	dias = "J";
		else if (dia == 6) 	dias = "V";
		else if (dia == 7) 	dias = "S";
		
		return dias;
	}
	
	private String corregirMes (String m)
	{
		int tam = m.length();
		if (tam == 1) m = "0" + m; 
		
		return m;
	}
	
	private Date generarFecha (String fecha)
	{
		String[] dosPartes = fecha.split("-");
		String[] fecha1 = dosPartes[0].split("/");
		
		String fechaFinal = fecha1[0] + "-" + fecha1[1] + "-" + fecha1[2] + "T" + dosPartes[1] + ".000Z";
		
		Date fechita;
		
		try 
		{
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			fechita = formato.parse(fechaFinal);
		}
		catch(Exception e)
		{
			fechita = null;
		}
		
		return fechita;
	}
	
	private String fechaAmbosFormato(Date fechaMod)
	{
		SimpleDateFormat sf = new SimpleDateFormat("yyy/MM/dd");
		return sf.format(fechaMod);
	}
	
	private ArrayList<String> generarListaDeFechas()
	{
		Comparendo[] aOrdenar = copiarComparendos();
		Comparable[] ordenados = shell_sort_Fecha(aOrdenar);
		ArrayList<String> listaFechas = new ArrayList<String>();
		
		int i = 0;
		String fecha = "";
		
		while(i < ordenados.length)
		{
			String sf = fechaAmbosFormato(((Comparendo) ordenados[i]).darFecha_Hora());
			
			if(!fecha.equals(sf))
			{
				fecha = sf;
				listaFechas.add(fecha);
			}
			
			i++;
		}
		
		return listaFechas;
	}
	
	private TablaHashSondeoLineal<String, Comparendo> pasarComparendoATablaBobi()
	{
		TablaHashSondeoLineal<String, Comparendo> tablitaNueva = new TablaHashSondeoLineal<String, Comparendo>(2);
		Node<Comparendo> actual = booty.darPrimerElemento();

		while(actual != null)
		{
			Comparendo compi = actual.darData();
			String fecha = fechaAmbosFormato(compi.darFecha_Hora());
			
			tablitaNueva.putInSet(fecha, compi);
			actual = actual.darSiguiente();

		}
		
		return tablitaNueva;
	}

	private int numeroComparendosEnPosTabla(Iterator subConjunto)
	{
		int contador = 0;
		
		while(subConjunto.hasNext())
		{
			contador++;
			subConjunto.next();
		}
		
		return contador;
	}
	
	private String generarAsteriscos(int valor, int cantidad)
	{
		String aste = "";
		cantidad = (cantidad/valor)+1;


		for(int i = 0; i < cantidad; i++)
		{
			aste = aste + "*";
		}
		
		return aste;
	}
	
	private Comparendo[] copiarComparendos()
	{
		Comparendo[] comparendosCopia = new Comparendo[booty.darTamanio()];
		int contador = 0;

		Node<Comparendo> actual = booty.darPrimerElemento();

		while(actual != null)
		{
			Comparendo compi = actual.darData();
			comparendosCopia[contador] = compi;

			contador++;
			actual = actual.darSiguiente();

		}

		return comparendosCopia;
	}
	
	private static int lessFecha(Comparendo compi1, Comparendo compi2)
	{
		return fecha.compare(compi1, compi2);
	}
	
	private static void exchange(Comparable[] copia, int pos1, int pos2)
	{
		Comparable tempo = copia[pos1];
		copia[pos1] = copia[pos2];
		copia[pos2] = tempo;
	}
	
	private Comparable[] shell_sort_Fecha(Comparendo[] copia)
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
				for(int j = i; j>=h && lessFecha(copia[j], copia[j-h]) < 0; j = j -h)
				{
					exchange(copia,j,j-h);
				}
			}

			h = h/3;
		}

		return copia;

	}

}






