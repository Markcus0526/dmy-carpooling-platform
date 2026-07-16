package com.webapi.crypto.util;

import java.math.BigInteger;


/**
 * Hex helpers for talking to Ethereum-flavored JSON-RPC. Everything the RPC
 * returns is either "0x"-prefixed hex or a plain string, so this file keeps
 * the parsing in one place rather than sprinkling substring(2) everywhere.
 */
public final class HexUtil
{
	private HexUtil() {}

	public static BigInteger parseQuantity(String hex)
	{
		if (hex == null || hex.equals("0x") || hex.isEmpty())
			return BigInteger.ZERO;
		return new BigInteger(strip0x(hex), 16);
	}

	public static String strip0x(String s)
	{
		return (s != null && s.startsWith("0x")) ? s.substring(2) : s;
	}

	/** Left-strip leading zeros from a 32-byte topic to get an address. */
	public static String topicToAddress(String topic)
	{
		String s = strip0x(topic);
		if (s.length() < 40) return "0x" + s;
		return "0x" + s.substring(s.length() - 40).toLowerCase();
	}

	public static BigInteger topicToUint(String topic)
	{
		return parseQuantity(topic);
	}
}
