package com.github.vprimachenko.colorcoder.util

import java.util.zip.Checksum

class CRC8(polynomial: Int, private val init: Int) : Checksum {
    private val crcTable = ByteArray(256)
    private var value: Byte

    /**
     * Updates the current checksum with the specified array of bytes.
     * Equivalent to calling `update(buffer, 0, buffer.length)`.
     * @param buffer the byte array to update the checksum with
     */
    override fun update(buffer: ByteArray, offset: Int, len: Int) {
        for (i in 0 until len) {
            val data = buffer[offset + i].toInt() xor value.toInt()
            value = (crcTable[data and 0xff].toInt() xor (value.toInt() shl 8)).toByte()
        }
    }

    override fun update(b: Int) {
        update(byteArrayOf(b.toByte()), 0, 1)
    }

    override fun getValue(): Long {
        return (value.toInt() and 0xff).toLong()
    }

    override fun reset() {
        value = init.toByte()
    }

    /**
     * Construct a CRC8 specifying the polynomial and initial value.
     * @param polynomial Polynomial, typically one of the POLYNOMIAL_* constants.
     * @param init Initial value, typically either 0xff or zero.
     */
    init {
        value = init.toByte()
        for (dividend in 0..255) {
            var remainder = dividend //<< 8;
            for (bit in 0..7) remainder = if (remainder and 0x01 != 0) remainder ushr 1 xor polynomial else remainder ushr 1
            crcTable[dividend] = remainder.toByte()
        }
    }
}
