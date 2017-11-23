package se.nordicehealth.ppc_app.implementation.containerdisplay;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import se.nordicehealth.ppc_app.core.containers.form.TimePeriodContainer;
import se.nordicehealth.ppc_app.core.interfaces.Implementations;
import se.nordicehealth.ppc_app.core.interfaces.Messages;
import se.nordicehealth.ppc_app.core.interfaces.UserInterface.FormComponentDisplay;

public class TimePeriodDisplay extends LinearLayout implements FormComponentDisplay
{
	@Override
	public boolean fillEntry()
	{
		return tpc.setEntry(
				new GregorianCalendar(dpFrom.getYear(), dpFrom.getMonth(), dpFrom.getDayOfMonth()),
				new GregorianCalendar(dpTo.getYear(), dpTo.getMonth(), dpTo.getDayOfMonth()));
	}

	@Override
	public boolean entryIsFilled()
	{
		return tpc.hasEntry();
	}

	protected TimePeriodDisplay(Context c, TimePeriodContainer tpc)
	{
		super(c);
		setOrientation(LinearLayout.VERTICAL);
		this.tpc = tpc;

		addView(titleArea(c));
		addView(datePanel(c));
	}

	private TimePeriodContainer tpc;
	private DatePicker dpFrom, dpTo;

    private Messages msg = Implementations.Messages();

	private DatePicker makeCalendar(final Context c, Calendar initial, Calendar lower, Calendar upper)
	{
		DatePicker dp = new DatePicker(c);
		dp.updateDate(initial.get(Calendar.YEAR), initial.get(Calendar.MONTH), initial.get(Calendar.DAY_OF_MONTH));
		dp.setMinDate(lower.getTimeInMillis());
		dp.setMaxDate(upper.getTimeInMillis());
		return dp;
	}

	private Calendar setFirstDayInMonth(Calendar cal)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1, 0, 0, 0 );
        return calendar;
    }

    private Calendar setLastDayInMonth(Calendar cal)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, 0, 0, 0, 0 );
        return calendar;
    }

    private LinearLayout datePanel(final Context c)
    {
        LinearLayout date = new LinearLayout(c);
        date.setOrientation(LinearLayout.VERTICAL);
        date.setLayoutParams(new FrameLayout.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT));

        Calendar upper = setLastDayInMonth(tpc.getUpperLimit());
        Calendar lower = setFirstDayInMonth(tpc.getLowerLimit());
        dpFrom = makeCalendar(c, lower, lower, upper);
        dpTo = makeCalendar(c, upper, lower, upper);

        date.addView(calendarPanel(c, dpFrom, msg.info(Messages.INFO.VD_DATE_FROM)));
        date.addView(calendarPanel(c, dpTo, msg.info(Messages.INFO.VD_DATE_TO)));
        return date;
    }

	private LinearLayout calendarPanel(final Context c, DatePicker dp, String description)
	{
		LinearLayout to = new LinearLayout(c);
		to.setOrientation(LinearLayout.VERTICAL);
		TextView tv = new TextView(c);
		tv.setLayoutParams(new FrameLayout.LayoutParams(
				LinearLayoutCompat.LayoutParams.MATCH_PARENT,
				LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
		tv.setText(description);
		to.addView(tv);
		to.addView(dp);
		return to;
	}

    private TextView titleArea(final Context c)
    {
        TextView jta = new TextView(c);
        jta.setLayoutParams(new FrameLayout.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
        jta.setSingleLine(false);
        jta.setMaxLines(35);
        jta.setText(optionalText() + tpc.getStatement() + description() + "\n");
        return jta;
    }

    private String description()
    {
        String description = "";
        if (tpc.getDescription() != null && !tpc.getDescription().isEmpty())
            description = "\n\n"+tpc.getDescription();
        return description;
    }

    private String optionalText()
    {
        if (tpc.allowsEmpty())
            return String.format("(%s) ", msg.info(Messages.INFO.UI_FORM_OPTIONAL));
        else
            return "";
    }
}
