package se.nordicehealth.ppc_app.implementation.containerdisplay;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import se.nordicehealth.ppc_app.core.containers.form.SingleOptionContainer;
import se.nordicehealth.ppc_app.core.interfaces.Implementations;
import se.nordicehealth.ppc_app.core.interfaces.Messages;
import se.nordicehealth.ppc_app.core.interfaces.UserInterface.FormComponentDisplay;
import se.nordicehealth.ppc_app.implementation.containerdisplay.extended.MyRadioButton;

public class SingleOptionDisplay extends LinearLayout implements FormComponentDisplay
{
	@Override
	public boolean fillEntry()
	{
		return soc.setEntry(Collections.singletonList(responseID));
	}

	@Override
	public boolean entryIsFilled()
	{
		return soc.hasEntry();
	}

	protected SingleOptionDisplay(Context c, SingleOptionContainer soc)
	{
		super(c);
		setOrientation(LinearLayout.VERTICAL);
		this.soc = soc;
		responseID = null;

		addView(titleArea(c));
		addView(buttonPanel(c));
	}

	private SingleOptionContainer soc;
	private Integer responseID;

    private Messages msg = Implementations.Messages();

    private RadioGroup buttonPanel(final Context c)
    {
        RadioGroup group = new RadioGroup(c);
        group.setOrientation(LinearLayout.VERTICAL);

        Map<Integer, String> opt = soc.getOptions();
        List<Integer> selected = soc.getSelectedID();

        for (Entry<Integer, String> e : opt.entrySet()) {
            String label = Integer.toString(e.getKey());
            MyRadioButton btn = makeButton(c, !selected.isEmpty() && selected.get(0).equals(e.getKey()), label, e.getValue());
            group.addView(btn);
        }
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                MyRadioButton sel = group.findViewById(checkedId);
                if (sel.isChecked())
                    responseID = Integer.parseInt(sel.getLabel());
            }
        });
        return group;
    }

    private MyRadioButton makeButton(final Context c, boolean selected, String label, String description)
    {
        MyRadioButton btn = new MyRadioButton(c);
        btn.setText(description);
        btn.setLabel(label);
        if (selected)
            btn.setSelected(true);
        return btn;
    }

    private TextView titleArea(final Context c)
    {
        TextView jta = new TextView(c);
        jta.setLayoutParams(new FrameLayout.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
        jta.setSingleLine(false);
        jta.setMaxLines(35);
        jta.setText(optionalText() + soc.getStatement() + description() + "\n");
        return jta;
    }

    private String description()
    {
        String description = "";
        if (soc.getDescription() != null && !soc.getDescription().isEmpty())
            description = "\n\n"+soc.getDescription();
        return description;
    }

    private String optionalText()
    {
        if (soc.allowsEmpty())
            return String.format("(%s) ", msg.info(Messages.INFO.UI_FORM_OPTIONAL));
        else
            return "";
    }
}
