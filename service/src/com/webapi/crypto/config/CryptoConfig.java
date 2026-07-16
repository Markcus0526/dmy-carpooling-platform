package com.webapi.crypto.config;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Properties;


/**
 * Loads crypto.properties from the classpath (WEB-INF/classes/).
 *
 * Values are read once at first access and cached. To rotate the escrow
 * address or arbiter key, redeploy the war — matches the project's existing
 * config-via-XML/properties pattern (see DBManager, ApiGlobal).
 */
public final class CryptoConfig
{
	private static final String FILE = "crypto.properties";

	private static Properties cached;

	private CryptoConfig() {}

	private static synchronized Properties props()
	{
		if (cached != null)
			return cached;

		Properties p = new Properties();
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		try (InputStream in = cl.getResourceAsStream(FILE))
		{
			if (in == null)
				throw new IllegalStateException(FILE + " not on classpath");
			p.load(in);
		}
		catch (IOException ex)
		{
			throw new IllegalStateException("failed to load " + FILE, ex);
		}
		cached = p;
		return cached;
	}

	public static String rpcUrl()          { return props().getProperty("crypto.rpc.url"); }
	public static long   chainId()         { return Long.parseLong(props().getProperty("crypto.chain.id")); }
	public static String escrowAddress()   { return props().getProperty("crypto.escrow.address").toLowerCase(); }
	public static int    minConfirmations(){ return Integer.parseInt(props().getProperty("crypto.min.confirmations", "2")); }

	/**
	 * How much on-chain wei corresponds to 1 CNY of app-side price.
	 * Since ETH/CNY rates fluctuate, we let ops set a conversion rate at
	 * deploy time (or the frontend quotes the wei amount and the backend
	 * just validates that the *contract event* recorded a plausibly correct
	 * amount for the order). For POC we require exact match against a value
	 * the frontend also computed — the backend re-derives it here.
	 *
	 * Returns wei per 1 CNY.
	 */
	public static BigInteger weiPerCny()
	{
		String v = props().getProperty("crypto.wei.per.cny");
		return new BigInteger(v);
	}
}
