/*
 * Created on 5/05/2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package net.sf.incubator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import net.sf.incubator.organisms.brain.BrainOrganismFactory;
import net.sf.incubator.organisms.brain.StillBirthException;

/**
 * @author bsutton
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class GenePool
{

	private int generation = 0;
	private int poolSize = 0;
	private ArrayList<IOrganism> pool = new ArrayList<IOrganism>();
	private long lTotalFitness = 0;
	private long lTotalScaledFitness = 0;
	private long lMaxFitness = 0;
	private long lMinFitness = Long.MAX_VALUE;
	private long lAvgFitness = 0;
	private int mutant;

	private ArrayList<IOrganism> breedingStock = new ArrayList<IOrganism>();
	static private Random rand = new Random(new Date().getTime());
	private static final double COPYPERCENTAGE = 0.50;
	private static final double BREEDPERCENTAGE = 0.50;

	private final IOrganismFactory factory;

	public GenePool(IOrganismFactory factory, int poolSize)
	{
		this.factory = factory;
		this.poolSize = poolSize;
	}

	public void createInitialGeneration() throws StillBirthException
	{

		for (int i = 0; i < poolSize; i++)
		{
			pool.add(factory.create());
		}
		calculateFitnessLevels();

	}

	// void add(IOrganism organism)
	// {
	// pool.add(organism);
	// poolSize++;
	// }

	public void newGeneration() throws StillBirthException
	{
		System.out.println("Selecting Breeding Stock");
		selectBreedingStock();
		// Clear the pool.
		pool = new ArrayList<IOrganism>();
		System.out.println("Copying Breeding Stock");
		copyStock();
		System.out.println("Breeding Stock");
		breedStock();

		// Check we have a full gene pool. If not then breed some more.
		while (pool.size() < poolSize)
		{
			IOrganism mom = breedingStock.get(rand
					.nextInt(breedingStock.size()));
			IOrganism dad = breedingStock.get(rand
					.nextInt(breedingStock.size()));

			BreedPair first = new BreedPair(mom, dad);
			first.start();
			BreedPair second = new BreedPair(mom, dad);
			second.start();

			try
			{
				first.join();
				second.join();
			}
			catch (InterruptedException e)
			{
				throw new StillBirthException(e);
			}
			pool.add(first.offspring);
			pool.add(second.offspring);
		}
		breedingStock = null;

		System.out.println("Creating Mutant");
		// finally create one mutation in every generation
		mutant = rand.nextInt(poolSize);
		getOrganism(mutant).mutate();
		System.out.println("Calculating Fitness");
		calculateFitnessLevels();
		generation++;
	}

	/**
	 * @throws StillBirthException
	 * @throws InterruptedException
	 * 
	 */
	private void breedStock() throws StillBirthException
	{
		int Quantity = (int) (poolSize * BREEDPERCENTAGE) / 2;

		for (int i = 0; i < Quantity; i++)
		{
			// randomly select the parents
			IOrganism mom = breedingStock.get(rand
					.nextInt(breedingStock.size()));
			IOrganism dad = breedingStock.get(rand
					.nextInt(breedingStock.size()));

			BreedPair first = new BreedPair(mom, dad);
			first.start();
			BreedPair second = new BreedPair(mom, dad);
			second.start();

			try
			{
				first.join();
				second.join();
			}
			catch (InterruptedException e)
			{
				throw new StillBirthException(e);
			}

			pool.add(first.offspring);
			pool.add(second.offspring);

			if (i % 100 == 0 && i > 0)
				System.out.println("Bred: " + i + " of " + Quantity);
		}

	}

	class BreedPair extends Thread
	{
		IOrganism mom;
		IOrganism dad;
		IOrganism offspring;

		BreedPair(IOrganism mom, IOrganism dad)
		{
			this.mom = mom;
			this.dad = dad;
		}

		public void run()
		{
			offspring = factory.breed(mom, dad);
		}
	}

