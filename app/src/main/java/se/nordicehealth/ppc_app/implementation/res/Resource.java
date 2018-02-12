package se.nordicehealth.ppc_app.implementation.res;

import se.nordicehealth.ppc_app.core.interfaces.Messages;
import se.nordicehealth.ppc_app.implementation.Data;
import se.nordicehealth.ppc_app.implementation.Key;

public class Resource
{
    public static void loadResources(android.content.res.Resources r)
    {
        ResourceStrings.loadMessages(r);
        ResourceKeys.loadKeys(r);
        ResourceData.loadData(r);
    }

    public static Key key()
    {
        return ResourceKeys.getKeys();
    }

    public static Messages messages()
    {
        return ResourceStrings.getMessages();
    }

    public static Data data() {
        return ResourceData.getData();
    }
}