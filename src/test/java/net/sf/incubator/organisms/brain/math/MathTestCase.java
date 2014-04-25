/**
 * 
 */
package net.sf.incubator.organisms.brain.math;

import java.text.NumberFormat;

import net.sf.incubator.organisms.brain.TestCase;

class MathTestCase implements TestCase
{

	enum Operator
	{
		ADDITION, MULTIPLICATION, DIVISION, SUBTRACTION;
	}

	double		leftOperand;

	double		rightOperand;

	Operator	operator;

	double		result;

	public MathTestCase(double result, double leftOperand, Operator operator,
			double rightOperand)
	{
		super();
		this.result = result;
		this.leftOperand = leftOperand;
		this.operator = operator;
		this.rightOperand = rightOperand;
	}

	public String printExpression()
	{
		NumberFormat nf = NumberFormat.getInstance();
		StringBuffer expression = new StringBuffer();

		expression.append(nf.format(leftOperand));
		
		double result = Double.NaN;

		switch (operator)
		{
		case MULTIPLICATION:
			expression.append(" * ");
			result = leftOperand * rightOperand;
			break;
		case DIVISION:
			expression.append(" / ");
			if (rightOperand != 0.0)
				result = leftOperand / rightOperand;
			break;
		case SUBTRACTION:
			expression.append(" - ");
			result = leftOperand - rightOperand;
			break;
		case ADDITION:
			expression.append(" + ");
			result = leftOperand + rightOperand;
			break;
		}

		expression.append(nf.format(rightOperand));
		expression.append(" = ");
		expression.append(nf.format(result));
		
		return expression.toString();
	}

}