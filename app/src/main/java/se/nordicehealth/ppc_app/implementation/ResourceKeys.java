package se.nordicehealth.ppc_app.implementation;

import android.content.res.Resources;

import java.math.BigInteger;

import se.nordicehealth.ppc_app.R;

class ResourceKeys
{
    static ResourceKeys getKeys() throws NullPointerException
    {
        if (self == null)
            throw new NullPointerException("Keys have not been loaded");
        return self;
    }

    BigInteger getExp()
    {
        return exp;
    }

    BigInteger getMod()
    {
        return mod;
    }

	/* Protected */

	/* Package */

    /**
     * Loads messages from the resources.
     *
     * @return {@code true} if messages was successfully loaded.
     * 		{@code false} if an error occurred while loading messages.
     */
    static boolean loadKeys(Resources r)
    {
        if (self == null)
            self = new ResourceKeys(r);
        return true;
    }

	/* Private */

	private BigInteger exp, mod;
    private static ResourceKeys self;

    private ResourceKeys(Resources r) {
        exp = new BigInteger(r.getString(R.string.exp), 16);
        mod = new BigInteger(r.getString(R.string.mod), 16);
    }
}