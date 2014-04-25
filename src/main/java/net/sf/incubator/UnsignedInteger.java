package net.sf.incubator;

public class UnsignedInteger extends Number
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//	The maximum size of an unsigned byte.    
	public static final long MAX_UNSIGNED_VALUE	= (1 << 16) - 1;
	public static final long BASE	= (1 << 16);
	

	private int				value;

	/**
	 * @param bOn
	 */
	public UnsignedInteger(boolean bOn)
	{
		value = (bOn == true) ? (byte) 1 : (byte) 0;
	}


	// Constructors

	/**
	 *Contructs a new UnsignedInteger and intializes its value to 0.
	 */
	public UnsignedInteger()
	{
		value = 0;
	}

	/**
	 *Constructs an UnsignedInteger from a short,
	 *throws an exception if the parameter is out of range.
	 *
	 *@param bytData >=0, <=MAX_UNSIGNED_VALUE
	 *@exception IllegalArgumentException if the parameter is out of range
	 */
	public UnsignedInteger(short bytData)
	{
		if ((bytData < 0) || (bytData > MAX_UNSIGNED_VALUE))
			throw new IllegalArgumentException(
					"Passed value beyond maximum range of an unsigned integer.");

		value = (int) bytData;
	}

	/**
	 *Constructs an UnsignedInteger from an int,
	 *throws an exception if the parameter is out of range.
	 *
	 *@param bytData >=0, <=MAX_UNSIGNED_VALUE
	 *@exception IllegalArgumentException if bytData is out of range
	 */
	public UnsignedInteger(int bytData)
	{
		if ((bytData < 0) || (bytData > MAX_UNSIGNED_VALUE))
			throw new IllegalArgumentException(
					"Passed value beyond maximum range of an unsigned integer." + bytData);

		value = (int) bytData;
	}

	// Overrides for the abstract class Number

	/**
	 * @param header
	 */
	public UnsignedInteger(UnsignedInteger rhs)
	{
		this.value = rhs.value;
	}

	/**
	 *Returns the current value as a double float. 
	 *
	 *@return the current value as a double float
	 */
	public double doubleValue()
	{
		// if _data is negative, we know we've overflowed the high bit.
		// If that's the case, return the "real" unsigned value.

		double ret;

		ret = (value < 0 ? BASE + value : value);
		return ret;
	}

	/**
	 *Returns a the current value as a float.
	 *
	 *@return the current value as a float.
	 */
	public float floatValue()
	{
		float ret;

		ret = (value < 0 ? BASE + value : value);
		return ret;
	}

	/**
	 *Returns the current value as an int.
	 *
	 *@return the current value as an int
	 */
	public int intValue()
	{
		return (int)(value < 0 ? BASE + value : value);
	}

	/**
	 *Returns the current value as a long.
	 *
	 *@return the current value as a long
	 */
	public long longValue()
	{
		long ret;

		ret = (value < 0 ? BASE + value : value);
		return ret;
	}

	public String toString()
	{
		return Integer.toString(intValue());
	}

	public String dump()
	{
		//		Integer ret = new Integer(this.intValue());
		//		return ret.toString();
		String ret;
		ret = "" + ((value & 0x80) >> 0x07) + "," + ((value & 0x40) >> 0x06) + ","
				+ ((value & 0x20) >> 0x05) + "," + ((value & 0x10) >> 0x04) + ","
				+ ((value & 0x08) >> 0x03) + "," + ((value & 0x04) >> 0x02) + ","
				+ ((value & 0x02) >> 0x01) + "," + ((value & 0x01) >> 0x00) + " = "
				+ intValue();

		return ret;
	}

	public boolean equals(Object _rhs)
	{
		boolean equals = false;

		UnsignedInteger rhs = (UnsignedInteger) _rhs;

		equals = (this.value == rhs.value);
		return equals;
	}

}