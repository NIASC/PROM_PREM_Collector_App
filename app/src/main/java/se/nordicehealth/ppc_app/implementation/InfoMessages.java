package se.nordicehealth.ppc_app.implementation;

import android.content.res.Resources;

import java.util.HashMap;
import java.util.Map;

import se.nordicehealth.ppc_app.R;
import se.nordicehealth.ppc_app.core.interfaces.Messages;

final class InfoMessages
{
    public final Map<String, String> msgInfo;

    InfoMessages(Resources r)
    {
        msgInfo = new HashMap<>();
        msgInfo.put(Messages.INFO_SELECT_OPTION,
                r.getString(R.string.INFO_SELECT_OPTION));
        msgInfo.put(Messages.INFO_EXIT,
                r.getString(R.string.INFO_EXIT));
        msgInfo.put(Messages.INFO_LOGIN,
                r.getString(R.string.INFO_LOGIN));
        msgInfo.put(Messages.INFO_REGISTER,
                r.getString(R.string.INFO_REGISTER));
        msgInfo.put(Messages.INFO_NEW_PASS_INFO,
                r.getString(R.string.INFO_NEW_PASS_INFO));
        msgInfo.put(Messages.INFO_CURRENT_PASSWORD,
                r.getString(R.string.INFO_CURRENT_PASSWORD));
        msgInfo.put(Messages.INFO_NEW_PASSWORD,
                r.getString(R.string.INFO_NEW_PASSWORD));
        msgInfo.put(Messages.INFO_RE_NEW_PASSWORD,
                r.getString(R.string.INFO_RE_NEW_PASSWORD));
        msgInfo.put(Messages.INFO_START_QUESTIONNAIRE,
                r.getString(R.string.INFO_START_QUESTIONNAIRE));
        msgInfo.put(Messages.INFO_VIEW_STATISTICS,
                r.getString(R.string.INFO_VIEW_STATISTICS));
        msgInfo.put(Messages.INFO_LOGOUT,
                r.getString(R.string.INFO_LOGOUT));
        msgInfo.put(Messages.INFO_UH_ENTER_USERNAME,
                r.getString(R.string.INFO_UH_ENTER_USERNAME));
        msgInfo.put(Messages.INFO_UH_ENTER_PASSWORD,
                r.getString(R.string.INFO_UH_ENTER_PASSWORD));
        msgInfo.put(Messages.INFO_UH_UPDATE_PASSWORD,
                r.getString(R.string.INFO_UH_UPDATE_PASSWORD));
        msgInfo.put(Messages.INFO_REG_CLINIC_NAME,
                r.getString(R.string.INFO_REG_CLINIC_NAME));
        msgInfo.put(Messages.INFO_REG_USER_NAME,
                r.getString(R.string.INFO_REG_USER_NAME));
        msgInfo.put(Messages.INFO_REG_USER_EMAIL,
                r.getString(R.string.INFO_REG_USER_EMAIL));
        msgInfo.put(Messages.INFO_REG_REQUEST_SENT,
                r.getString(R.string.INFO_REG_REQUEST_SENT));
        msgInfo.put(Messages.INFO_UI_UNFILLED,
                r.getString(R.string.INFO_UI_UNFILLED));
        msgInfo.put(Messages.INFO_UI_FILLED,
                r.getString(R.string.INFO_UI_FILLED));
        msgInfo.put(Messages.INFO_UI_FORM_OPTIONAL,
                r.getString(R.string.INFO_UI_FORM_OPTIONAL));
        msgInfo.put(Messages.INFO_UI_FORM_BACK,
                r.getString(R.string.INFO_UI_FORM_BACK));
        msgInfo.put(Messages.INFO_UI_FORM_NEXT,
                r.getString(R.string.INFO_UI_FORM_NEXT));
        msgInfo.put(Messages.INFO_UI_FORM_PREVIOUS,
                r.getString(R.string.INFO_UI_FORM_PREVIOUS));
        msgInfo.put(Messages.INFO_UI_FORM_CONTINUE,
                r.getString(R.string.INFO_UI_FORM_CONTINUE));
        msgInfo.put(Messages.INFO_UI_FORM_FINISH,
                r.getString(R.string.INFO_UI_FORM_FINISH));
        msgInfo.put(Messages.INFO_VD_SELECT_PERIOD,
                r.getString(R.string.INFO_VD_SELECT_PERIOD));
        msgInfo.put(Messages.INFO_VD_SELECT_QUESTIONS,
                r.getString(R.string.INFO_VD_SELECT_QUESTIONS));
        msgInfo.put(Messages.INFO_VD_DATE_FROM,
                r.getString(R.string.INFO_VD_DATE_FROM));
        msgInfo.put(Messages.INFO_VD_DATE_TO,
                r.getString(R.string.INFO_VD_DATE_TO));
        msgInfo.put(Messages.INFO_Q_PATIENT_FORENAME,
                r.getString(R.string.INFO_Q_PATIENT_FORENAME));
        msgInfo.put(Messages.INFO_Q_PATIENT_SURNAME,
                r.getString(R.string.INFO_Q_PATIENT_SURNAME));
        msgInfo.put(Messages.INFO_Q_PATIENT_PNR,
                r.getString(R.string.INFO_Q_PATIENT_PNR));

    }
}