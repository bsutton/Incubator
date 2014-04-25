/*
 * Created on 11/05/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sf.incubator;

import java.util.Iterator;

/**
 * @author bsutton
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ByteIterator implements Iterator<Object>
{

	DNAStrand	strand;
	int		index;

	/**
	 * @param strand
	 */
	public ByteIterator(DNAStrand strand)
	{
		this.strand = strand;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	public void remove()
	{
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext()
	{
		 return index / 4 < strand.asIntArray().length;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public Object next()
	{
		int intValue = strand.asIntArray()[index / 4];
		return getByte(intValue, index++);
	}

	/**
	 * @param intValue
	 * @return
	 */
	public static Object getByte(int intValue, int index)
	{
		UnsignedByte byteValue = null;
		switch (index % 4)
		{
			case 0 :
				byteValue = new UnsignedByte((intValue >> 24) % 256);
				break;
			case 1 :
				byteValue = new UnsignedByte((intValue >> 16) % 256);
				break;
			case 2 :
				byteValue = new UnsignedByte((intValue >> 8) % 256);
				break;
			case 3 :
				byteValue = new UnsignedByte(intValue % 256);
				break;
		}

		return byteValue;
	}

}