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
public class ShortIterator implements Iterator<Short>
{

	private static final int	MAX_INT	= 1 << 16;
	int[]						strand;
	int							index;

	/**
	 * @param strand
	 */
	public ShortIterator(DNAStrand strand)
	{
		this.strand = strand.asIntArray();
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
		return index / 2 < strand.length;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public Short next()
	{
		int intValue = strand[index / 2];
		Short value = null;
		switch (index % 2)
		{
			case 0 :
				value = new Short((short)((intValue >> 16) % (MAX_INT)));
				break;
			case 1 :
				value = new Short((short)(intValue % (MAX_INT)));
				break;
		}
		index++;
		return value;
	}

}