package se.nordicehealth.ppc_app.impl.containerdisplay.extended;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;

public class MyCheckBox extends AppCompatCheckBox
{
    public MyCheckBox(Context c)
    {
        super(c);
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public String getLabel()
    {
        return label;
    }

    private String label;
}
