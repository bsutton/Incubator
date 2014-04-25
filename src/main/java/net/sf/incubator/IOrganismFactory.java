/*
 * Created on 5/05/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sf.incubator;


/**
 * @author bsutton
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IOrganismFactory
{
	/** creates a single organism from thin air
	 * 
	 * @return a new organism
	 */
	IOrganism create();
	
	/** Creates a new organism by breeding two parents
	 * 
	 * @param mum
	 * @param dad
	 * @return a child of mum and dad
	 */
	IOrganism breed(IOrganism mum, IOrganism dad);
}
