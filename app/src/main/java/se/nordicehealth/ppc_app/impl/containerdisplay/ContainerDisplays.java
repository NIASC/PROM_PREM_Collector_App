package se.nordicehealth.ppc_app.impl.containerdisplay;

import android.content.Context;

import se.nordicehealth.ppc_app.core.containers.form.AreaContainer;
import se.nordicehealth.ppc_app.core.containers.form.FieldContainer;
import se.nordicehealth.ppc_app.core.containers.form.FormContainer;
import se.nordicehealth.ppc_app.core.containers.form.MultipleOptionContainer;
import se.nordicehealth.ppc_app.core.containers.form.SingleOptionContainer;
import se.nordicehealth.ppc_app.core.containers.form.SliderContainer;
import se.nordicehealth.ppc_app.core.containers.form.TimePeriodContainer;
import se.nordicehealth.ppc_app.core.interfaces.UserInterface.FormComponentDisplay;

public abstract class ContainerDisplays
{
	public static FormComponentDisplay getDisplay(Context c, FormContainer fc)
	{
		if (fc instanceof SingleOptionContainer) {
			if (fc instanceof MultipleOptionContainer)
				return new MultipleOptionDisplay(c, (MultipleOptionContainer) fc);
			return new SingleOptionDisplay(c, (SingleOptionContainer) fc);
		} else if (fc instanceof AreaContainer) {
			if (fc instanceof FieldContainer)
				return new FieldDisplay(c, (FieldContainer) fc);
			return new AreaDisplay(c, (AreaContainer) fc);
		} else if (fc instanceof SliderContainer)
			return new SliderDisplay(c, (SliderContainer) fc);
		else if (fc instanceof TimePeriodContainer)
			return new TimePeriodDisplay(c, (TimePeriodContainer) fc);
		else
			return null;
	}
}
