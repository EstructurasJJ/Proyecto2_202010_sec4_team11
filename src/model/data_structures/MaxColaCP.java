package model.data_structures;

import model.logic.Comparendo;

public class MaxColaCP <T extends Comparable <T>>
{
	
	private T[] datos;
	private int size;
	private static final int EARTH_RADIUS = 6371;
	public static final double LAT_ESTPOL=4.647586;
	public static final double LONG_ESTPOL=74.078122;

	//Metodo constructor, crea los un objeto de la clase vacío

	public MaxColaCP ()
	{
		datos =(T[]) new Comparable[3];
		size=0;
	}

	public int darSize()
	{
		return size;
	}
	
	public T[] darDatos()
	{
		return datos;
	}

	public void verificarHeapTamaño()
	{
		if(datos.length-1 == size)
		{
			T[] prov = (T[]) new Comparable[size+10];
			
			for(int i=1; i < datos.length; i++)
			{
				prov[i] = datos[i];
			}
			
			datos = prov;
		}
	}
	
	
	public boolean less (int i, int j)
	{
		Comparendo pri=(Comparendo)datos[i], seg=(Comparendo)datos[j];

		if (pri != null && seg!=null)
		{
			double distPri=distance(pri.darLatitud(),pri.darLongitud(),LAT_ESTPOL, LONG_ESTPOL);
			double distSeg=distance(seg.darLatitud(),seg.darLongitud(),LAT_ESTPOL, LONG_ESTPOL);
			return distPri<distSeg;
		}
		else
		{
			return false;
		}
	}
	
	private void exchange (int pos1, int pos2)
	{
		T tempo = datos[pos1];
		datos[pos1] = datos[pos2];
		datos[pos2] = tempo;
	}
	
	public void swim (int k)
	{
		while (k > 1 && !less(k/2, k))
		{
			exchange(k, k/2);
			k = k/2;
		}
	}
	
	public void agregar (T elem)
	{
		verificarHeapTamaño();
		datos[++size] = elem;
		swim(size);
	}
	
	private void sink (int k)
	{
		while (2*k <= size)
		{
			int j = 2*k;
			
			if (j < size && !less(j, j+1))
			{
				j++;
			}
			
			if (less(k,j))
			{
				break;
			}
			
			exchange(k, j);
			k = j;
		}
	}
	
	public T eliminarMax()
	{
		T max = datos[1];
		exchange(1, size--);
		
		sink(1);
		datos[++size] = null;
		
		size--;
		
		return max;
	}
	
	
	
	public T darMax()
	{
		return datos[1];
	}
	
	
	
	public boolean emptyList()
	{
		return size==0;
	}




	public static double distance(double startLat, double startLong, double endLat, double endLong)
	{
		double dLat=Math.toRadians((endLat-startLat));
		double dLong=Math.toRadians((endLong-startLong));

		startLat=Math.toRadians(startLat);
		endLat   = Math.toRadians(endLat);

		double a = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));	

		return EARTH_RADIUS * c;
	}

	private static double haversin(double val)
	{
		return Math.pow(Math.sin(val/2), 2);
	}



}



