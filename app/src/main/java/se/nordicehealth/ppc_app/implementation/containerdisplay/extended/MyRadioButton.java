package se.nordicehealth.ppc_app.implementation.containerdisplay.extended;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;

public class MyRadioButton extends AppCompatRadioButton
{
    public MyRadioButton(Context c)
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
