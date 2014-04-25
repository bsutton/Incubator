package net.sf.incubator.organisms.brain.math;

import java.util.Iterator;

import net.sf.incubator.organisms.brain.TestCase;
import net.sf.incubator.organisms.brain.TestCases;
import net.sf.incubator.organisms.brain.math.MathTestCase.Operator;

public class MathTestCases extends TestCases
{
	public static MathTestCases		standardTestCases	= new MathTestCases();

	private static final int	TEST_DOMAIN			= 10;

	static MathTestCases		simpleTestCases		= new MathTestCases();

	private double[][]			expressions;

	private MathTestCases()
	{
	}

	@Override
	public double[][] getInputArray()
	{
		if (expressions == null)
		{
			expressions = new double[this.size()][4];
			int row = 0;
			for (Iterator<TestCase> iter = this.iterator(); iter.hasNext();)
			{
				MathTestCase testCase = (MathTestCase) iter.next();

				expressions[row][0] = testCase.leftOperand;
				expressions[row][1] = (double) testCase.operator.ordinal();
				expressions[row][2] = testCase.rightOperand;
				expressions[row][3] = testCase.result;

				row++;
			}
		}
		return expressions;

	}

	static
	{
		// Create set of static test cases

		// Multiplcation
		for (int i = 0; i < TEST_DOMAIN; i++)
		{
			int leftOperand = i;
			for (int j = 0; j < TEST_DOMAIN; j++)
			{
				int rightOperand = j;
				double result = leftOperand * rightOperand;

				standardTestCases.add(new MathTestCase(result, leftOperand,
						Operator.MULTIPLICATION, rightOperand));
			}
		}

		// simple test cases in the range 0.1 to 1
		float rightOperand = 0.1F;

		for (float left = 0; left < TEST_DOMAIN; left += 0.1)
		{
			float leftOperand = left;
			double result = leftOperand * rightOperand;

			simpleTestCases.add(new MathTestCase(result, leftOperand,
					Operator.MULTIPLICATION, rightOperand));
		}
/*
		// simple test cases in the range 1 to 10
		rightOperand = 1F;

		for (float left = 0; left < TEST_DOMAIN; left += 1)
		{
			float leftOperand = left;
			double result = leftOperand * rightOperand;

			simpleTestCases.add(new MathTestCase(result, leftOperand,
					Operator.MULTIPLICATION, rightOperand));
		}

		// Division
		for (int i = 0; i < TEST_DOMAIN; i++)
		{
			int leftOperand = i;
			for (int j = 0; j < TEST_DOMAIN; j++)
			{
				rightOperand = j;
				double result = leftOperand * rightOperand;

				simpleTestCases.add(new MathTestCase(result, leftOperand,
						Operator.DIVISION, rightOperand));
			}
		} // addition
		for (int i = 0; i < TEST_DOMAIN; i++)
		{
			int leftOperand = i;
			for (int j = 0; j < TEST_DOMAIN; j++)
			{
				rightOperand = j;
				double result = leftOperand + rightOperand;

				simpleTestCases.add(new MathTestCase(result, leftOperand,
						Operator.ADDITION, rightOperand));
			}
		}
		// subtraction
		for (int i = 0; i < TEST_DOMAIN; i++)
		{
			int leftOperand = i;
			for (int j = 0; j < TEST_DOMAIN; j++)
			{
				rightOperand = j;
				double result = leftOperand - rightOperand;

				simpleTestCases.add(new MathTestCase(result, leftOperand,
						Operator.SUBTRACTION, rightOperand));
			}
		}
		*/

	}

}
