package se.nordicehealth.ppc_app.impl;

import java.math.BigInteger;

public interface Key
{
    BigInteger exp();
    BigInteger mod();
}
