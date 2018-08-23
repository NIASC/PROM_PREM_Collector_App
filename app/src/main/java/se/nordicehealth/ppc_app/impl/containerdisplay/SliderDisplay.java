package se.nordicehealth.ppc_app.impl.containerdisplay;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import se.nordicehealth.ppc_app.core.containers.form.SliderContainer;
import se.nordicehealth.ppc_app.core.interfaces.Implementations;
import se.nordicehealth.ppc_app.core.interfaces.Messages;
import se.nordicehealth.ppc_app.core.interfaces.UserInterface.FormComponentDisplay;

public class SliderDisplay extends LinearLayout implements FormComponentDisplay
{
	@Override
	public boolean fillEntry()
	{
		return sc.setEntry(response);
	}

	@Override
	public boolean entryIsFilled()
	{
		return sc.hasEntry();
	}

	protected SliderDisplay(Context c, SliderContainer sc)
	{
		super(c);
		setOrientation(LinearLayout.VERTICAL);
		this.sc = sc;
		response = sc.lowerBound();

		addView(titleArea(c));
		addView(makeSlider(c));
	}

	private SliderContainer sc;
	private Integer response;

	private Messages msg = Implementations.Messages();

	private TextView titleArea(final Context c)
	{
		TextView jta = new TextView(c);
		jta.setLayoutParams(new FrameLayout.LayoutParams(
				LinearLayoutCompat.LayoutParams.MATCH_PARENT,
				LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
		jta.setSingleLine(false);
		jta.setMaxLines(35);
        jta.setText(optionalText() + sc.getStatement() + description() + "\n");
		return jta;
	}

	private String description()
	{
		String description = "";
		if (sc.getDescription() != null && !sc.getDescription().isEmpty())
			description = "\n\n"+sc.getDescription();
		return description;
	}

	private String optionalText()
	{
		if (sc.allowsEmpty())
			return String.format("(%s) ", msg.info(Messages.INFO.UI_FORM_OPTIONAL));
		else
			return "";
	}

	private SeekBar makeSlider(final Context c)
	{
		SeekBar slider = new SeekBar(c);
		//slider.setMin(sc.lowerBound());
		slider.setMax(sc.upperBound());
		slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				response = progress;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { }

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) { }
		});
		return slider;
	}
}
