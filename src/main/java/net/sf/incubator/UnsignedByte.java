package net.sf.incubator;

public class UnsignedByte extends Number
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//	The maximum size of an unsigned byte.    
	public static final short	MAX_UNSIGNED_VALUE	= 255;

	private byte				m_bytData;

	/**
	 * @param bOn
	 */
	public UnsignedByte(boolean bOn)
	{
		m_bytData = (bOn == true) ? (byte) 1 : (byte) 0;
	}


	// Constructors

	/**
	 *Contructs a new UnsignedByte and intializes its value to 0.
	 */
	public UnsignedByte()
	{
		m_bytData = 0;
	}

	/** 
	 *Constructs an UnsignedByte from a signed byte.
	 *throws an exception if the parameter is out of range. 
	 *
	 *@param bytData >=0
	 *@exception IllegalArgumentException if bytData is out of range
	 */
	public UnsignedByte(byte bytData)
	{
		// Accept negative values 
		if (bytData < -128)
			throw new IllegalArgumentException(
					"Passed value beyond maximum range of an unsigned byte.");
		m_bytData = bytData;
	}

	/**
	 *Constructs an UnsignedByte from a short,
	 *throws an exception if the parameter is out of range.
	 *
	 *@param bytData >=0, <=MAX_UNSIGNED_VALUE
	 *@exception IllegalArgumentException if the parameter is out of range
	 */
	public UnsignedByte(short bytData)
	{
		if ((bytData < 0) || (bytData > MAX_UNSIGNED_VALUE))
			throw new IllegalArgumentException(
					"Passed value beyond maximum range of an unsigned byte.");

		m_bytData = (byte) bytData;
	}

	/**
	 *Constructs an UnsignedByte from an int,
	 *throws an exception if the parameter is out of range.
	 *
	 *@param bytData >=0, <=MAX_UNSIGNED_VALUE
	 *@exception IllegalArgumentException if bytData is out of range
	 */
	public UnsignedByte(int bytData)
	{
		if ((bytData < 0) || (bytData > MAX_UNSIGNED_VALUE))
			throw new IllegalArgumentException(
					"Passed value beyond maximum range of an unsigned byte.");

		m_bytData = (byte) bytData;
	}

	// Overrides for the abstract class Number

	/**
	 * @param header
	 */
	public UnsignedByte(UnsignedByte rhs)
	{
		this.m_bytData = rhs.m_bytData;
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

		ret = (m_bytData < 0 ? MAX_UNSIGNED_VALUE + m_bytData + 1 : m_bytData);
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

		ret = (m_bytData < 0 ? MAX_UNSIGNED_VALUE + m_bytData + 1 : m_bytData);
		return ret;
	}

	/**
	 *Returns the current value as an int.
	 *
	 *@return the current value as an int
	 */
	public int intValue()
	{
		int ret;

		ret = (m_bytData < 0 ? MAX_UNSIGNED_VALUE + m_bytData + 1 : m_bytData);
		return ret;
	}

	/**
	 *Returns the current value as a long.
	 *
	 *@return the current value as a long
	 */
	public long longValue()
	{
		long ret;

		ret = (m_bytData < 0 ? MAX_UNSIGNED_VALUE + m_bytData + 1 : m_bytData);
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
		ret = "" + ((m_bytData & 0x80) >> 0x07) + "," + ((m_bytData & 0x40) >> 0x06) + ","
				+ ((m_bytData & 0x20) >> 0x05) + "," + ((m_bytData & 0x10) >> 0x04) + ","
				+ ((m_bytData & 0x08) >> 0x03) + "," + ((m_bytData & 0x04) >> 0x02) + ","
				+ ((m_bytData & 0x02) >> 0x01) + "," + ((m_bytData & 0x01) >> 0x00) + " = "
				+ intValue();

		return ret;
	}

	public boolean equals(Object _rhs)
	{
		boolean equals = false;

		UnsignedByte rhs = (UnsignedByte) _rhs;

		equals = (this.m_bytData == rhs.m_bytData);
		return equals;
	}

}