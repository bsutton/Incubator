/*
 * Created on 19/04/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sf.incubator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import junit.framework.TestCase;

import org.junit.Assert;


/**
 * @author bsutton
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StrandTest extends TestCase
{

//	public static void main(String[] args)
//	{
//		junit.swingui.TestRunner.run(StrandTest.class);
//	}

	/*
	 * Class under test for void Strand()
	 */
	public void testStrand()
	{
		DNAStrand strand = new DNAStrand();
		Assert.assertEquals(strand.length(), DNAStrand.STRAND_LENGTH);
		
		//test that the strings arn't random
		DNAStrand strand2 = new DNAStrand();
		Assert.assertEquals(false, strand.equals(strand2));
	}

	/*
	 * Class under test for void Strand(byte[])
	 */
	public void testStrandbyteArray()
	{
		
	}

	/*
	 * Class under test for void Strand(Strand, Strand)
	 */
	public void testStrandStrandStrand()
	{
		// Test that the child is different from the parents
		DNAStrand mom = new DNAStrand();
		DNAStrand dad = new DNAStrand();
		DNAStrand child = new DNAStrand(mom, dad);
		Assert.assertEquals(false, mom.equals(child));
		Assert.assertEquals(false, dad.equals(child));
		
		
		// Test the bit splice operates at a single point
		int len = 10;
	
		int[] baMom = new int[len];
		Arrays.fill(baMom, 0x00);

		int[] baDad = new int[len];
		Arrays.fill(baDad, 0x7FFFFFFF);

		
		mom = new DNAStrand(10);
		dad = new DNAStrand(10);
		child = new DNAStrand(mom, dad);
		System.out.println(mom.toString());
		System.out.println(dad.toString());
		System.out.println(child.toString());

		//Assert.assertEquals(false, mom.equals(child));
		//Assert.assertEquals(false, dad.equals(child));

		// check that mom is all zeros
//		char[] momExpected = new char[len * Strand.WORD_SIZE];
//		Arrays.fill(momExpected, '0');
//		Assert.assertEquals(new String(momExpected), mom.toString());
		
		// check that dad is all zeros
//		char[] dadExpected = new char[len * Strand.WORD_SIZE];
//		Arrays.fill(dadExpected, '1');
//		Assert.assertEquals(new String(dadExpected), dad.toString());
		
		// now start breeding
		ArrayList<DNAStrand> breedingStock = new ArrayList<DNAStrand>();
		for (int i = 0; i < 100; i++)
		{
			breedingStock.add(new DNAStrand(10));
			System.out.println(breedingStock.get(breedingStock.size()-1));

		}
		Random	rand							= new Random(new Date().getTime());
		
		
		for (int i = 0; i < 100000; i++)
		{
			// select two individuals to breed
			// with within the last 20% of children
			int iBreedDad = rand.nextInt(breedingStock.size()/5);
			int iBreedMom = rand.nextInt(breedingStock.size()/5);
			iBreedDad = (breedingStock.size()-1) - iBreedDad;
			iBreedMom = (breedingStock.size()-1) - iBreedMom;
			mom = (DNAStrand)breedingStock.get(iBreedMom);
			//rand.nextInt(breedingStock.size()));
			dad = (DNAStrand)breedingStock.get(iBreedDad);
			//rand.nextInt(breedingStock.size()));
			child = new DNAStrand(mom, dad);
			breedingStock.add(child);
			System.out.println(child.toString());
		}
 
	}

}
