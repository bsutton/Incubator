/*
 * Created on 11/05/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sf.incubator;

import junit.framework.TestCase;
import net.sf.incubator.ByteIterator;
import net.sf.incubator.UnsignedByte;


/**
 * @author bsutton
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ByteIteratorTest extends TestCase
{
	public static void testGetByte()
	{
		int value = 0x0001;
		UnsignedByte result;
		result = (UnsignedByte)ByteIterator.getByte(value, 0);
		assertTrue(result.intValue() == 0x0);
		result = (UnsignedByte)ByteIterator.getByte(value, 1);
		assertTrue(result.intValue() == 0x0);
		result = (UnsignedByte)ByteIterator.getByte(value, 2);
		assertTrue(result.intValue() == 0x0);
		result = (UnsignedByte)ByteIterator.getByte(value, 3);
		assertTrue(result.intValue() == 0x1);

		value = 0x0100;
		result = (UnsignedByte)ByteIterator.getByte(value, 0);
		assertTrue(result.intValue() == 0x0);
		result = (UnsignedByte)ByteIterator.getByte(value, 1);
		assertTrue(result.intValue() == 0x0);
		result = (UnsignedByte)ByteIterator.getByte(value, 2);
		assertTrue(result.intValue() == 0x1);
		result = (UnsignedByte)ByteIterator.getByte(value, 3);
		assertTrue(result.intValue() == 0x0);

		value = 0x7F7F;
		result = (UnsignedByte)ByteIterator.getByte(value, 0);
		assertTrue(result.intValue() == 0x0);
		result = (UnsignedByte)ByteIterator.getByte(value, 1);
		assertTrue(result.intValue() == 0x0);
		result = (UnsignedByte)ByteIterator.getByte(value, 2);
		assertTrue(result.intValue() == 0x7F);
		result = (UnsignedByte)ByteIterator.getByte(value, 3);
		assertTrue(result.intValue() == 0x7F);

		value = 0xFFFF;
		result = (UnsignedByte)ByteIterator.getByte(value, 0);
		assertTrue(result.intValue() == 0x0);
		result = (UnsignedByte)ByteIterator.getByte(value, 1);
		assertTrue(result.intValue() == 0x0);
		result = (UnsignedByte)ByteIterator.getByte(value, 2);
		assertTrue(result.intValue() == 0xFF);
		result = (UnsignedByte)ByteIterator.getByte(value, 3);
		assertTrue(result.intValue() == 0xFF);

		value = 0x7FFF0000;
		result = (UnsignedByte)ByteIterator.getByte(value, 0);
		assertTrue(result.intValue() == 0x7F);
		result = (UnsignedByte)ByteIterator.getByte(value, 1);
		assertTrue(result.intValue() == 0xFF);
		result = (UnsignedByte)ByteIterator.getByte(value, 2);
		assertTrue(result.intValue() == 0x00);
		result = (UnsignedByte)ByteIterator.getByte(value, 3);
		assertTrue(result.intValue() == 0x00);

	}


}
