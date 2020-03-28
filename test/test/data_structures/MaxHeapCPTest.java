package test.data_structures;

import static org.junit.Assert.*;

import org.junit.Test;

import model.data_structures.MaxHeapCP;
import model.logic.Comparendo;

public class MaxHeapCPTest 
{
	private MaxHeapCP heap;

	public void setUpVacio()
	{
		heap = new MaxHeapCP();
	}

	public void setUp1()
	{
		heap = new MaxHeapCP();

		for(int i = 1; i < 100; i++)
		{
			int random = (int) Math.floor((100 * Math.random()));
			heap.añadir(random);
		}
		
		heap.añadir(100);
		heap.añadir(99);
	}
	
	public void setUp2()
	{
		heap = new MaxHeapCP();

		for(int i = 1; i < 100; i++)
		{
			int dat = i;
			heap.añadir(dat);
		}
		
	}

	@Test
	public void estaVaciaTest()
	{
		setUpVacio();
		assertTrue(heap.esVacia());
	}
	@Test
	public void testAgregar()
	{
		setUp1();
		assertEquals(100, heap.darHeap()[1] );
		assertEquals(99, heap.darHeap()[3] );
	}
	
	@Test
	public void testSacarMax()
	{
		setUp1();
		Comparable x = heap.devolverMax();
		assertEquals(100, x);
	}
	
	@Test
	public void testSacarMax2()
	{
		setUp2();
		
		for(int i = 99; i>0; i--)
		{
			Comparable x = heap.devolverMax();
			assertEquals(i, x);
		}
		

	}

}
