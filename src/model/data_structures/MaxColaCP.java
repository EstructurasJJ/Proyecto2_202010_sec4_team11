package model.data_structures;

import java.util.Iterator;

public class MaxColaCP <T extends Comparable <T>>
{
	private Node <T> firstNode;
	private Node <T> lastNode;
	private int size;
	
	
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
		Node<T> porAgregar=new Node(elemento);
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
		swim (size);
		
	}
	
	//Devuelve y elimina el mayor elemento de la lista, si está vacía devuelve null
	
	public T eliminarMax()
	{
		T rip=(T)firstNode.darData();
		firstNode=firstNode.darSiguiente();
		
		if(emptyList())
		{
			lastNode=null;
		}
		size--;
		return rip;
	}
	
	//Retorna el máximo sin eliminarlo de la lista
	public T darMax()
	{
		return (T)firstNode.darData();
	}
	
	public void swim(int t)
	{
		while (t>1 && less(t/2,t))
		{
			exchange(t,t/2);
			t=t/2;
		}
	}
	
	public void sink(int t)
	{
		while (2*t <= size)
		{
			int j = 2*t;
			if (j<size && less(j,j+1))
			{
				j++;
			}
			if (!less(t,j))
			{
				break;
			}
			exchange(t,j);
			t=j;
		}
	}
	
	public boolean less (int i, int j)
	{
		Comparable [] x=darDatosEnArreglo();
		if (x[i] != null && x[j]!=null)
		{
			return x[i].compareTo(x[j])<0;
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
		Comparable [] result= new Comparable[size+1];

		Iterator<T> iter= iterator(); 
		int cont=0; 
		while(iter.hasNext())
		{
			result[cont]=iter.next(); 
			cont++;
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
	
}
