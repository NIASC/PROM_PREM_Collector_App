package se.nordicehealth.ppc_app.implementation.containerdisplay;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import se.nordicehealth.ppc_app.core.containers.form.AreaContainer;
import se.nordicehealth.ppc_app.core.interfaces.Implementations;
import se.nordicehealth.ppc_app.core.interfaces.Messages;
import se.nordicehealth.ppc_app.core.interfaces.UserInterface.FormComponentDisplay;

public class AreaDisplay extends LinearLayout implements FormComponentDisplay
{
	@Override
	public boolean fillEntry()
	{
		return ac.setEntry(area.getText().toString());
	}

	@Override
	public boolean entryIsFilled()
	{
		if (!area.getText().toString().trim().isEmpty())
			fillEntry();
		return ac.hasEntry();
	}

	protected AreaDisplay(Context c, AreaContainer ac)
	{
		super(c);
		setOrientation(LinearLayout.VERTICAL);
		this.ac = ac;

        area = textArea(c);
        addView(titleArea(c));
		addView(area);
	}

	private AreaContainer ac;
	private EditText area;
    private Messages msg = Implementations.Messages();

    private EditText textArea(final Context c)
    {
        EditText area = new EditText(c);
        area.setLayoutParams(new FrameLayout.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
        area.setSingleLine(false);
        area.setMaxLines(35);
        return area;
    }

    private TextView titleArea(final Context c)
    {
        TextView jta = new TextView(c);
        jta.setLayoutParams(new FrameLayout.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
        jta.setSingleLine(false);
        jta.setMaxLines(35);
        jta.setText(optionalText() + ac.getStatement() + getDescription() + "\n");
        return jta;
    }

    private String getDescription()
    {
        String description = "";
        if (ac.getDescription() != null && !ac.getDescription().isEmpty())
            description = "\n\n"+ac.getDescription();
        return description;
    }

    private String optionalText()
    {
        if (ac.allowsEmpty())
            return String.format("(%s) ", msg.info(Messages.INFO.UI_FORM_OPTIONAL));
        else
            return "";
    }
}
