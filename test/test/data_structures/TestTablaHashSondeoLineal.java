package test.data_structures;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.data_structures.TablaHashSondeoLineal;

public class TestTablaHashSondeoLineal 
{
	private TablaHashSondeoLineal<Integer, Integer> HSLBobi;
	
	@Before
	public void setUp()
	{
		HSLBobi = new TablaHashSondeoLineal<Integer, Integer>(1);
		
		int key = (int) (Math.random() *999);
		
		for(int i = 0; i <100; i++)
		{
			HSLBobi.putInSet(key, i);
		}
	}
	
	@Test
	public void testAgregarGet()
	{
		int key = 88;
		
		HSLBobi.putInSet(key, 1000);
		assertTrue(HSLBobi.getSet(key) != null);
		
	}
	
	@Test
	public void testEliminar()
	{
		int key = 88;
		
		HSLBobi.deleteSet(key);
		assertTrue(HSLBobi.getSet(key) == null);
		
	}
	
}
