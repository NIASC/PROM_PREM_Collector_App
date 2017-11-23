package se.nordicehealth.ppc_app.core;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import se.nordicehealth.ppc_app.core.containers.QuestionContainer;
import se.nordicehealth.ppc_app.core.containers.StatisticsContainer;
import se.nordicehealth.ppc_app.core.containers.ViewDataContainer;
import se.nordicehealth.ppc_app.core.containers.form.FormContainer;
import se.nordicehealth.ppc_app.core.containers.form.MultipleOptionContainer;
import se.nordicehealth.ppc_app.core.containers.form.TimePeriodContainer;
import se.nordicehealth.ppc_app.core.interfaces.Server;
import se.nordicehealth.ppc_app.core.interfaces.FormUtils;
import se.nordicehealth.ppc_app.core.interfaces.Implementations;
import se.nordicehealth.ppc_app.core.interfaces.Messages;
import se.nordicehealth.ppc_app.core.interfaces.Questions;
import se.nordicehealth.ppc_app.core.interfaces.UserInterface;

class ViewData
{
	ViewData(UserInterface ui, UserHandle uh)
	{
		this.ui = ui;
		this.uh = uh;
		dateSel = new DateSelection();
	}

	void start()
	{
		dateSel.createForm();
	}
	
	private UserHandle uh;
	private UserInterface ui;
	private DateSelection dateSel;
	private ViewDataContainer vdc;
	
	private class DateSelection implements FormUtils
	{
		@Override
		public RetFunContainer validateUserInput(List<FormContainer> form) {
			RetFunContainer rfc = new RetFunContainer();

			MultipleOptionContainer questionselect = (MultipleOptionContainer) form.get(0);
			TimePeriodContainer timeperiod = (TimePeriodContainer) form.get(1);
			
			List<Calendar> bounds = timeperiod.getEntry();
			Calendar lower = bounds.get(0);
			Calendar upper = bounds.get(1);
            int nEntries = timeperiod.getPeriodEntries();

            rfc.message = errorMessages(lower, upper, nEntries);
            if (rfc.message == null) {
                List<Integer> selQuestions = questionselect.getEntry();
                StatisticsContainer sc = new StatisticsContainer();
                db.loadQResults(uh.getUID(), lower, upper, selQuestions, sc);
                vdc = new ViewDataContainer(sc.getStatistics(), upper, lower, nEntries);
                rfc.valid = true;
            }
            return rfc;
		}

		@Override
		public void callNext() {
			ui.presentViewData(vdc);
		}

		Server db;
        Messages msg;

		DateSelection() {
            msg = Implementations.Messages();
            db = Implementations.Server();
        }

        String errorMessages(Calendar lower, Calendar upper, int entries)
        {
            if (lower == null || upper == null)
                return msg.error(Messages.ERROR.VD_INVALID_PERIOD);
            else if (entries < 5)
                return msg.error(Messages.ERROR.VD_FEW_ENTRIES);
            else
                return null;
        }

		void createForm()
		{
            List<FormContainer> form = new LinkedList<>();
			QuestionContainer qc = Questions.getQuestions().getContainer();
			MultipleOptionContainer questionselect =
					new MultipleOptionContainer(false, msg.info(Messages.INFO.VD_SELECT_PERIOD), null);

			for (int i = 0; i < qc.getSize(); ++i)
				questionselect.addOption(i, qc.getContainer(i).getStatement());
			form.add(questionselect);

			TimePeriodContainer timeperiod =
					new TimePeriodContainer(false, msg.info(Messages.INFO.VD_SELECT_QUESTIONS), null);
			db.loadQResultDates(uh.getUID(), timeperiod);
			form.add(timeperiod);

			ui.presentForm(form, this, false);
		}
		
	}
}
