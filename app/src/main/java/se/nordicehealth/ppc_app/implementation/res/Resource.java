package se.nordicehealth.ppc_app.implementation.res;

import android.content.res.Resources;

import se.nordicehealth.ppc_app.core.interfaces.Messages;
import se.nordicehealth.ppc_app.implementation.Key;

public class Resource
{
    public static void loadResources(android.content.res.Resources r)
    {
        ResourceStrings.loadMessages(r);
        ResourceKeys.loadKeys(r);
    }

    public static Key key()
    {
        return ResourceKeys.getKeys();
    }

    public static Messages messages()
    {
        return ResourceStrings.getMessages();
    }
}