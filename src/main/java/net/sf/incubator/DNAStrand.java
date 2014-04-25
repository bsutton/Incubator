/*
 * Created on 2/04/2005
 * 
 * A strand is a string which represents a dna sequence.
 * 
 */
package net.sf.incubator;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

public class DNAStrand implements Cloneable
{

	// We let the dna dictate how the splicing works by encoding the
	// control factors in the dna.

	static final int DOMAIN = 0x7FFFFFFF;
	public static int MAX_SPLICE_SEGMENT_SIZE_OFFSET = 0;
	public static double MUTATION_RATE = 0.001;
	private static final int MUTATIONS = 1;

	public static int STRAND_LENGTH = 10000;
	public static int WORD_SIZE = Integer.SIZE;
	private int[] strand;
	static private Random rand = new Random(new Date().getTime());

	public Object clone()
	{
		DNAStrand copy = null;
		try
		{
			copy = (DNAStrand) super.clone();
			copy.strand = (int[]) strand.clone();
		}
		catch (CloneNotSupportedException e)
		{
			// should never happen
			throw new RuntimeException(e);
		}
		return copy;
	}

	public DNAStrand()
	{
		strand = new int[STRAND_LENGTH];
		for (int i = 0; i < STRAND_LENGTH; i++)
			strand[i] = rand.nextInt(DOMAIN);
	}

	DNAStrand(int strandLen)
	{
		strand = new int[strandLen];
		for (int i = 0; i < strandLen; i++)
			strand[i] = rand.nextInt(DOMAIN);
	}

	DNAStrand(int[] strand)
	{
		this.strand = strand;
	}

	DNAStrand(DNAStrand mom, DNAStrand dad)
	{
		if (mom.length() != dad.length())
			throw new IllegalArgumentException("strands must be the same length");

		int length = mom.length();
		this.strand = new int[length];
		System.arraycopy(mom.strand, 0, this.strand, 0, length);

		int nMaxSegmentLength = getMaxSpliceSegmentSize();

		int iBitLength = length * WORD_SIZE;
		int iPos = 0;
		while (iPos < iBitLength - 1)
		{
			int iSegmentLen = Math.min(iBitLength - iPos - 1, rand.nextInt(nMaxSegmentLength));

			int iCutTo = iPos + iSegmentLen;

			// System.out.println(iCutTo);

			bitCopy(dad.strand, this.strand, iPos, iCutTo);

			iPos += iSegmentLen;

			// Now skip a section (effectively copy part of mom as we start with
			// her)
			int iSkipTo = Math.min(iBitLength - iPos - 1, rand.nextInt(nMaxSegmentLength));
			iPos += iSkipTo;
		}
	}

