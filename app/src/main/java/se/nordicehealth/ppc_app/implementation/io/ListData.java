package se.nordicehealth.ppc_app.implementation.io;

import org.json.simple.JSONArray;

import java.util.Collections;
import java.util.List;

class ListData
{
    private JSONArray jarr;
    private List<String> jlist;

    @SuppressWarnings("unchecked")
    ListData(JSONArray jarr)
    {
        this.jarr = jarr != null ? jarr : new JSONArray();
        this.jlist = (List<String>) this.jarr;
    }

    void add(String value)
    {
        jlist.add(value);
    }

    Iterable<String> iterable()
    {
        return Collections.unmodifiableList(jlist);
    }

    public String toString()
    {
        return jarr.toString();
    }
}