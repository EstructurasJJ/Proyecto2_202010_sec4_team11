package model.logic;

import java.util.Comparator;

public class OrdenarComparendoFecha implements Comparator<Comparendo>
{
	@Override
	public int compare(Comparendo compi1, Comparendo compi2)
	{
		return compi1.darFecha_Hora().compareTo(compi2.darFecha_Hora());
	}
}