	/**
	 * move bits from source to destination. The number of bits to copy begins
	 * at the specified from and extends to the specified 'to' (inclusive). Thus
	 * the number of bits copied is 'to - from + 1'.
	 * 
	 * @param source
	 * @param dest
	 * @param startBit
	 * @param endBit
	 */
	static public void bitCopy(int[] source, int[] dest, int startBit, int endBit)
	{
		// if (startBit == endBit)
		// {
		// // System.out.println("from == to");
		// // Nothing to copy.
		// return;
		// }

		// System.out.println("source=" + source.length);
		// System.out.println("dest=" + dest.length);
		// System.out.println("from =" + from);
		// System.out.println("to =" + to);

		// A bit range is made up of a leading number of bits (less than a word
		// in size)
		// a trailing number of bits less than a word in size
		// and a middle number of bits which are some multiple of words in size.

		int iLeadingSrcWord = source[startBit / WORD_SIZE];
		int iLeadingDestWord = dest[startBit / WORD_SIZE];
		int iTrailingSrcWord = source[endBit / WORD_SIZE];
		int iTrailingDestWord = dest[endBit / WORD_SIZE];

		// to--;

		int lenMiddle = ((endBit / WORD_SIZE - 1) - (startBit / WORD_SIZE + 1)) + 1;

		// Moving the odd leading bits

		// Check if 'startBit' is on a word boundary. If so then there are no
		// leading bits.
		if (startBit % WORD_SIZE != 0)
		{
			// startBit is not on a word boundary so we must have leading bits.

			// move first partial word

			// Start by working out which words we are copying bits
			// endBit/startBit.
			int lastBit = ((endBit / WORD_SIZE) == (startBit / WORD_SIZE) ? (endBit % WORD_SIZE) : WORD_SIZE);

			// If the end bit is on a word boundary we need to go one bit
			// further
			if ((endBit + 1) % WORD_SIZE == 0 && lastBit < WORD_SIZE)
				lastBit++;
			for (int i = startBit % WORD_SIZE; i < lastBit; i++)
			{
				if ((iLeadingSrcWord & 1 << i) == 0)
					iLeadingDestWord &= ~(1 << i); // clear the bit
				else
					iLeadingDestWord |= 1 << i; // set the bit
			}
			dest[startBit / WORD_SIZE] = iLeadingDestWord;
		}
		else
		{
			// No leading bits, but we need to a special copy of the first word
			// or it will be skipped.
			// Unless the start and end bit are in the same word
			// in which case the copy will be dealt with by the end copy logic.
			if (startBit % WORD_SIZE != endBit % WORD_SIZE)
			{
				iLeadingSrcWord = source[startBit / WORD_SIZE];
				dest[startBit / WORD_SIZE] = iLeadingSrcWord;
			}
		}

		// move whole middle words words
		if (lenMiddle > 0)
		{
			int srcPosMiddle = startBit / WORD_SIZE + 1;
			int destPosMiddle = (endBit / WORD_SIZE - 1);
			try
			{
				System.arraycopy(source, srcPosMiddle, dest, destPosMiddle, lenMiddle);
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// no op
			}
		}

		// to++;

		// move last trailing bits
		// Check if 'endBit' is on a word boundary. If so then there are no
		// trailing bits.
		if (endBit % WORD_SIZE != 0)
		{
			// 'endBit' is not on a word boundary so we must have trailing bits.
			// no action required if start and end are in the same word as the
			// work to moving the leading bits
			// would have moved our stuff too.
			// Unless startBit was on a word boundary in which case its up to
			// us.
			if (endBit / WORD_SIZE != startBit / WORD_SIZE || startBit % WORD_SIZE == 0)
			{
				int firstBit = 0;

				for (int i = firstBit; i <= (endBit % WORD_SIZE); i++)
				{

					if ((iTrailingSrcWord & 1 << i) == 0)
						iTrailingDestWord &= ~(1 << i); // clear the bit
					else
						iTrailingDestWord |= 1 << i; // set the bit

				}

				if (endBit % WORD_SIZE == 0)
					endBit--;

				dest[endBit / WORD_SIZE] = iTrailingDestWord;
			}
		}
		else
		{
			// Well actually there is a single trailing bit due to the 'off by
			// one' problem.
			// If the endBit is on a word boundary it is actually the first bit
			// of the next word

			if ((source[startBit / WORD_SIZE] & 1 << 1) == 0)
				dest[endBit / WORD_SIZE] &= ~(1 << 1); // clear the bit
			else
				dest[endBit / WORD_SIZE] |= 1 << 1; // set the bit

		}

	}

	/**
	 * @return
	 */
	public int length()
	{
		return strand.length;
	}

	public boolean equals(DNAStrand rhs)
	{
		return Arrays.equals(this.strand, rhs.strand);
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();

		char[] pad = new char[32];
		Arrays.fill(pad, '0');
		String padString = new String(pad);

		for (int i = 0; i < strand.length; i++)
		{
			String current = Integer.toBinaryString(strand[i]);
			sb.append(padString.substring(0, 32 - current.length()));
			sb.append(current);
		}

		return sb.toString();
	}

	public int getMaxSpliceSegmentSize()
	{
		return 7; // this.strand[MAX_SPLICE_SEGMENT_SIZE_OFFSET];
	}

	public void mutate()
	{
		for (int i = 0; i < MUTATIONS; i++)
		{
			int length = length();
			int iBitLength = length * WORD_SIZE;
			int iMutate = rand.nextInt(iBitLength);
			int iOffset = iMutate / WORD_SIZE;
			int iBit = iMutate % WORD_SIZE;

			// BitSet bsBits = wordToBitSet(strand[iOffset]);

			if ((strand[iOffset] & 1 << iBit) == 1)
				strand[iOffset] |= ~(1 << iBit);
			else
				strand[iOffset] |= (1 << iBit);

			// if (bsBits.get(iBit))
			// bsBits.set(iBit, false);
			// else
			// bsBits.set(iBit, true);
			// strand[iOffset] = bitSetToWord(bsBits);
		}
	}

	/**
	 * 
	 */
	public void dump()
	{
		System.out.println(toString().substring(0, 1000));

	}

	/**
	 * Returns the dna strand as a series of bytes
	 * 
	 * @return
	 */
	public Iterator<UnsignedInteger> iterator()
	{
		return new IntegerIterator(this);
	}

	/**
	 * Returns the dna strand as a series of bytes
	 * 
	 * @return
	 */
	public Iterator<?> shortIterator()
	{
		return new ShortIterator(this);
	}

	public static double getMUTATION_RATE()
	{
		return MUTATION_RATE;
	}

	public static void setMUTATION_RATE(double mutation_rate)
	{
		MUTATION_RATE = mutation_rate;
	}

	public int[] asIntArray()
	{
		return strand;
	}

	/**
	 * Counts the number of bits set to 1
	 * 
	 * @return the count of the bits set to 1.
	 */
	static public int getBitCount(int[] strand)
	{
		int bitCount = 0;

		for (int gene : strand)
		{
			bitCount += Integer.bitCount(gene);
		}
		return bitCount;
	}
}