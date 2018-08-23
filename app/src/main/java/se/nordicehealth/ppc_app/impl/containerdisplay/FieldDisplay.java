package se.nordicehealth.ppc_app.impl.containerdisplay;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import se.nordicehealth.ppc_app.core.containers.form.FieldContainer;
import se.nordicehealth.ppc_app.core.interfaces.UserInterface.FormComponentDisplay;

public class FieldDisplay extends LinearLayout implements FormComponentDisplay
{
	@Override
	public boolean fillEntry()
	{
		return fc.setEntry(field.getText().toString());
	}

	@Override
	public boolean entryIsFilled()
	{
		if (!field.getText().toString().trim().isEmpty())
			fillEntry();
		return fc.hasEntry();
	}

	protected FieldDisplay(Context c, FieldContainer fc)
	{
		super(c);
		setOrientation(LinearLayout.HORIZONTAL);
		this.fc = fc;

		field = fieldComponent(c);
		addView(label(c));
		addView(field);
	}

	private FieldContainer fc;
	private EditText field;

	private EditText fieldComponent(final Context c)
	{
		EditText field = new EditText(c);
		field.setText(fc.getEntry());
		field.setLayoutParams(new FrameLayout.LayoutParams(
				LinearLayoutCompat.LayoutParams.MATCH_PARENT,
				LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
		if (fc.isSecret())
			field.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		field.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus) {
					fillEntry();
				}
			}
		});
		return field;
	}

	private TextView label(final Context c)
	{
		TextView fieldLabel = new TextView(c);
		fieldLabel.setText(String.format("%s: ", fc.getStatement()));
		return fieldLabel;
	}
}
