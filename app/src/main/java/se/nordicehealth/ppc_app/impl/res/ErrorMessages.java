package se.nordicehealth.ppc_app.impl.res;

import android.content.res.Resources;

import java.util.HashMap;
import java.util.Map;

import se.nordicehealth.ppc_app.R;
import se.nordicehealth.ppc_app.core.interfaces.Messages;

final class ErrorMessages
{
    final Map<Messages.ERROR, String> message;

    ErrorMessages(Resources r)
    {
        message = new HashMap<>();
        message.put(Messages.ERROR.UH_PR_PASSWORD_SIMPLE, r.getString(R.string.ERROR_UH_PR_PASSWORD_SIMPLE));
        message.put(Messages.ERROR.UH_PR_INVALID_LENGTH, r.getString(R.string.ERROR_UH_PR_INVALID_LENGTH));
        message.put(Messages.ERROR.UH_PR_MISMATCH_NEW, r.getString(R.string.ERROR_UH_PR_MISMATCH_NEW));
        message.put(Messages.ERROR.UH_PR_INVALID_CURRENT, r.getString(R.string.ERROR_UH_PR_INVALID_CURRENT));
        message.put(Messages.ERROR.UH_INVALID_LOGIN, r.getString(R.string.ERROR_UH_INVALID_LOGIN));
        message.put(Messages.ERROR.NOT_LOGGED_IN, r.getString(R.string.ERROR_NOT_LOGGED_IN));
        message.put(Messages.ERROR.OPERATION_NOT_PERMITTED, r.getString(R.string.ERROR_OPERATION_NOT_PERMITTED));
        message.put(Messages.ERROR.NULL_SELECTED, r.getString(R.string.ERROR_NULL_SELECTED));
        message.put(Messages.ERROR.UNKNOWN_RESPONSE, r.getString(R.string.ERROR_UNKNOWN_RESPONSE));
        message.put(Messages.ERROR.UH_ALREADY_ONLINE, r.getString(R.string.ERROR_UH_ALREADY_ONLINE));
        message.put(Messages.ERROR.UH_SERVER_FULL, r.getString(R.string.ERROR_UH_SERVER_FULL));
        message.put(Messages.ERROR.REG_REQUEST_FAILED, r.getString(R.string.ERROR_REG_REQUEST_FAILED));
        message.put(Messages.ERROR.QP_INVALID_PID, r.getString(R.string.ERROR_QP_INVALID_PID));
        message.put(Messages.ERROR.VD_INVALID_PERIOD, r.getString(R.string.ERROR_VD_INVALID_PERIOD));
        message.put(Messages.ERROR.VD_FEW_ENTRIES, r.getString(R.string.ERROR_VD_FEW_ENTRIES));
    }
}