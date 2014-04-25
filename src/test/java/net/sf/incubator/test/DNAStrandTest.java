package net.sf.incubator.test;

import java.util.Arrays;

import junit.framework.TestCase;
import net.sf.incubator.DNAStrand;

public class DNAStrandTest extends TestCase
{

	private static final int SOURCE_SIZE = Integer.SIZE * 3;

	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(DNAStrandTest.class);

	}

	/*
	 * Test method for 'net.sf.Incubator.DNAStrand.mutate()'
	 */
	public void testBitCopy()
	{
		int[] source = new int[]
		{ 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF };// , 0xFFFFFFFF, 0xFFFFFFFF,
												// 0xFFFFFFFF};
		int[] dest = new int[source.length];

//		System.out.println("Moving to");
//		for (int i = 0; i <= SOURCE_SIZE - 1; i++)
//		{
//			for (int j = 0; j < dest.length; j++)
//				dest[j] = 0;
//
//			System.out.println("copying bits " + i + " to bits " + (SOURCE_SIZE -1));
//			DNAStrand.bitCopy(source, dest, i, SOURCE_SIZE -1);
//
//			displayResults(source, dest, i, SOURCE_SIZE - i);
//		}

		System.out.println("Moving from");
		for (int i = 0; i <= SOURCE_SIZE - 1; i++)
		{
			for (int j = 0; j < dest.length; j++)
				dest[j] = 0;

			System.out.println("copying bits " + 0 + " to bits " + (SOURCE_SIZE - 1 - i));
			DNAStrand.bitCopy(source, dest, 0, SOURCE_SIZE - 1 - i);

			displayResults(source, dest, i, SOURCE_SIZE - i);
		}
	}

	private void displayResults(int[] source, int[] dest, int i, int expected)
	{
		if (DNAStrand.getBitCount(dest) != expected)
		{
			// Print out the heading which is a two line array of numbers so we
			// can work out in which
			// column any bit errors occur.
			System.out.print("           0");
			System.out.print("         1");
			System.out.print("         2");
			System.out.print("         3");
			System.out.print("         4");
			System.out.print("         5");
			System.out.print("         6");
			System.out.print("         7");
			System.out.print("         8");
			System.out.println("         9");
			System.out.print("           0123456789");
			System.out.print("0123456789");
			System.out.print("0123456789");
			System.out.print("0123456789");
			System.out.print("0123456789");
			System.out.print("0123456789");
			System.out.print("0123456789");
			System.out.print("0123456789");
			System.out.print("0123456789");
			System.out.println("012345");
			System.out.println("source   : " + toString(source));
			System.out.print("dest   ");
			if (i < 10)
				System.out.print(" ");
			System.out.print(i + ": ");
			System.out.println(toString(dest));
			System.out.println();
			System.out.println("*********** ERROR: Expected bits: " + expected + " actual bits copied ="
					+ DNAStrand.getBitCount(dest) + "*************");
			System.out.println();
		}
	}

	public String toString(int[] strand)
	{
		StringBuffer sb = new StringBuffer();

		char[] pad = new char[32];
		Arrays.fill(pad, '0');
		String padString = new String(pad);

		for (int i = 0; i < strand.length; i++)
		{
			String current = Integer.toBinaryString(strand[i]);
			StringBuffer val = new StringBuffer(padString.substring(0, 32 - current.length()) + current);
			sb.append(val.reverse());
		}

		return sb.toString();
	}

}
