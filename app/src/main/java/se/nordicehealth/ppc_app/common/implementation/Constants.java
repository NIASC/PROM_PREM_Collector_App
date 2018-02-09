package se.nordicehealth.ppc_app.common.implementation;

import android.util.Log;

import java.io.IOException;
import java.net.URL;

public abstract class Constants {

	public static final URL SERVER_URL;
	
	static
    {
		URL url = null;
		try {
            //url = new URL("http://188.114.242.3:443/PROM_PREM_Collector/main");
			url = new URL("http://192.168.1.22:8080/PROM_PREM_Collector/main");
		}
		catch (IOException | IllegalArgumentException ignore) {
			Log.e("ECONN", ignore.getMessage());
		}
		SERVER_URL = url;
	}

    public enum QuestionTypes {
        __NULL__,
        SINGLE_OPTION,
        MULTIPLE_OPTION,
        SLIDER,
        AREA
    }

    public static <T extends Enum<T>> T getEnum(T v[], int ordinal) {
        for (T t : v) {
            if (t.ordinal() == ordinal) {
                return t;
            }
        }
        return v[0];
    }

    public static <T extends Enum<T>> T getEnum(T v[], String ordinalStr) throws NumberFormatException {
        return getEnum(v, Integer.parseInt(ordinalStr));
    }

    public static <T extends Enum<T>> boolean equal(T lhs, T rhs) {
        return lhs.compareTo(rhs) == 0;
    }
}
