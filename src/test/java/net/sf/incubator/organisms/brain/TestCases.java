package net.sf.incubator.organisms.brain;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class TestCases
{
	ArrayList<TestCase> testCases = new ArrayList<TestCase>();
	
	protected void add(TestCase testCase)
	{
		testCases.add(testCase);
	}
	
	public Iterator<TestCase> iterator()
	{
		
		return testCases.iterator();
	}

	public int size()
	{
		return testCases.size();
	}

	abstract public double[][] getInputArray();
}
