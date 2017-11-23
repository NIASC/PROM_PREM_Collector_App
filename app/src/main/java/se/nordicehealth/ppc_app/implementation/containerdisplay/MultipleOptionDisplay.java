package se.nordicehealth.ppc_app.implementation.containerdisplay;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import se.nordicehealth.ppc_app.core.containers.form.MultipleOptionContainer;
import se.nordicehealth.ppc_app.core.containers.form.SingleOptionContainer;
import se.nordicehealth.ppc_app.core.interfaces.Implementations;
import se.nordicehealth.ppc_app.core.interfaces.Messages;
import se.nordicehealth.ppc_app.core.interfaces.UserInterface;
import se.nordicehealth.ppc_app.core.interfaces.UserInterface.FormComponentDisplay;
import se.nordicehealth.ppc_app.implementation.containerdisplay.extended.MyCheckBox;

public class MultipleOptionDisplay extends LinearLayout implements FormComponentDisplay
{
	@Override
	public boolean fillEntry()
	{
		return moc.setEntry(responseID);
	}

	@Override
	public boolean entryIsFilled()
	{
		return moc.hasEntry();
	}

	protected MultipleOptionDisplay(Context c, MultipleOptionContainer moc)
	{
		super(c);
		setOrientation(LinearLayout.VERTICAL);
		this.moc = moc;
		responseID = new ArrayList<>();
        options = new TreeMap<>();

		addView(titleArea(c));
		addView(buttonPanel(c));
	}

	private MultipleOptionContainer moc;
	private List<Integer> responseID;
	private Map<String, MyCheckBox> options;

    private Messages msg = Implementations.Messages();

    private TextView titleArea(final Context c)
    {
        TextView jta = new TextView(c);
        jta.setLayoutParams(new FrameLayout.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
        jta.setSingleLine(false);
        jta.setMaxLines(35);
        jta.setText(optionalText() + moc.getStatement() + description() + "\n");
        return jta;
    }

    private LinearLayout buttonPanel(final Context c)
    {
        LinearLayout buttonPanel = new LinearLayout(c);
        buttonPanel.setOrientation(LinearLayout.VERTICAL);

        Map<Integer, String> opt = moc.getOptions();
        List<Integer> selected = moc.getSelectedID();

        for (Entry<Integer, String> e : opt.entrySet()) {
            String buttonName = Integer.toString(e.getKey());
            MyCheckBox btn = makeButton(c, selected.contains(e.getKey()), buttonName, e.getValue());
            buttonPanel.addView(btn);
            options.put(buttonName, btn);
        }
        return buttonPanel;
    }

    private MyCheckBox makeButton(final Context c, boolean selected, String label, String description)
    {
        MyCheckBox btn = new MyCheckBox(c);
        btn.setText(description);
        btn.setLabel(label);
        if (selected)
            btn.setSelected(true);
        btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                MyCheckBox button = (MyCheckBox) buttonView;
                if (options.get(button.getLabel()) == null)
                    return;

                if (isChecked)
                    responseID.add(Integer.parseInt(button.getLabel()));
                else
                    responseID.remove(Integer.valueOf(button.getLabel()));
            }
        });
        return btn;
    }

    private String description()
    {
        String description = "";
        if (moc.getDescription() != null && !moc.getDescription().isEmpty())
            description = "\n\n"+moc.getDescription();
        return description;
    }

    private String optionalText()
    {
        if (moc.allowsEmpty())
            return String.format("(%s) ", msg.info(Messages.INFO.UI_FORM_OPTIONAL));
        else
            return "";
    }
}
