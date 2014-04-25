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
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IOrganism extends Cloneable, Comparable<IOrganism>
{
	/**
	 * @return
	 */
	DNAStrand getStrand();

	/**
	 * 
	 */
	void dumpDNA();

	/**
	 * 
	 */
	void mutate();

	/**
	 * @return
	 */
	String getName();

	/**
	 * 
	 */
	void dumpStats();

	/**
	 * Request the organism to caculate its own level of fitness.
	 * @throws StillBirthException 
	 *
	 */
	void calculateFitness() throws StillBirthException;
	
	
	/**
	 * Scales the Organisms fitness
	 * 
	 * @param minFitness
	 * @param maxFitness
	 */
	void scaleFitness(long minFitness, long maxFitness);

	/**
	 * @param b
	 * @return
	 */
	long getScaledFitness();
	
	long getRealFitness();
	
	public IOrganism clone();
	
}
