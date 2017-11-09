package se.nordicehealth.ppc_app.implementation;

import android.content.res.Resources;
import se.nordicehealth.ppc_app.core.interfaces.Messages;

public class ResourceStrings implements Messages
{
    public static ResourceStrings getMessages() throws NullPointerException
    {
        if (self == null)
            throw new NullPointerException("Messages have not been loaded");
        return self;
    }

    @Override
    public String getError(String errorName)
    {
        return emsg.msgError.get(errorName);
    }

    @Override
    public String getInfo(String infoName)
    {
        return imsg.msgInfo.get(infoName);
    }

	/* Protected */

	/* Package */

    /**
     * Loads messages from the resources.
     *
     * @return {@code true} if messages was successfully loaded.
     * 		{@code false} if an error occurred while loading messages.
     */
    static boolean loadMessages(Resources r)
    {
        if (self == null)
            self = new ResourceStrings(r);
        return true;
    }

	/* Private */

    private InfoMessages imsg;
    private ErrorMessages emsg;
    private static ResourceStrings self;

    private ResourceStrings(Resources r) {
        imsg = new InfoMessages(r);
        emsg = new ErrorMessages(r);
    }
}