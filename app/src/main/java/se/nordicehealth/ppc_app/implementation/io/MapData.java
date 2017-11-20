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

    void put(String key, String value)
    {
        jmap.put(key, value);
    }

    String get(String key)
    {
        return jmap.get(key);
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