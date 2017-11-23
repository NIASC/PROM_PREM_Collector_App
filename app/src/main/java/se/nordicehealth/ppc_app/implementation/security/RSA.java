package se.nordicehealth.ppc_app.implementation.security;

import java.math.BigInteger;
import java.util.Locale;

public class RSA implements Encryption
{
	public RSA(BigInteger exp, BigInteger mod)
	{
		e = exp;
		n = mod;
	}

	@Override
	public String encrypt(String message)
	{
		byte b[] = encryptRSA(message.getBytes());

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.length; sb.append(i < b.length ? ":" : ""))
			sb.append(String.format(Locale.US, "%02x", b[i++]));
		return sb.toString();
	}

	private BigInteger e, n;

	private byte[] encryptRSA(byte msgBytes[])
	{
		return new BigInteger(msgBytes).modPow(e, n).toByteArray();
	}
}
