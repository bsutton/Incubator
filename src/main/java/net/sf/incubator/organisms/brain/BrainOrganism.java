/*
 * Created on 13/05/2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package net.sf.incubator.organisms.brain;

import net.sf.incubator.IOrganism;
import net.sf.incubator.Organism;

/**
 * @author bsutton
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class BrainOrganism extends Organism implements IOrganism
{
	int layers = -1;

	int connectedLayers = -1;

	TestCases testCases;

	protected BrainOrganism(TestCases testCases)
	{
		super(2000);
		this.testCases = testCases;
	}

	/**
	 * @param mum
	 * @param dad
	 */
	public BrainOrganism(IOrganism mum, IOrganism dad)
	{
		super(mum, dad);
		this.testCases = ((BrainOrganism)dad).testCases;
	}

	public IOrganism clone()
	{
		BrainOrganism copy = null;
		copy = (BrainOrganism) super.clone();
		// copy.brain = (Brain) brain.clone();
		return copy;
	}

	protected Brain getBrain() throws StillBirthException 
	{
		Brain brain = null;
		BrainGestator gestator = new BrainGestator();
		try
		{
			brain = gestator.gestate(strand, testCases);
		}
		catch (InstantiationException e)
		{
			throw new StillBirthException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new StillBirthException(e);
			}
		return brain;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.Incubator.IOrganism#fitness()
	 */
	public void calculateFitness() throws StillBirthException
	{
		if (this.realFitness == -1)
		{
			super.calculateFitness();
			Brain brain = getBrain();
			// layers = brain.getLayerCount();
			connectedLayers = brain.getConnectedLayerCount();
			// fitness = layers + connectedLayers * 10;
			// fitness = layers;
			realFitness = connectedLayers;
			assert realFitness >= 0 : "Invalid fitness level";
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.Incubator.IOrganism#dumpStats()
	 */
	public void dumpStats()
	{
		// make certain that the fitness is know
		try
		{
			calculateFitness();
			System.out.println("Organism statistics: name=" + this.getName());
			System.out.println("Neuron Layers=" + layers);
			System.out.println("Neuron Layers[connected]=" + connectedLayers);
			strand.dump();
		}
		catch (StillBirthException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
