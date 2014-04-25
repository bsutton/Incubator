package net.sf.incubator.organisms.brain.math;

import net.sf.incubator.organisms.brain.Brain;
import net.sf.incubator.organisms.brain.BrainOrganism;
import net.sf.incubator.organisms.brain.StillBirthException;
import net.sf.incubator.organisms.brain.TestCases;



public class MathBrainOrganism extends BrainOrganism
{

	public MathBrainOrganism(TestCases testCases)
	{
		super(testCases);
	}

	@Override
	public void calculateFitness() throws StillBirthException
	{
		super.calculateFitness();
		Brain brain = this.getBrain();

		brain.run();

		// TODO check the results and set the fitness measure
	}

}
