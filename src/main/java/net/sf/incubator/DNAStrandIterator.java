package net.sf.incubator;

import java.util.Iterator;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class DNAStrandIterator implements Iterator<Object>
{
	int index = 0;
	DNAStrand dna;
	
	public DNAStrandIterator(DNAStrand dna)
	{
		this.dna = dna;
	}
	
	/**
	 * Returns the value stored in a dna strand at the given index. The strand
	 * is an array of integers but we process it as a sequence of shorts.
	 * 
	 * @param dna
	 * @param index
	 * @return
	 */
	public short getValue()
	{
		short value = 0;

		// Determine if we need the upper or lower short.
		switch (index % 2)
		{
		// upper short
		case 0:
			value = (short) ((dna.asIntArray()[index / 2] >> 16) % (Short.MAX_VALUE));
			break;
		// lower short
		case 1:
			value = (short) (dna.asIntArray()[index / 2] % (Short.MAX_VALUE));
			break;
		}
		index++;
		return value;
	}

	/**
	 * Returns the value stored in a dna strand at the given index as a float
	 * Unlike getValue this method uses the full int and can only 
	 * operate at even boundary points (e.g. index must be divisible by two with no remander). 
	 * 
	 * 
	 * @param dna
	 * @param index
	 * @return
	 */
	public float getValueAsFloat()
	{
		if (index % 2 != 0)
			throw new IllegalArgumentException("index must be divisible by two");
		
		float value = Float.intBitsToFloat(dna.asIntArray()[index / 2]);
		index+=2;
		return value;
	}

	public boolean hasNext()
	{
		return index < dna.length() * 2;
	}

	public Short next()
	{
		return getValue();
	}

	public void remove()
	{
		throw new NotImplementedException();
		
	}

	public int position()
	{
		return index;
	}


}
