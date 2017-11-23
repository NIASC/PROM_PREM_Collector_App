package se.nordicehealth.ppc_app.implementation.res;

import android.content.res.Resources;

import java.math.BigInteger;

import se.nordicehealth.ppc_app.R;
import se.nordicehealth.ppc_app.implementation.Key;

class ResourceKeys implements Key
{
    @Override
    public BigInteger exp()
    {
        return exp;
    }

    @Override
    public BigInteger mod()
    {
        return mod;
    }

    static ResourceKeys getKeys() throws NullPointerException
    {
        if (self == null)
            throw new NullPointerException("Keys have not been loaded");
        return self;
    }

    static void loadKeys(Resources r)
    {
        self = new ResourceKeys(r);
    }

    private static ResourceKeys self;
	private BigInteger exp, mod;

    private ResourceKeys(Resources r)
    {
        exp = new BigInteger(r.getString(R.string.exp), 16);
        mod = new BigInteger(r.getString(R.string.mod), 16);
    }
}