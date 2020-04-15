package model.logic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
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
			//String keyJuanJo = compaAgregar.darMedio_Dete() + "-" + compaAgregar.darClase_Vehi() + "-" + compaAgregar.darTipo_Servicio() + "-" + compaAgregar.darLocalidad();
			HSLBobi.putInSet(keyBob, compaAgregar);
			//HSCJuanjo.putInSet(keyJuanJo, compaAgregar);
			keyBob = "";
			//keyJuanJo = "";


			//datosHeap.añadir(compaAgregar);
			//datosCola.agregar(compaAgregar);

			booty.enqueue(compaAgregar);

			//arbolBobi.put(compaAgregar.darFecha_Hora(), compaAgregar);
			//arbolJuanjo.put(compaAgregar.darLatitud(), compaAgregar);



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

	public ListaEnlazadaQueue<Comparendo> darMComparendosPolicia(int m)
	{
		ListaEnlazadaQueue<Comparendo> respuesta=new ListaEnlazadaQueue<Comparendo>();

		while (m>0)
		{
			respuesta.enqueue(datosCola.eliminarMax());

			m--;
		}

		return respuesta;
	}

	public Comparable[] buscarMedioClaseTipoLoca(String Medio, String Clase, String Tipo, String Localidad)
	{
		String key = Medio + "-"  + Clase + "-" + Tipo + "-" + Localidad;

		Comparendo[] comparendos = copiarComparendosEncadenados(key);
		Comparable [] ordenados = shell_sort_Fecha(comparendos);

		return ordenados;

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

		System.out.println("Total de comparendos: " + bobi.darDatos());

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
			System.out.println(fecha1 + "-" + fecha2 + "    |   " + asteriscos + " // " + total );

		}

	}

	public ArrayList<String[]> costosTotalesMetodoViejoYAburrido()
	{
		Comparendo[] datos= copiarComparendos();
		Comparable[] ordenados= shell_sort_Fecha(datos);
		ArrayList<String[]> respuesta=new ArrayList<String[]>();
		int costoTotal=0;
		int minT400=400, maxT400=0, minT40=400, maxT40=0, minT4=400, maxT4=0, cantidad400=0, cantidad40=0,cantidad4=0, totDT4=0,totDT40=0, totDT400=0,contadorDiario=0;
		SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
		Date diaDelAnio=null, nextYear=null;
		int parteDelArreglo = 0;
		int valorMulta=0;
		boolean salir=false;
		int contadorEspera=0;

		try 
		{
			diaDelAnio= sdf.parse("01/01/2018");
			nextYear=sdf.parse("01/01/2019");
		}
		catch (ParseException e) {e.printStackTrace();}


		while(daysBetween(diaDelAnio, nextYear)>0)
		{
			salir=false;
			while (contadorDiario<1500 && parteDelArreglo<ordenados.length && !salir)
			{
				Comparendo actual=(Comparendo)ordenados[parteDelArreglo];
				Date fechaAnalizada=actual.darFecha_Hora();
				String descMulta=actual.darDes_Infrac();

				//Para no analizar los comparendos del futuro si me entraron menos de 1500
				if (esAntesDMY(diaDelAnio,fechaAnalizada))
				{
					salir=true;
				}
				else
				{
					valorMulta = calcularValorMulta(descMulta);
					if (valorMulta == 400)
						cantidad400++;
					else if (valorMulta==40)
						cantidad40++;
					else
						cantidad4++;

					//Si el comparendo se está leyendo de forma puntual
					if (mismoDMY(diaDelAnio,fechaAnalizada))
					{
						if (valorMulta==4)
							minT4=0;
						else if (valorMulta==40)
							minT40=0;
						else
							minT400=0;
					}
					else
					{
						//Calculo los días que tuvo que esperar para ser procesado
						
						int diasDeEspera=daysBetween(fechaAnalizada,diaDelAnio);
						costoTotal += (diasDeEspera*valorMulta);

						if (valorMulta==4)
						{ 
							totDT4 += diasDeEspera;

							if (diasDeEspera>maxT4)
								maxT4=diasDeEspera;
						}
						else if (valorMulta==40)
						{
							totDT40 += diasDeEspera;

							if (diasDeEspera>maxT40)
								maxT40=diasDeEspera;
						}
						else
						{
							totDT400 += diasDeEspera;

							if (diasDeEspera>maxT400)
								maxT400=diasDeEspera;
						}
					}
					contadorDiario++;
					parteDelArreglo++;
				}
			}

			//Significa que ya leí los 1500 pero no he cambiado de fecha 
			//Con eso cuento cuántos comparendos tengo represados para ese día
			if(parteDelArreglo<ordenados.length && (mismoDMY(diaDelAnio,((Comparendo)ordenados[parteDelArreglo]).darFecha_Hora()) || ((Comparendo)ordenados[parteDelArreglo]).darFecha_Hora().before(diaDelAnio)) )
			{
				int contadorAux=parteDelArreglo;
				Date fechaAuxiliar=((Comparendo)ordenados[contadorAux]).darFecha_Hora();

				//Mientras no se salga del arreglo y esté antes o en el mismo día en el que estoy 
				while (contadorAux<ordenados.length && (mismoDMY(diaDelAnio,fechaAuxiliar) || ((Comparendo)ordenados[contadorAux]).darFecha_Hora().before(diaDelAnio)))
				{
					contadorEspera++;
					contadorAux++;
					if (contadorAux<ordenados.length)
					{
						fechaAuxiliar=((Comparendo)ordenados[contadorAux]).darFecha_Hora();
					}
				}			
			}

			String[] pars = new String [3];
			pars[0] = sdf.format(diaDelAnio);
			pars[1]=contadorDiario+"";

			pars[2] = contadorEspera+"";
			respuesta.add(pars);

			contadorEspera=0;
			contadorDiario=0;
			diaDelAnio=avanzarD(diaDelAnio);

		}

		//Proceso los datos que quedaron faltando del 31 de diciembre
		//Igualmente se leen 1500 comparendos diarios, pero no entran los de 2019

		if (parteDelArreglo<ordenados.length)
		{
			String multisha="";

			while (parteDelArreglo<ordenados.length)
			{
				contadorDiario=0;

				while (contadorDiario<1500 && parteDelArreglo<ordenados.length)
				{
					Comparendo aux=(Comparendo)ordenados[parteDelArreglo];
					multisha = aux.darDes_Infrac();
					Date analizada=aux.darFecha_Hora();

					valorMulta = calcularValorMulta(multisha);
					if (valorMulta == 400)
						cantidad400++;
					else if (valorMulta==40)
						cantidad40++;
					else
						cantidad4++;

					int espera=daysBetween(analizada, nextYear);

					costoTotal += (espera*valorMulta);

					if (valorMulta==4)
					{ 
						totDT4 += espera;

						if (espera>maxT4)
							maxT4=espera;
					}
					else if (valorMulta==40)
					{
						totDT40 += espera;

						if (espera>maxT40)
							maxT40=espera;
					}
					else
					{
						totDT400 +=espera;

						if (espera>maxT400)
							maxT400=espera;
					}
					contadorDiario++;
					parteDelArreglo++;
				}
				
				nextYear=avanzarD(nextYear);
			}
		}
		
		System.out.println("Fecha\t\t|ComparendosProcesados\t\t\t\t***");
		System.out.println("\t\t|Comparendos que están en espera\t\t###");
		System.out.println("-----------------------------------------------------------------------");
		
		
		for (String[] arreglo: respuesta)
		{
			//System.out.println(arreglo[0]+"-"+arreglo[1]+"-"+arreglo[2]);
			String[] aux=darCadenasAsteriscosYNumerales(arreglo);
			System.out.println(arreglo[0]+"\t|"+aux[0]);
			System.out.println("\t\t|"+aux[1]);
			
		}

		System.out.println("El costo total es de: $" + costoTotal);
		System.out.println("El tiempo total de espera de los comparendos fue de: "+(totDT4+totDT40+totDT400)+" días");
		System.out.println("En promedio, un comparendo debe esperar: "+((totDT4+totDT40+totDT400)/ordenados.length)+" días");

		System.out.println("A continuación se esperan los datos de interés de cada tipo de comparendo según su multa:");
		System.out.println("Costo Diario\t|Tiempo Mínimo\t|Tiempo Promedio\t|Tiempo Max");
		System.out.println("\t4\t|"+minT4+"\t\t|"+(totDT4/cantidad4)+"\t\t\t|"+maxT4);
		System.out.println("\t40\t|"+minT40+"\t\t|"+(totDT40/cantidad40)+"\t\t\t|"+maxT40);
		System.out.println("\t400\t|"+minT400+"\t\t|"+(totDT400/cantidad400)+"\t\t\t|"+maxT400);
		return respuesta;
	}



	public void nuevoModeloQueEsMejorYAhorraPlata()
	{

		//Estructuras

		ArrayList<String> listaFechas = generarListaDeFechas();
		TablaHashSondeoLineal<String, Comparendo> bobi = tablaSuperNueva();
		ListaEnlazadaQueue<Comparendo> actual = new ListaEnlazadaQueue<Comparendo>();

		long total = 0;

		//Proceso para el año regular

		int i = 0;
		while (i < listaFechas.size())
		{
			String fechaRef = listaFechas.get(i) + "-00:00:00";;
			Date fechaReferencia = generarFecha(fechaRef);

			String fechaActual = listaFechas.get(i);
			Iterator subConjunto = bobi.getSet(fechaActual);
			actual = pasarIterCola(subConjunto, actual);

			//Calculo.
			int cantidad = 0;			
			while(cantidad < 1500 && actual.darTamanio() > 0)
			{
				Comparendo compi = actual.dequeue();

				String fechaCamb =  fechaAmbosFormato(compi.darFecha_Hora()) + "-00:00:00";
				Date fechaCambiante = generarFecha(fechaCamb);

				int diferencia = diasEntreDosFechas(fechaReferencia, fechaCambiante);
				int valor = multaComparendo(compi);				

				total = total + (diferencia*valor);
				cantidad++;
			}

			i++;
		}

		System.out.println("----------------");
		System.out.println("Total parcial: $" + total);
		System.out.println("Comparendos restantes: " + actual.darTamanio());
		System.out.println("----------------");

		//Proceso para los que sobraron.

		long faltante = 0;

		int dia = 1;
		while (actual.darTamanio() > 0 )
		{
			String fechaRef = "";

			if(dia < 10) fechaRef = "2019/01/0" + dia + "-00:00:00";
			else fechaRef = "2019/01/" + dia + "-00:00:00";

			Date fechaReferencia = generarFecha(fechaRef);

			int cantidad = 0;			
			while(cantidad < 1500 && actual.darTamanio() > 0)
			{
				Comparendo compi = actual.dequeue();

				String fechaCamb =  fechaAmbosFormato(compi.darFecha_Hora()) + "-00:00:00";
				Date fechaCambiante = generarFecha(fechaCamb);

				int diferencia = diasEntreDosFechas(fechaReferencia, fechaCambiante);				
				int valor = multaComparendo(compi);				

				faltante = faltante + (diferencia*valor);
				cantidad++;
			}

			dia++;
		}

		System.out.println("Faltante en 2019: $" + faltante);
		System.out.println("Comparendos restantes: " + actual.darTamanio());
		System.out.println("----------------");

		long ultimo = total + faltante;
		System.out.println("Total: $" + ultimo);
		System.out.println("----------------");
	}

	/////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////EXTRA////////
	/////////////////////////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////////////////////BOBI

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
		SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
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

	private TablaHashSondeoLineal<String, Comparendo> tablaSuperNueva()
	{
		TablaHashSondeoLineal<String, Comparendo> tablitaNueva = new TablaHashSondeoLineal<String, Comparendo>(2);
		Node<Comparendo> actual = booty.darPrimerElemento();

		while(actual != null)
		{
			Comparendo compi = actual.darData();
			String fecha = fechaAmbosFormato(compi.darFecha_Hora());
			int clasi = clasificacion(compi);

			tablitaNueva.putInSetEspecial(fecha, compi, clasi);
			actual = actual.darSiguiente();

		}

		return tablitaNueva;
	}

	private int clasificacion (Comparendo compi)
	{
		String multisha = compi.darDes_Infrac();

		if (multisha.contains("LICENCIA DE CONDUC") || multisha.contains("SERA INMOVILIZADO") || multisha.contains("SERÁ INMOVILIZADO")) return 1;
		else return 2;
	}

	private int diasEntreDosFechas(Date fech1, Date fech2)
	{
		long tim2 = fech2.getTime();
		long tim1 = fech1.getTime();

		long dif = Math.abs(tim2-tim1);

		long dias = dif/86400000;

		int findias = (int) dias;
		return findias;
	}

	private ListaEnlazadaQueue<Comparendo> pasarIterCola (Iterator conjunto, ListaEnlazadaQueue<Comparendo> actual)
	{
		while(conjunto.hasNext())
		{
			Comparendo compi = (Comparendo) conjunto.next();

			int clasi = clasificacion(compi);
			if(clasi == 1) actual.push(compi);
			else actual.enqueue(compi);		
		}

		return actual;
	}

	private int multaComparendo (Comparendo compi)
	{
		String multisha = compi.darDes_Infrac();

		if (multisha.contains("SERA INMOVILIZADO") || multisha.contains("SERÁ INMOVILIZADO")) return 400;
		else if (multisha.contains("LICENCIA DE CONDUC")) return 40;
		else return 4;

	}

	/////////////////////////////////////////////////////////////////////////////////JUANJO

	private int daysBetween (Date actual, Date analizado)
	{
		int respuesta=0;
		String cadenaActual="", cadenaAnalizado="";

		SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");

		try
		{
			cadenaActual=sdf.format(actual);
			cadenaAnalizado=sdf.format(analizado);
		}
		catch(Exception e) {e.printStackTrace();}

		while (!cadenaActual.equals(cadenaAnalizado))
		{
			actual=avanzarD(actual);
			respuesta++;
			cadenaActual=sdf.format(actual);
		}

		return respuesta;
	}

	private boolean mismoDMY(Date fecha1, Date fecha2)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String cadena1= sdf.format(fecha1);
		String cadena2=sdf.format(fecha2);

		return cadena1.equals(cadena2);
	}

	private Date avanzarD(Date inicial)
	{
		Calendar c = Calendar.getInstance(); 
		c.setTime(inicial); 
		c.add(Calendar.DATE, 1);
		inicial = c.getTime();

		return inicial;
	}

	private boolean esAntesDMY(Date fecha1, Date fecha2) 
	{
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		String cadena1=sdf.format(fecha1), cadena2=sdf.format(fecha2);
		String[] arreglo1=cadena1.split("/"), arreglo2=cadena2.split("/");
		boolean respuesta=false;

		if (Integer.parseInt(arreglo1[1])<Integer.parseInt(arreglo2[1]))
		{
			respuesta=true;
		}
		else if (Integer.parseInt(arreglo1[1])>Integer.parseInt(arreglo2[1]))
		{
			return false;
		}
		else if (Integer.parseInt(arreglo1[0])<Integer.parseInt(arreglo2[0]))
		{
			respuesta=true;
		}

		return respuesta;

	}

	private Comparendo[] copiarComparendosEncadenados(String key)
	{
		Iterator<Comparendo> comparendos = HSCJuanjo.getSet(key), sizeComparendos=HSCJuanjo.getSet(key);

		int s=0;
		if (sizeComparendos!=null)
		{
			while(sizeComparendos.hasNext())
			{
				sizeComparendos.next();
				s++;
			}	
		}


		Comparendo[] respuesta =  new Comparendo[s];
		int contador=0;

		if (comparendos!=null)
		{
			while (comparendos.hasNext())
			{
				Comparendo aux = comparendos.next();
				respuesta[contador] = aux;
				contador++;
			}
		}

		return respuesta;
	}
	
	private String[] darCadenasAsteriscosYNumerales(String [] arreglo)
	{
		String [] respuesta = new String [2];
		String cadenaAsteriscos="", cadenaNumerales="";
		
		int numAsteriscos = Integer.parseInt(arreglo[1])/150;
		int numNumerales = Integer.parseInt(arreglo[2])/150;
		
		for (int i=0;i<numAsteriscos;i++)
		{
			cadenaAsteriscos=cadenaAsteriscos +"*";
		}
		
		for (int i=0;i<numNumerales;i++)
		{
			cadenaNumerales=cadenaNumerales +"#";
		}
		
		respuesta[0]=cadenaAsteriscos;
		respuesta[1]=cadenaNumerales;
		
		return respuesta;
		
	}
	
	private int calcularValorMulta(String desc)
	{
		if (desc.contains("SERA INMOVILIZADO") || desc.contains("SERÁ INMOVILIZADO")||desc.contains("INMOVILIZADO"))
		{
			return 400;
		}
		else if (desc.contains("LICENCIA DE CONDUC"))
		{
			return 40;
		}
		else
		{
			return 4;
		}
	}

	/////////////////////////////////////////////////////////////////////////////////AMBOS

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