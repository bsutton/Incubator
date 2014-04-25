package net.sf.incubator.organisms.brain;

import org.joone.io.MemoryInputSynapse;


public class MyInputSynapse extends MemoryInputSynapse
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	TestCases				testCases;

	int							currentLine			= 0;

	public MyInputSynapse(TestCases testCases)
	{
		this.testCases = (TestCases) testCases;
		setInputArray(testCases.getInputArray());
	}

	public int size()
	{
		return testCases.size();
	}

}
