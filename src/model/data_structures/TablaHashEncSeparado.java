package model.data_structures;

import java.util.Iterator;

public class TablaHashEncSeparado <K extends Comparable <K>, V extends Comparable <V>>
{
	
	private int capacidadM;
	private int totalN;
	private K[] keys;
	private ListaEnlazadaQueue[] valores;
	private int rehashes;
	
	
	public TablaHashEncSeparado(int m) 
	{
		capacidadM=m;
		totalN=0;
		
		keys = (K[]) new Comparable[capacidadM];
		valores= new ListaEnlazadaQueue[capacidadM];
		
	}
	
	
	public int darRehashes()
	{
		return rehashes;
	}
	
	public double darFactorDeCarga()
	{
		return totalN/capacidadM;
	}
	
	public int darNumElementos()
	{
		return totalN;
	}
	
	public int darCapacidad()
	{
		return capacidadM;
	}
	
	private int hash(K key)
	{
		return (key.hashCode() & 0x7fffffff) % capacidadM;
	}
	
	private void resize(int nuevaM)
	{
		TablaHashEncSeparado<K,V> aux = new TablaHashEncSeparado<K,V>(nuevaM);
		
		for (int i=0;i<capacidadM;i++)
		{
			if (keys[i]!=null)
			{
				Node <V> actual = valores[i].darPrimerElemento();
				
				while(actual!=null)
				{
					V eliminado = (V)(valores[i].dequeue());
					aux.putInSet(keys[i], eliminado);
					actual=valores[i].darPrimerElemento();
				}
			}
		}
		
		this.keys = aux.keys;
		this.valores = aux.valores;
	}
	
	
	public void putInSet(K key, V value) 
	{
		if (key!=null)
		{
						
			int i=hash(key);
			if (valores[i]==null)
			{
				valores[i]=new ListaEnlazadaQueue<V>();
			}
			valores[i].enqueue(value);
			totalN++;
			
			if (keys[i]==null)
			{
				keys[i]=key;
			}
			
			if (totalN>capacidadM*5)
			{
				resize(capacidadM*2);
				capacidadM=2*capacidadM;
				rehashes++;
			}

		}
		
	}
	
	public Iterator<V> getSet(K key) 
	{
		if (key!=null)
		{
			int i = hash(key);
			if (valores[i]!=null)
			{
				if (valores[i].darTamanio()>0)
				{
					return valores[i].iterator();
				}
			}
			
			
		}
		
		return null;
	}
	
	private boolean contains(K key)
	{
		return getSet(key)!=null;
	}
	
	public Iterator deleteSet(K key) 
	{
		if (!contains(key)) return null;
		
		ListaEnlazadaQueue respuesta=new ListaEnlazadaQueue<V>();
		
		int i = hash(key);
		
		keys[i]=null;
		
		while (valores[i].darPrimerElemento()!=null)
		{
			respuesta.enqueue(valores[i].dequeue());
			totalN--;
		}
		
		while (totalN<=capacidadM*2.5 && totalN>0)
		{
			resize(capacidadM/2);
			capacidadM = capacidadM/2;
			rehashes++;
		}
		
		if (totalN==0)
		{
			resize(1);
		}
		
		return respuesta.iterator();
	}

	public Iterator keys() 
	{ 
		ListaEnlazadaQueue<K> lista = new ListaEnlazadaQueue<K>();
		
		for (K k: keys)
		{
			if (k!=null) lista.enqueue(k);
		}
		return lista.iterator();
	}




}
