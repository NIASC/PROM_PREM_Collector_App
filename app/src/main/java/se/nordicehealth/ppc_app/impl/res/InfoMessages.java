package se.nordicehealth.ppc_app.impl.res;

import android.content.res.Resources;

import java.util.HashMap;
import java.util.Map;

import se.nordicehealth.ppc_app.R;
import se.nordicehealth.ppc_app.core.interfaces.Messages;

final class InfoMessages
{
    final Map<Messages.INFO, String> message;

    InfoMessages(Resources r)
    {
        message = new HashMap<>();
        message.put(Messages.INFO.SELECT_OPTION, r.getString(R.string.INFO_SELECT_OPTION));
        message.put(Messages.INFO.EXIT, r.getString(R.string.INFO_EXIT));
        message.put(Messages.INFO.LOGIN, r.getString(R.string.INFO_LOGIN));
        message.put(Messages.INFO.REGISTER, r.getString(R.string.INFO_REGISTER));
        message.put(Messages.INFO.NEW_PASS_INFO, r.getString(R.string.INFO_NEW_PASS_INFO));
        message.put(Messages.INFO.CURRENT_PASSWORD, r.getString(R.string.INFO_CURRENT_PASSWORD));
        message.put(Messages.INFO.NEW_PASSWORD, r.getString(R.string.INFO_NEW_PASSWORD));
        message.put(Messages.INFO.RE_NEW_PASSWORD, r.getString(R.string.INFO_RE_NEW_PASSWORD));
        message.put(Messages.INFO.START_QUESTIONNAIRE, r.getString(R.string.INFO_START_QUESTIONNAIRE));
        message.put(Messages.INFO.VIEW_STATISTICS, r.getString(R.string.INFO_VIEW_STATISTICS));
        message.put(Messages.INFO.LOGOUT, r.getString(R.string.INFO_LOGOUT));
        message.put(Messages.INFO.UH_ENTER_USERNAME, r.getString(R.string.INFO_UH_ENTER_USERNAME));
        message.put(Messages.INFO.UH_ENTER_PASSWORD, r.getString(R.string.INFO_UH_ENTER_PASSWORD));
        message.put(Messages.INFO.UH_UPDATE_PASSWORD, r.getString(R.string.INFO_UH_UPDATE_PASSWORD));
        message.put(Messages.INFO.REG_CLINIC_NAME, r.getString(R.string.INFO_REG_CLINIC_NAME));
        message.put(Messages.INFO.REG_USER_NAME, r.getString(R.string.INFO_REG_USER_NAME));
        message.put(Messages.INFO.REG_USER_EMAIL, r.getString(R.string.INFO_REG_USER_EMAIL));
        message.put(Messages.INFO.REG_REQUEST_SENT, r.getString(R.string.INFO_REG_REQUEST_SENT));
        message.put(Messages.INFO.UI_UNFILLED, r.getString(R.string.INFO_UI_UNFILLED));
        message.put(Messages.INFO.UI_FILLED, r.getString(R.string.INFO_UI_FILLED));
        message.put(Messages.INFO.UI_FORM_OPTIONAL, r.getString(R.string.INFO_UI_FORM_OPTIONAL));
        message.put(Messages.INFO.UI_FORM_BACK, r.getString(R.string.INFO_UI_FORM_BACK));
        message.put(Messages.INFO.UI_FORM_NEXT, r.getString(R.string.INFO_UI_FORM_NEXT));
        message.put(Messages.INFO.UI_FORM_PREVIOUS, r.getString(R.string.INFO_UI_FORM_PREVIOUS));
        message.put(Messages.INFO.UI_FORM_CONTINUE, r.getString(R.string.INFO_UI_FORM_CONTINUE));
        message.put(Messages.INFO.UI_FORM_FINISH, r.getString(R.string.INFO_UI_FORM_FINISH));
        message.put(Messages.INFO.VD_SELECT_PERIOD, r.getString(R.string.INFO_VD_SELECT_PERIOD));
        message.put(Messages.INFO.VD_SELECT_QUESTIONS, r.getString(R.string.INFO_VD_SELECT_QUESTIONS));
        message.put(Messages.INFO.VD_DATE_FROM, r.getString(R.string.INFO_VD_DATE_FROM));
        message.put(Messages.INFO.VD_DATE_TO, r.getString(R.string.INFO_VD_DATE_TO));
        message.put(Messages.INFO.Q_PATIENT_FORENAME, r.getString(R.string.INFO_Q_PATIENT_FORENAME));
        message.put(Messages.INFO.Q_PATIENT_SURNAME, r.getString(R.string.INFO_Q_PATIENT_SURNAME));
        message.put(Messages.INFO.Q_PATIENT_PNR, r.getString(R.string.INFO_Q_PATIENT_PNR));
    }
}