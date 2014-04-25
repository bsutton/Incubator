package net;

public class PhoneNumbers
{
	static class Map
	{
		public Map(int no, String seq)
		{
			this.no = no;
			this.seq = seq;
		}
		int no;
		String seq;
		
	}
	
	static Map[] map = new Map[] {
	new Map(2, "ABC")
	,new Map(3, "DEF")
	,new Map(4, "GHI")
	,new Map(5, "JKL")
	,new Map(6, "MNO")
	,new Map(7, "PQRS")
	,new Map(8, "TUV")
	,new Map(9, "WXYZ")
	};
	
	public static void main(String [] args)
	{
		String base = "NOO";
		
		for (int i = 222; i < 1000; i++)
		{
			char[] seq1 = map(i / 100);
			char[]  seq2 = map(i / 10 % 10);
			char[]  seq3 = map(i % 10);
			if (seq1 != null && seq2 != null && seq3 != null)
			for (char j : seq1)
			{
				for (char k : seq2)
				{
					for (char l : seq3)
					{
						System.out.print(base + j + k + l);
						System.out.print(" 666" + i);
						System.out.println("");
					}
				}
			}
		}
	}

	private static char[] map(int i)
	{
		char[] ret = null;
		if (i >= 2 && i <= 9)
				ret = map[i - 2].seq.toCharArray();
		return ret;
	}
}
