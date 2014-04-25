/*
 * Created on 5/05/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sf.incubator.organisms.brain.math;

import net.sf.incubator.IOrganism;
import net.sf.incubator.IOrganismFactory;
import net.sf.incubator.organisms.brain.BrainOrganism;

/**
 * @author bsutton
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class MathBrainOrganismFactory implements IOrganismFactory
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.Incubator.IOrganismFactory#factory()
	 */
	public IOrganism create()
	{
		return new MathBrainOrganism(MathTestCases.standardTestCases);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.Incubator.IOrganismFactory#breed(net.sf.Incubator.IOrganism,
	 *      net.sf.Incubator.IOrganism)
	 */
	public IOrganism breed(IOrganism mum, IOrganism dad)
	{
		return new BrainOrganism(mum, dad);
	}

}
