package se.nordicehealth.ppc_app.implementation.res;

import android.content.res.Resources;
import android.util.Log;

import java.io.IOException;
import java.net.URL;

import se.nordicehealth.ppc_app.R;
import se.nordicehealth.ppc_app.implementation.Data;

public class ResourceData implements Data {
    @Override
    public URL getServerURL() {
        return SERVER_URL;
    }

    static void loadData(Resources r) {
        self = new ResourceData(r);
    }

    static Data getData() throws NullPointerException {
        if (self == null)
            throw new NullPointerException("Data have not been loaded");
        return self;
    }

    private static ResourceData self;
    private URL SERVER_URL;

    private ResourceData(Resources r) {
        try {
            SERVER_URL = new URL(r.getString(R.string.servlet_url));
        }
        catch (IOException e) {
            Log.e("ECONN", e.getMessage());
        }
    }
}
