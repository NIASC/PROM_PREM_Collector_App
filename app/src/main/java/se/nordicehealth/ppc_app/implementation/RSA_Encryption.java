/*! SHA_Encryption.java
 * 
 * Copyright 2017 Marcus Malmquist
 * 
 * This file is part of PROM_PREM_Collector.
 * 
 * PROM_PREM_Collector is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * PROM_PREM_Collector is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with PROM_PREM_Collector.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
package se.nordicehealth.ppc_app.implementation;

import java.math.BigInteger;
import java.util.Locale;

import se.nordicehealth.ppc_app.core.interfaces.Encryption;

/**
 * This class is an example of an implementation of
 * Entryption_Interface.
 * 
 * @author Marcus Malmquist
 *
 */
public class RSA_Encryption implements Encryption
{
	/* Public */
	
	/**
	 * Initializes variables.
	 */
	public RSA_Encryption()
	{
		e = ResourceKeys.getKeys().getExp();
		n = ResourceKeys.getKeys().getMod();
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
	
	/* Protected */
	
	/* Private */
	private BigInteger e, n;

	private byte[] encryptRSA(byte msgBytes[])
	{
		return new BigInteger(msgBytes).modPow(e, n).toByteArray();
	}
}
