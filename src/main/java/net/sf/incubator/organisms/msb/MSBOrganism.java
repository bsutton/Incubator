/*
 * Created on 5/05/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sf.incubator.organisms.msb;

import net.sf.incubator.DNAStrand;
import net.sf.incubator.IOrganism;
import net.sf.incubator.Organism;

/**
 * @author bsutton
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MSBOrganism extends Organism implements IOrganism
{
	/**
	 * 
	 */
	public MSBOrganism()
	{
	
	}

	/**
	 * @param mum
	 * @param dad
	 */
	public MSBOrganism(IOrganism mum, IOrganism dad)
	{
		super(mum, dad);
	}

	/* (non-Javadoc)
	 * @see net.sf.Incubator.IOrganisim#fitness()
	 */
	public long realFitness()
	{
		// does a bit count
		realFitness = 0;
		for (int iCtr = 0; iCtr < strand.length(); iCtr++)
		{
			int word= strand.asIntArray()[iCtr];
			for (int i = 0; i < DNAStrand.WORD_SIZE; i++)
			{
				if ((word & 1 << i) == 1)
					realFitness++;
			}
//			BitSet bs = DNAStrand.wordToBitSet(strand.asIntArray()[iCtr]);
//			for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i + 1))
//			{
//				realFitness++;
//			}
		}

		return realFitness;
	}

	/* (non-Javadoc)
	 * @see net.sf.Incubator.IOrganism#fitness(boolean)
	 */
	public long scaledFitness(boolean reset)
	{
		if (reset)
			scaledFitness = -1;
		return scaledFitness();
	}

	/* (non-Javadoc)
	 * @see net.sf.Incubator.IOrganism#fitness(boolean)
	 */
	public long scaledFitness()
	{
		return scaledFitness;
	}

	
	/* (non-Javadoc)
	 * @see net.sf.Incubator.IOrganisim#fitness()
	 */
	public long fitnessZero()
	{
		scaledFitness = 0;
		for (int iCtr = 0; iCtr < strand.length(); iCtr++)
			scaledFitness += strand.asIntArray()[iCtr];

		return (Long.MAX_VALUE / 10000) - scaledFitness;
	}

	/* (non-Javadoc)
	 * @see net.sf.Incubator.IOrganisim#fitness()
	 */
	public long fitnessMAXLONG()
	{
		scaledFitness = 0;
		for (int iCtr = 0; iCtr < strand.length(); iCtr++)
			scaledFitness += strand.asIntArray()[iCtr];

		return scaledFitness;
	}

	/* (non-Javadoc)
	 * @see net.sf.Incubator.IOrganism#dumpStats()
	 */
	public void dumpStats()
	{
		// TODO Auto-generated method stub
		
	}



}