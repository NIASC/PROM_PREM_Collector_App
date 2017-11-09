package se.nordicehealth.ppc_app.implementation;

import android.content.res.Resources;

import java.util.HashMap;
import java.util.Map;

import se.nordicehealth.ppc_app.R;
import se.nordicehealth.ppc_app.core.interfaces.Messages;

final class ErrorMessages
{
    public final Map<String, String> msgError;

    ErrorMessages(Resources r)
    {
        msgError = new HashMap<>();
        msgError.put(Messages.ERROR_UH_PR_PASSWORD_SIMPLE,
                r.getString(R.string.ERROR_UH_PR_PASSWORD_SIMPLE));
        msgError.put(Messages.ERROR_UH_PR_INVALID_LENGTH,
                r.getString(R.string.ERROR_UH_PR_INVALID_LENGTH));
        msgError.put(Messages.ERROR_UH_PR_MISMATCH_NEW,
                r.getString(R.string.ERROR_UH_PR_MISMATCH_NEW));
        msgError.put(Messages.ERROR_UH_PR_INVALID_CURRENT,
                r.getString(R.string.ERROR_UH_PR_INVALID_CURRENT));
        msgError.put(Messages.ERROR_UH_INVALID_LOGIN,
                r.getString(R.string.ERROR_UH_INVALID_LOGIN));
        msgError.put(Messages.ERROR_NOT_LOGGED_IN,
                r.getString(R.string.ERROR_NOT_LOGGED_IN));
        msgError.put(Messages.ERROR_OPERATION_NOT_PERMITTED,
                r.getString(R.string.ERROR_OPERATION_NOT_PERMITTED));
        msgError.put(Messages.ERROR_NULL_SELECTED,
                r.getString(R.string.ERROR_NULL_SELECTED));
        msgError.put(Messages.ERROR_UNKNOWN_RESPONSE,
                r.getString(R.string.ERROR_UNKNOWN_RESPONSE));
        msgError.put(Messages.ERROR_UH_ALREADY_ONLINE,
                r.getString(R.string.ERROR_UH_ALREADY_ONLINE));
        msgError.put(Messages.ERROR_UH_SERVER_FULL,
                r.getString(R.string.ERROR_UH_SERVER_FULL));
        msgError.put(Messages.ERROR_REG_REQUEST_FAILED,
                r.getString(R.string.ERROR_REG_REQUEST_FAILED));
        msgError.put(Messages.ERROR_QP_INVALID_PID,
                r.getString(R.string.ERROR_QP_INVALID_PID));
        msgError.put(Messages.ERROR_VD_INVALID_PERIOD,
                r.getString(R.string.ERROR_VD_INVALID_PERIOD));
        msgError.put(Messages.ERROR_VD_FEW_ENTRIES,
                r.getString(R.string.ERROR_VD_FEW_ENTRIES));
    }
}