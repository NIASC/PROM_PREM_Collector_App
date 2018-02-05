package se.nordicehealth.ppc_app.implementation.io;

import org.json.simple.JSONObject;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

class MapData
{
    private JSONObject jobj;
    private Map<String, String> jmap;

    @SuppressWarnings("unchecked")
    MapData(JSONObject jobj)
    {
        this.jobj = jobj != null ? jobj : new JSONObject();
        this.jmap = (Map<String, String>) this.jobj;
    }

    void put(Enum<?> key, Enum<?> value)
    {
        jmap.put(Integer.toString(key.ordinal()),
                Integer.toString(value.ordinal()));
    }

    void put(Enum<?> key, String value)
    {
        jmap.put(Integer.toString(key.ordinal()), value);
    }

    String get(Enum<?> key)
    {
        return jmap.get(Integer.toString(key.ordinal()));
    }

    Iterable<Entry<String, String>> iterable()
    {
        return jmap.entrySet();
    }

    Map<String, String> map()
    {
        return Collections.unmodifiableMap(jmap);
    }

    public String toString()
    {
        return jobj.toString();
    }
}