package se.nordicehealth.ppc_app.implementation.res;

import android.content.res.Resources;
import se.nordicehealth.ppc_app.core.interfaces.Messages;

class ResourceStrings implements Messages
{
    static ResourceStrings getMessages() throws NullPointerException
    {
        if (self == null)
            throw new NullPointerException("Messages have not been loaded");
        return self;
    }

    @Override
    public String error(ERROR errorName)
    {
        return error.message.get(errorName);
    }

    @Override
    public String info(INFO infoName)
    {
        return info.message.get(infoName);
    }

    static boolean loadMessages(Resources r)
    {
        if (self == null)
            self = new ResourceStrings(r);
        return true;
    }

    private InfoMessages info;
    private ErrorMessages error;
    private static ResourceStrings self;

    private ResourceStrings(Resources r) {
        info = new InfoMessages(r);
        error = new ErrorMessages(r);
    }
}