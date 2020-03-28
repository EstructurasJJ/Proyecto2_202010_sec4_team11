package test.data_structures;

import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;

import model.data_structures.TablaHashEncSeparado;

public class TestTablaHashEncSeparado 
{
	private TablaHashEncSeparado<Integer, Integer> thes;

	public void setUp()
	{
		thes = new TablaHashEncSeparado<Integer, Integer>(1);



		for(int i = 0; i <100; i++)
		{
			int key = (int) (Math.random() *999);
			thes.putInSet(key, i);
		}

	}


	@Test
	public void testAgregarGet()
	{
		setUp();
		assertTrue( 100 == thes.darNumElementos());
		int key = 88;

		thes.putInSet(key, 1000);
		assertTrue(thes.getSet(key) != null);

	}


	@Test
	public void testEliminar()
	{
		setUp();


		thes.deleteSet(88);
		assertTrue(thes.getSet(88)==null);

	}







}
