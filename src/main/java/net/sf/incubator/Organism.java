/*
 * Created on 5/05/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sf.incubator;

import net.sf.incubator.organisms.brain.StillBirthException;

/**
 * @author bsutton
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
abstract public class Organism implements IOrganism
{

	static int				DNA_SIZE		= 2000;

	static int				seed			= 0;

	protected final String	name;

	protected DNAStrand		strand;

	protected long			scaledFitness	= -1;

	protected long			realFitness		= -1;

	private boolean	scaled = false;

	public IOrganism clone()
	{
		Organism copy = null;
		try
		{
			copy = (Organism) super.clone();
			copy.strand = (DNAStrand) this.strand.clone();
		}
		catch (CloneNotSupportedException e)
		{
			// should never happen
			throw new RuntimeException(e);
		}
		return copy;
	}

	protected Organism()
	{
		name = genName();
		// System.out.println("Creating " + name);
		strand = new DNAStrand(DNA_SIZE);

	}

	protected Organism(int dnaSize)
	{
		name = genName();
		strand = new DNAStrand(dnaSize);

	}

	/**
	 * @param mum
	 * @param dad
	 */
	public Organism(IOrganism mum, IOrganism dad)
	{
		name = genName();
		strand = new DNAStrand(mum.getStrand(), dad.getStrand());

	}

	public String getName()
	{
		return name;
	}

	static String genName()
	{
		return new Integer(seed++).toString();

	}

	public DNAStrand getStrand()
	{
		return strand;
	}

	public void dumpDNA()
	{
		System.out.print("[" + getName() + ", " + getRealFitness() + "] ");
		strand.dump();

	}

	public void mutate()
	{
		strand.mutate();

	}

	/**
	 * @param minFitness
	 * @param maxFitness
	 */
	public void scaleFitness(long minFitness, long maxFitness)
	{
		assert minFitness <= this.scaledFitness;

		if (!scaled )
		{
			this.scaledFitness = realFitness - minFitness;
			scaled = true;
		}
		assert this.scaledFitness >= 0;

	}

	public long getScaledFitness()
	{
		return scaledFitness;
	}

	public long getRealFitness()
	{
		return realFitness;
	}

	/**
	 * Call this method when the current fitness measure is no longer valid
	 * and needs to be recalculated.
	 *
	 */
	protected void resetFitness()
	{
		realFitness = -1;
		scaledFitness = -1;
	}
		
	/**
	 * Over load this function to allow your organism to calculate 
	 * its own fitness.
	 * 
	 * This function by default just resets the scaled flag so you must
	 * overload it and you must call it i.e. super.calculateFitness()
	 * @throws StillBirthException 
	 */
	public void calculateFitness() throws StillBirthException
	{
		scaled = false;
	}

	
	public int compareTo(IOrganism o)
	{
		long diff = (o.getRealFitness() - getRealFitness());
		if (diff == 0)
			diff = o.getName().compareToIgnoreCase(getName());
		return (int) diff;
	}

}