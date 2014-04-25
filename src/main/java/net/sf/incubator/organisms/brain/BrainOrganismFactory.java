/*
 * Created on 5/05/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sf.incubator.organisms.brain;

import net.sf.incubator.IOrganism;
import net.sf.incubator.IOrganismFactory;
import net.sf.incubator.organisms.brain.math.MathTestCases;


/**
 * @author bsutton
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BrainOrganismFactory implements IOrganismFactory
{

	/* (non-Javadoc)
	 * @see net.sf.Incubator.IOrganismFactory#factory()
	 */
	public IOrganism create()
	{
		return new BrainOrganism(MathTestCases.standardTestCases);
	}

	/* (non-Javadoc)
	 * @see net.sf.Incubator.IOrganismFactory#breed(net.sf.Incubator.IOrganism, net.sf.Incubator.IOrganism)
	 */
	public IOrganism breed(IOrganism mum, IOrganism dad)
	{
		return new BrainOrganism(mum, dad);
	}

	
}
