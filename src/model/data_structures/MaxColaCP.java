package model.data_structures;

import java.util.Iterator;
import model.logic.Comparendo;

public class MaxColaCP <T extends Comparable <T>>
{
	private Node <T> firstNode;
	private Node <T> lastNode;
	private int size;
	private static final int EARTH_RADIUS = 6371;
	public static final double LAT_ESTPOL=4.647586;
	public static final double LONG_ESTPOL=74.078122;
	
	//Metodo constructor, crea los un objeto de la clase vacío
	
	public MaxColaCP ()
	{
		firstNode=null;
		lastNode=null;
		size=0;
	}
	
	public int darSize()
	{
		return size;
	}
	
	public boolean emptyList()
	{
		return (darSize()==0);
	}
	
	
	public void agregar(T elemento)
	{
		Node<T> anteriorUlt= lastNode;
		Node<T> porAgregar= new Node(elemento);
		lastNode = porAgregar;
		lastNode.cambiarSiguiente(null);
		
		if (emptyList())
		{
				firstNode=lastNode;
		}
		else
		{
			anteriorUlt.cambiarSiguiente(lastNode);
		}
		
		size++;
		int aux=size -1;
		swim (aux);
		
	}
	
	//Devuelve y elimina el mayor elemento de la lista, si está vacía devuelve null
	
	public T eliminarMax()
	{
		T rip=null;
		if(!emptyList())
		{
			rip=(T)firstNode.darData();
			firstNode=firstNode.darSiguiente();
		}
		
		
		if(emptyList())
		{
			lastNode=null;
		}
		else
		{
			size--;
		}
		return rip;
	}
	
	//Retorna el máximo sin eliminarlo de la lista
	public T darMax()
	{
		return (T)firstNode.darData();
	}
	
	public void swim(int t)
	{
		while (t>0 && less(t,t/2))
		{
			exchange(t,t/2);
			t=t/2;
		}
	}
	
	public boolean less (int i, int j)
	{
		Comparable [] x=darDatosEnArreglo();
		Comparendo pri=(Comparendo)x[i], seg=(Comparendo)x[j];
		
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
	
	public void exchange(int i, int j)
	{
		Comparable[] x=darDatosEnArreglo();
		T aux= (T)x[i];
		T aux2=(T)x[j];
		x[i]=aux2;
		x[j]=aux;
		
		devolverALista(x);
	}
	
	public void devolverALista(Comparable[] x)
	{	
		Comparendo aux = (Comparendo)x[0];
		firstNode=new Node(aux);
		Node<T> actual= firstNode;
		
		for (int i=1;i<x.length;i++)
		{
			actual.cambiarSiguiente(new Node((Comparendo)x[i]));
			actual=actual.darSiguiente();
			if(i==x.length-1)
			{
				lastNode = actual;
			}
		}
		
		
	}
	

	
	public Iterator<T> iterator()
	{ 
		return new ListIterator(); 
	}

	private class ListIterator implements Iterator<T>
	{
		private Node<T> current = firstNode;
		
		public boolean hasNext()
		{ 
			return current != null; 
		}
		
		public void remove() 
		{ }

		public T next()
		{
			T item = (T)current.darData();
			current = current.darSiguiente();
			return item;
		}
	} 
	
	public Comparable[] darDatosEnArreglo()
	{
		Comparable [] result= new Comparable[size];

		Node<T> actual=firstNode;
		int cuenta=0;
		
		while(actual!=null)
		{
			result[cuenta]=actual.darData();
			
			actual=actual.darSiguiente();
			cuenta++;
		}
		
		return result; 
	}
	
	public T darPrimerNodo()
	{
		if (firstNode == null)
		{
			return null;
		}
		else
		{
			return (T)firstNode.darData();
		}
	}
	
	public T darUltNodo()
	{
		if (lastNode == null)
		{
			return null;
		}
		else
		{
			return (T)lastNode.darData();
		}
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



