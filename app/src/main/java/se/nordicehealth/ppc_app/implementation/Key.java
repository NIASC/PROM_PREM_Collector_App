package se.nordicehealth.ppc_app.implementation;

import java.math.BigInteger;

public interface Key
{
    BigInteger exp();
    BigInteger mod();
}
