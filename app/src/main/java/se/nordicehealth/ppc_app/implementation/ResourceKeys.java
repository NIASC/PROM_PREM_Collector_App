package se.nordicehealth.ppc_app.implementation;

import android.content.res.Resources;

import java.math.BigInteger;

import se.nordicehealth.ppc_app.R;

class ResourceKeys implements Key
{
    static ResourceKeys getKeys() throws NullPointerException
    {
        if (self == null)
            throw new NullPointerException("Keys have not been loaded");
        return self;
    }

    @Override
    public BigInteger getExp()
    {
        return exp;
    }

    @Override
    public BigInteger getMod()
    {
        return mod;
    }

	/* Protected */

	/* Package */

    static void loadKeys(Resources r)
    {
        self = new ResourceKeys(r);
    }

	/* Private */

    private static ResourceKeys self;
	private BigInteger exp, mod;

    private ResourceKeys(Resources r)
    {
        exp = new BigInteger(r.getString(R.string.exp), 16);
        mod = new BigInteger(r.getString(R.string.mod), 16);
    }
}