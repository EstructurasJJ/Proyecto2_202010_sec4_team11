package test.data_structures;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.data_structures.MaxColaCP;
import model.logic.Comparendo;

public class MaxColaCPTest 
{
	private MaxColaCP<Comparendo> booty;

	@Before
	public void setup1()
	{
		booty= new MaxColaCP<Comparendo>(); 
		int contador = 0;
		double valor = 4.799;
		
		while (contador <100)
		{
			Comparendo x= new Comparendo();
			x.asignarObjectid(contador);
			x.asignarLongitud(75);
			x.asignarLatitud(valor);
			
			booty.agregar(x);
			
			contador++;
			valor-= 0.001;
		}
		
	}
	
	//Vacio 
	public void setup2()
	{
		booty= new MaxColaCP<Comparendo>(); 
	}


	@Test
	public void testCola()
	{
		setup2();
		assertEquals("La cola debe estar vacia",true,booty.emptyList());
		setup1();
		assertEquals("La cola no debe estar vacia",false,booty.emptyList());
	}

	@Test
	public void testSize()
	{
		setup1();
		assertEquals("size es 100",100,booty.darSize());

		setup2();
		assertEquals("cola vacia",0,booty.darSize());

	}


	@Test
	public void testDarPrimero()
	{
		setup1();
		assertEquals(99,booty.darPrimerNodo().darObjectid());

		setup2();
		assertEquals("no hay elementos",null,booty.darPrimerNodo());

	}
	


	@Test
	public void testAgregar()
	{
		try{
			setup1();
			Comparendo x = new Comparendo ();
			x.asignarLatitud(4.64);
			x.asignarLongitud(75);
			x.asignarObjectid(300);
			booty.agregar(x);
			
			assertEquals("Error no da el elemento con mayor prioridad",x,booty.darMax());
			assertNotNull("Error", booty.darUltNodo());
			assertNotNull("Error", booty.darPrimerNodo());
			assertNotNull("Error", booty.darMax());
			
			}
		catch (Exception e)
		{
			fail();
		}

	}
	
	@Test
	public void consulta()
	{
		try{
			setup1();
			booty.eliminarMax(); 
			assertEquals("Error no saco el elemento",99,booty.darSize());
			
			}
		catch (Exception e)
		{
			fail();
		}

	}

	
}
