/*
 * Created on 5/05/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sf.incubator;

import junit.framework.TestCase;
import net.sf.incubator.organisms.brain.BrainOrganismFactory;
import net.sf.incubator.organisms.brain.StillBirthException;


/**
 * @author bsutton
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GenePoolTest extends TestCase
{

	public void testGenePool() throws StillBirthException
	{
		GenePool pool = new GenePool(new BrainOrganismFactory(), 10000);
		pool.createInitialGeneration();
		pool.dumpStats();
		pool.dumpPool(false);
		//while (pool.getTotalFitness() < 0x7FFFFFFFFFFFFFFFL)
		while (true) //pool.getMaxFitness() < 310)
		{
			pool.newGeneration();
			pool.dumpStats();
			System.out.println("Fittest Individual:");
			pool.dumpFittest();
			
			//pool.dumpPool();
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
//	public static void main(String[] args)
//	{
//		junit.swingui.TestRunner.run(GenePoolTest.class);
//	}

}