//	private void breedRandomPair()
//	{
//		// randomly select the parents
//		int mom = rand.nextInt(breedingStock.size());
//		int dad = rand.nextInt(breedingStock.size());
//
//		breedPair(mom, dad);
//
//	}

	/**
	 * @param mom
	 * @param dad
	 */
//	private IOrganism breedPair(int mom, int dad)
//	{
//		// Breed a Pair of organisms and return the offspring.
//		IOrganism offspring = factory.breed((IOrganism) breedingStock.get(mom),
//			(IOrganism) breedingStock.get(dad));
//
//		return offspring;
//	}

	private void copyStock()
	{
		int Quantity = (int) (poolSize * COPYPERCENTAGE) - 1;

		// First guarentee that the fittest individual is always
		// copied into the next generation
		pool.add(getFittest(breedingStock));

		for (int i = 0; i < Quantity; i++)
		{
			int source = rand.nextInt(breedingStock.size());
			pool.add(breedingStock.get(source).clone());
		}

	}

	/**
	 * We selecting the breeding stock by selecting more of the most fit
	 * individuals. e.g. the higher the fitness level the more copies of the
	 * individual that will be copied into the next generation.
	 * 
	 */
	private void selectBreedingStock()
	{
		double dDenominator = lTotalScaledFitness / poolSize;
		breedingStock = new ArrayList<IOrganism>();
		while (breedingStock.size() < poolSize)
		{
			for (int i = 0; i < poolSize; i++)
			{
				int copies = (int) ((((IOrganism) pool.get(i))
						.getScaledFitness()) / dDenominator);
				assert copies > 0 && copies < poolSize;
				for (int j = 0; j < copies; j++)
				{
					breedingStock.add(pool.get(i));
					if (breedingStock.size() >= poolSize)
						break;
				}
				if (breedingStock.size() >= poolSize)
					break;
			}
		}

	}

	/**
	 * @throws StillBirthException
	 * 
	 */
	private void calculateFitnessLevels() throws StillBirthException
	{
		lTotalFitness = 0;
		lMaxFitness = 0;
		lMinFitness = Long.MAX_VALUE;
		for (int iCtr = 0; iCtr < poolSize; iCtr += 2)
		{

			CalculateFitness first = new CalculateFitness(pool.get(iCtr));
			first.start();
			CalculateFitness second = new CalculateFitness(pool.get(iCtr + 1));
			second.start();

			try
			{
				first.join();
				second.join();
			}
			catch (InterruptedException e)
			{
				throw new StillBirthException(e);
			}

			if (first.e != null)
				throw first.e;

			if (second.e != null)
				throw second.e;

			lTotalFitness += first.lFitness;
			lMaxFitness = Math.max(lMaxFitness, first.lFitness);
			lMinFitness = Math.min(lMinFitness, first.lFitness);

			lTotalFitness += second.lFitness;
			lMaxFitness = Math.max(lMaxFitness, second.lFitness);
			lMinFitness = Math.min(lMinFitness, second.lFitness);
		}

		// now apply fitness scaling and recalc the max min etc.
		long scaledMin = Long.MAX_VALUE;
		long scaledMax = 0;
		lTotalScaledFitness = 0;

		for (int iCtr = 0; iCtr < poolSize; iCtr++)
		{
			IOrganism organism = pool.get(iCtr);
			organism.scaleFitness(lMinFitness, lMaxFitness);
			long lFitness = organism.getScaledFitness();
			assert lFitness >= 0;
			lTotalScaledFitness += lFitness;
			scaledMax = Math.max(scaledMax, lFitness);
			scaledMin = Math.min(scaledMin, lFitness);
		}

		lAvgFitness = lTotalFitness / poolSize;
	}

	class CalculateFitness extends Thread
	{
		IOrganism organism;
		long lFitness;
		StillBirthException e = null;

		CalculateFitness(IOrganism organism)
		{
			this.organism = organism;
		}

		public void run()
		{
			try
			{
				organism.calculateFitness();
				lFitness = organism.getRealFitness();
			}
			catch (StillBirthException e)
			{
				this.e = e;
			}
		}
	}

	/**
	 * @return
	 */
	public long getTotalFitness()
	{
		return lTotalFitness;
	}

	public void dumpStats()
	{
		System.out.println("Generation [" + generation + "]");
		System.out.println("Individuals=" + this.pool.size());
		System.out.println("Mutant [" + getOrganism(mutant).getName() + "]");
		System.out.println("lTotalFitness= " + lTotalFitness);
		System.out.println("lMaxFitness= " + lMaxFitness);
		System.out.println("lMinFitness= " + lMinFitness);
		System.out.println("lAvgFitness= " + lAvgFitness);

	}

	/**
	 * @param i
	 * @return
	 */
	private IOrganism getOrganism(int individual)
	{
		return (IOrganism) pool.get(individual);
	}

	/**
	 * Either dump all or just the fittest ten When dumping the fittest ten we
	 * don't dump duplicates but just indicate a count
	 * 
	 * @param fittest
	 */
	public void dumpPool(boolean fittest)
	{
		IOrganism[] aOrganisms = new IOrganism[0];
		aOrganisms = pool.toArray(aOrganisms);
		int count = aOrganisms.length;
		if (fittest)
		{
			Arrays.sort(aOrganisms);
			count = (count > 10 ? 10 : count); // we just dump the fitest 10
		}

		IOrganism last = null;
		int occurs = 0;
		for (int i = 0; i < count; i++)
		{
			IOrganism organism = aOrganisms[i];
			if (fittest
					&& last != null
					&& last.getName().compareToIgnoreCase(organism.getName()) == 0)
			{
				occurs++;
				count++;
				if (count > aOrganisms.length)
					count = aOrganisms.length;
				continue;
			}

			organism.dumpDNA();
			if (occurs > 1)
				System.out.println("occurs " + occurs);
			last = organism;
			occurs = 0;
		}
	}

	/**
	 * Check if all of the population members have converged (a bad thing!)
	 */
	public boolean convergant()
	{
		boolean divergent = false;
		for (int i = 0; i < poolSize - 1; i++)
		{
			if (pool.get(i).getScaledFitness() != pool.get(i + 1)
					.getScaledFitness())
			{
				divergent = true;
				break;
			}
		}
		return !divergent;
	}

	/**
	 * @return
	 */
	public long getMaxFitness()
	{
		return lMaxFitness;
	}

	/**
	 * 
	 */
	public void dumpFittest()
	{

		IOrganism fittest = getFittest(pool);
		fittest.dumpStats();

	}

	/**
	 * @param organisms
	 * 
	 */
	public IOrganism getFittest(ArrayList<IOrganism> organisms)
	{
		IOrganism fittest = null;

		for (int i = 0; i < organisms.size() - 1; i++)
		{
			IOrganism current = ((IOrganism) organisms.get(i));

			if (fittest == null
					|| current.getScaledFitness() > fittest.getScaledFitness())
			{
				fittest = current;
			}
		}
		return fittest;

	}

	static public void main(String[] args)
	{
		try
		{
			// System.Process
			Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
			GenePool pool = new GenePool(new BrainOrganismFactory(), 10);
			pool.createInitialGeneration();
			pool.dumpPool(false);
			pool.dumpStats();
			// pool.dumpPool();
			// while (pool.getTotalFitness() < 0x7FFFFFFFFFFFFFFFL)
			while (true) // pool.getMaxFitness() < 310)
			{
				pool.newGeneration();
				pool.dumpPool(false);
				pool.dumpStats();
				System.out.println("Fittest Individual:");
				pool.dumpFittest();

				pool.dumpPool(true);
				if (pool.convergant())
				{
					System.out.println("convergent");
					pool.dumpStats();
					break;
				}
			}
			pool.dumpPool(false);
			pool.dumpStats();
		}
		catch (StillBirthException e)
		{
			e.printStackTrace();
		}
	}
}